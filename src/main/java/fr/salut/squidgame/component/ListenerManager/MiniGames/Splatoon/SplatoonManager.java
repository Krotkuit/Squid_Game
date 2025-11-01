package fr.salut.squidgame.component.ListenerManager.MiniGames.Splatoon;

import fr.salut.squidgame.component.commands.games.SplatoonCommand;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static fr.salut.squidgame.component.ListenerManager.MiniGames.Splatoon.ZoneManager.clearWoolZones;

public class SplatoonManager implements Listener {

  public static final Map<Block, ClaimedZone> blockToZone = new HashMap<>();
  public static final Set<ClaimedZone> allZones = new HashSet<>();

  // Quantité de peinture restante pour chaque joueur
  private static final Map<UUID, Integer> paintLeft = new HashMap<>();

  // Association joueur → équipe (4 équipes déjà existantes)
  private static final Map<UUID, String> playerTeams = new HashMap<>();

  // Joueurs en train de recharger
  private static final Set<UUID> recharging = new HashSet<>();

  @Getter @Setter
  public static int brushValue = 1;

  @Getter @Setter
  public static int rechargeValue = 10;

  // Mapping équipe → couleur (pour blocs)
  private final Map<String, Material> teamConcrete = Map.of(
      "rouge", Material.RED_CONCRETE,
      "bleu_marine", Material.BLUE_CONCRETE,
      "vert_profond", Material.GREEN_CONCRETE,
      "jaune", Material.YELLOW_CONCRETE
  );

  public static final Map<String, Material> teamWool = Map.of(
      "rouge", Material.RED_WOOL,
      "bleu_marine", Material.BLUE_WOOL,
      "vert_profond", Material.GREEN_WOOL,
      "jaune", Material.YELLOW_WOOL
  );

  public static final Map<String, ChatColor> teamColorMap = Map.of(
      "rouge", ChatColor.RED,
      "bleu_marine", ChatColor.BLUE,
      "vert_profond", ChatColor.GREEN,
      "jaune", ChatColor.YELLOW
  );


  // Plugin instance (à passer depuis ta classe principale)
  private final JavaPlugin plugin;

  public SplatoonManager(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  /* ---------------------------------------------------------- */
  /* --------------------- MÉTHODES JEU ------------------------ */
  /* ---------------------------------------------------------- */

  public static void startEpreuve() {

    SplatoonManager.initializeTeamsFromScoreboard();
    initZones();
    // Initialiser peinture si nécessaire
    for (UUID playerId : playerTeams.keySet()) {
      paintLeft.putIfAbsent(playerId, rechargeValue);
    }

    // Donner les pinceaux et notifier
    for (UUID playerId : playerTeams.keySet()) {
      Player p = Bukkit.getPlayer(playerId);
      if (p != null && p.isOnline()) {
        String team = playerTeams.get(playerId);
        p.getInventory().addItem(createBrush(team));
        p.sendMessage(ChatColor.AQUA + "L'épreuve Splatoon commence !");
      }
    }

    Bukkit.broadcastMessage(ChatColor.GREEN + "La partie Splatoon est maintenant en cours !");
  }



  public static void clearGame() {
    for (String team : playerTeams.values()) {
      // Retirer les pinceaux
      for (Player p : Bukkit.getOnlinePlayers()) {
        if (playerTeams.get(p.getUniqueId()) != null && playerTeams.get(p.getUniqueId()).equalsIgnoreCase(team)) {
          removeBrush(p);
        }
      }
    }
    paintLeft.clear();
    playerTeams.clear();
    recharging.clear();
    clearWoolZones();
  }


  public static void initializeTeamsFromScoreboard() {
    for (Player p : Bukkit.getOnlinePlayers()) {
      Team team = Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(p.getName());
      if (team != null && (team.getName().equalsIgnoreCase("rouge") ||
          team.getName().equalsIgnoreCase("bleu_marine") ||
          team.getName().equalsIgnoreCase("vert_profond") ||
          team.getName().equalsIgnoreCase("jaune"))){
          assignTeam(p, team.getName()); // Assure-toi que les noms match "rouge", "bleu_marine", etc.
      }
    }
  }


  public static void assignTeam(Player p, String teamName) {
    playerTeams.put(p.getUniqueId(), teamName.toLowerCase());
    paintLeft.put(p.getUniqueId(), rechargeValue);
  }
  private static Set<Block> findConnectedConcrete(Block start) {
    Set<Block> result = new HashSet<>();
    Queue<Block> toCheck = new LinkedList<>();
    Material baseType = start.getType();

    toCheck.add(start);
    result.add(start);

    while (!toCheck.isEmpty()) {
      Block b = toCheck.poll();
      for (Block n : getAdjacentBlocks(b)) {
        if (!result.contains(n) && n.getType() == baseType) {
          result.add(n);
          toCheck.add(n);
        }
      }
    }
    return result;
  }


  private static List<Block> getAdjacentBlocks(Block b) {
    return List.of(
        b.getRelative(1, 0, 0),
        b.getRelative(-1, 0, 0),
        b.getRelative(0, 0, 1),
        b.getRelative(0, 0, -1),
        b.getRelative(0, -1, 0),
        b.getRelative(0, 1, 0)
    );
  }

  private void startClaim(ClaimedZone zone, String team, Player player) {

    if (!zone.getPlayersInZone().containsKey(team) ||
        !zone.getPlayersInZone().get(team).contains(player.getUniqueId())) {
      // Enregistrer le joueur dans la zone
      zone.addPlayer(team, player.getUniqueId());
    }

    if (zone.getPlayersInZone().size() == 1 && zone.getCurrentOwner() != null && zone.getCurrentOwner().equalsIgnoreCase(team)) {
      return; // la zone appartient déjà à cette équipe
    }

    if (zone.getTask() != null) return; // tâche déjà en cours

    // --- TÂCHE DE PROGRESSION ---
    BukkitRunnable claimTask = new BukkitRunnable() {
      @Override
      public void run() {

        // Si plus aucun joueur dans la zone
        if (zone.isEmpty() || !SplatoonCommand.isGameRunning()) {
          zone.resetProgress();
          zone.setClaimingTeam(null);
          zone.setTask(null);
          cancel();
          return; // ne cancel pas, on attend le retour des joueurs
        }
        // --- Calcul des forces en présence ---
        Map<String, Integer> countByTeam = new HashMap<>();
        for (Map.Entry<String, Set<UUID>> entry : zone.getPlayersInZone().entrySet()) {
          countByTeam.put(entry.getKey(), entry.getValue().size());
        }

        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(countByTeam.entrySet());
        sorted.sort((a, b) -> b.getValue() - a.getValue());

        if (sorted.isEmpty()) return;

        // Vérification de la domination
        String leadingTeam = sorted.get(0).getKey();
        int top = sorted.get(0).getValue();
        int totalPlayers = countByTeam.values().stream().mapToInt(Integer::intValue).sum();

        // Cas égalité
        if (sorted.size() > 1) {
          int second = sorted.get(1).getValue();
          if (top == second) {
            zone.setClaimingTeam(null);
            return; // pause progression en cas d'égalité
          }
        }

        // --- Calcul de l'incrément ---
        double increment;
        if (sorted.size() > 1) {
          // Plus d'une équipe, top domine
          int second = sorted.size() > 1 ? sorted.get(1).getValue() : 0;
          double ratio = (double) top / totalPlayers;

          double speedFactor = 10;
          if (ratio > 0.5) speedFactor = Math.max(1, speedFactor - top + second);

          increment = 100.0 / (speedFactor * 20.0);
        } else {
          // Une seule équipe
          if (leadingTeam.equalsIgnoreCase(zone.getCurrentOwner())) {
            zone.setClaimingTeam(null);
            zone.setTask(null);
            zone.resetProgress();
            cancel();
            return; // déjà été claim par cette équipe
          }
          int n = sorted.get(0).getValue();
          double timeSeconds = Math.max(1, 5 - (n - 1));
          increment = 100.0 / (timeSeconds * 20.0);
        }


        // Cas la team leader est différente de l'équipe en train de claimer
        if (!leadingTeam.equals(zone.getClaimingTeam())) {
          if (!leadingTeam.equalsIgnoreCase(zone.getCurrentOwner())) {
            zone.setClaimingTeam(leadingTeam);
          }
        }

        // Cas Team leader est la seule équipe et propriétaire actuel
        if (leadingTeam.equalsIgnoreCase(zone.getCurrentOwner()) && sorted.size() == 1) {
          zone.setClaimingTeam(null);
          zone.setTask(null);
          zone.resetProgress();
          cancel();
          return; // déjà été claim par cette équipe
        }
        // Cas Team leader est la propriétaire actuel mais d'autres équipes présentes
        else if (leadingTeam.equalsIgnoreCase(zone.getCurrentOwner()) && sorted.size() > 1) {
          zone.setClaimingTeam(null);
          return; // pause progression
        }
        zone.setProgress(zone.getProgress() + increment);

        // Zone capturée
        if (zone.getProgress() >= 100) {
          zone.setTask(null);
          claimZone(zone, leadingTeam);
          // Supprimer tous les joueurs de la zone
          for (String team : new HashSet<>(zone.getPlayersInZone().keySet())) { // copie des clés
            Set<UUID> playerSet = new HashSet<>(zone.getPlayersInZone().get(team)); // copie des UUID
            for (UUID playerId : playerSet) {
              zone.removePlayer(team, playerId);
            }
          }
          cancel();
        }
      }
    };

    // --- TÂCHE D’AFFICHAGE ---
    BukkitRunnable barTask = new BukkitRunnable() {
      @Override
      public void run() {
        if (!SplatoonCommand.isGameRunning()) return;
        if (zone.getTask() == null || !SplatoonCommand.isGameRunning()) {
          cancel();
          return;
        }
        String claimingTeam = zone.getClaimingTeam();
        ChatColor color = claimingTeam != null ? teamColorMap.get(claimingTeam) : ChatColor.GRAY;
        String bar = progressBar(zone.getProgress());

        for (Set<UUID> players : zone.getPlayersInZone().values()) {
          for (UUID id : players) {
            Player pl = Bukkit.getPlayer(id);
            if (pl != null && pl.isOnline()) {
              pl.sendActionBar(color + "Zone en cours de capture " +
                  color + bar +
                  ChatColor.WHITE + " " + String.format("%.0f", zone.getProgress()) + "%");
            }
          }
        }

        // Stoppe l'affichage si la tâche claim est annulée
        if (zone.getTask() == null) cancel();
      }
    };

    // Lancer les tâches
    barTask.runTaskTimer(plugin, 0L, 10L); // 0.5s
    claimTask.runTaskTimer(plugin, 0L, 1L);

    zone.setTask(claimTask);
  }



  public static void initZones() {
    // Récupère le monde
    World world = Bukkit.getWorld("worlds/squidgame/splatoon");
    if (world == null) {
      Bukkit.getLogger().warning("Le monde n'existe pas !");
      return;
    }

    // Définition des coins du cuboid
    int x1 = 15, y1 = 65, z1 = -28;
    int x2 = 6,  y2 = 70, z2 = -36;

    int minX = Math.min(x1, x2);
    int maxX = Math.max(x1, x2);
    int minY = Math.min(y1, y2);
    int maxY = Math.max(y1, y2);
    int minZ = Math.min(z1, z2);
    int maxZ = Math.max(z1, z2);

    // Parcours du cuboid
    for (int x = minX; x <= maxX; x++) {
      for (int y = minY; y <= maxY; y++) {
        for (int z = minZ; z <= maxZ; z++) {
          Block block = world.getBlockAt(x, y, z);
          if (block.getType() == Material.WHITE_CONCRETE) {
            // Trouve tous les blocs connectés
            Set<Block> connected = findConnectedConcrete(block);

            // Crée une zone si nécessaire
            ClaimedZone zone = new ClaimedZone(connected);
            allZones.add(zone);
            for (Block b : connected) {
              blockToZone.put(b, zone);
            }
          }
        }
      }
    }

    Bukkit.getLogger().info("Initialisation des zones terminée : " + allZones.size() + " zones créées.");
  }


  private void claimZone(ClaimedZone zone, String team) {
    Material teamConcreteColor = teamConcrete.get(team.toLowerCase());
    if (teamConcreteColor == null) return;

    for (Block b : zone.getBlocks()) {
      b.setType(teamConcreteColor);
    }

    zone.setCurrentOwner(team);
    zone.resetProgress();
    zone.setClaimingTeam(null);

    Bukkit.broadcastMessage("§7Zone claimée par l’équipe " + teamColorMap.get(team) + team + "§7 !");
    zone.getBlocks().iterator().next().getWorld().playSound(
        zone.getBlocks().iterator().next().getLocation(),
        org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.4f, 1.9f
    );
    String claimingTeam = zone.getClaimingTeam();
    ChatColor color = claimingTeam != null ? teamColorMap.get(claimingTeam) : ChatColor.GRAY;
    String bar = progressBar(100.0);

    for (Set<UUID> players : zone.getPlayersInZone().values()) {
      for (UUID id : players) {
        Player pl = Bukkit.getPlayer(id);
        if (pl != null && pl.isOnline()) {
          pl.sendActionBar(color + "Zone capturée " +
              color + bar +
              ChatColor.WHITE + " 100%");
        }
      }
    }

    // === Effet de particules quand la zone est claim ===
    AtomicInteger ticks = new AtomicInteger(0);

    Bukkit.getScheduler().runTaskTimer(plugin, task -> {
      if (ticks.get() > 50) {
        task.cancel();
        return;
      }

      for (Block block : zone.getBlocks()) {
        Location loc = block.getLocation().clone().add(0.5, 1.0, 0.5); // un peu au-dessus du bloc
        block.getWorld().spawnParticle(
            org.bukkit.Particle.END_ROD,
            loc,
            2,      // nombre de particules
            0.0, 0.0, 0.0, // écart
            0.25    // vitesse
        );
      }

      ticks.addAndGet(10);
    }, 0L, 10L);


  }

  public static void clearAllZones() {
    for (ClaimedZone zone : allZones) {
      zone.resetProgress();
      zone.setClaimingTeam(null);
      zone.setCurrentOwner(null);

      // On remet les blocs en blanc, pour repartir à zéro
      for (Block b : zone.getBlocks()) {
        if (b.getType().name().endsWith("_CONCRETE")) {
          b.setType(Material.WHITE_CONCRETE);
        }
      }
    }

    blockToZone.clear();
    allZones.clear();

    Bukkit.broadcastMessage(ChatColor.GRAY + "[Splatoon] Toutes les zones ont été réinitialisées.");
  }


  private String progressBar(double percent) {
    int total = 20;
    int filled = (int) Math.round(percent / (100.0 / total));
    return "§a" + "█".repeat(filled) + "§7" + "█".repeat(total - filled);
  }


  /* ---------------------------------------------------------- */
  /* ---------------------- ÉVÉNEMENTS ------------------------- */
  /* ---------------------------------------------------------- */

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent e) {
    Player p = e.getPlayer();

    // 🔴 Si le jeu est en pause/STOP → message + annulation
    if (SplatoonCommand.getSplatoonState() == SplatoonState.STOP) {
      p.sendActionBar(ChatColor.RED + "Le jeu est en pause !");
      e.setCancelled(true);
      return;
    }
    if (!SplatoonCommand.isGameRunning()) return;
    if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
    if (e.getClickedBlock() == null) return;
    if (e.getHand() != EquipmentSlot.HAND) return;
    if (!playerTeams.containsKey(p.getUniqueId())) return;


    Block block = e.getClickedBlock();
    String team = playerTeams.get(p.getUniqueId());

    // Vérifier que le joueur tient bien un pinceau
    if (!isBrush(p.getInventory().getItemInMainHand(), team)) {
      p.sendActionBar(ChatColor.GRAY + "Tu dois utiliser ton pinceau pour peindre !");
      return;
    }

    if (recharging.contains(p.getUniqueId())) {
      p.sendActionBar(ChatColor.GRAY + "Tu es en train de recharger ton pinceau...");
      return;
    }

    int left = paintLeft.getOrDefault(p.getUniqueId(), 0);
    if (left <= 0) {
      p.sendActionBar(ChatColor.RED + "Tu n’as plus de peinture !");
      return;
    }

    // Peindre uniquement la laine
    if (!block.getType().name().contains("WOOL")) return;

    // Récupère la laine de l'équipe du joueur
    Material teamWoolColor = teamWool.get(team.toLowerCase());
    int brushValue = getBrushValue();

    // === Nouvelle boucle pour peindre en "zone sphérique" ===
    double radius = brushValue - 0.5; // rayon légèrement adouci
    for (int x = -brushValue + 1; x < brushValue; x++) {
      for (int y = -brushValue + 1; y < brushValue; y++) {
        for (int z = -brushValue + 1; z < brushValue; z++) {

          // Distance euclidienne au centre
          double distance = Math.sqrt(x * x + y * y + z * z);
          if (distance > radius) continue; // ignore les blocs en dehors du rayon

          Block target = block.getRelative(x, y, z);

          if (!target.getType().name().contains("WOOL")) continue;
          if (target.getType() == teamWoolColor) continue;
          if (target.getType() != Material.WHITE_WOOL && !isEnemyWool(target.getType(), team)) continue;
          boolean inZone = false;
          for (String zoneName : ZoneManager.getZones().keySet()) {
            if (ZoneManager.isInsideZone(zoneName, target.getLocation())) {
              inZone = true;
              break;
            }
          }

          if (!inZone) continue;

          target.setType(teamWoolColor);
          p.getWorld().playSound(target.getLocation(), Sound.BLOCK_SLIME_BLOCK_PLACE, 1.0f, 1.2f);
        }
      }
    }

    paintLeft.put(p.getUniqueId(), left - 1);
    p.sendActionBar(ChatColor.GRAY + "Peinture restante: " + ChatColor.WHITE + paintLeft.get(p.getUniqueId()) + "/" + rechargeValue);

    if (paintLeft.get(p.getUniqueId()) <= 0) {
      p.sendActionBar(ChatColor.YELLOW + "Retourne à ta base pour recharger ton pinceau !");
      p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.4f, 1.9f);
    }
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent e) {
    Player p = e.getPlayer();

    // 🔴 Si le jeu est en pause/STOP → message + annulation
    if (SplatoonCommand.getSplatoonState() == SplatoonState.STOP) {
      p.sendActionBar(ChatColor.RED + "Le jeu est en pause !");
      return;
    }
    if (!SplatoonCommand.isGameRunning()) return;
    if (!playerTeams.containsKey(p.getUniqueId())) return;

    Block blockUnder = p.getLocation().subtract(0, 1, 0).getBlock();
    Material type = blockUnder.getType();
    String team = playerTeams.get(p.getUniqueId());

    // Si le joueur est sur SA couleur de base → recharge
    if (teamConcrete.get(team.toLowerCase()).equals(type)) {
      if (paintLeft.get(p.getUniqueId()) < rechargeValue && !recharging.contains(p.getUniqueId())) {
        startRecharge(p);

      }
    }

    // Si le joueur est sur une laine adverse → Slowness I
    else if (isEnemyWool(type, team)) {
      p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1, 1, false, false));
    }

    // ---------- SYSTEME DE CLAIM ----------
    Block blockUnderFeet = p.getLocation().subtract(0, 1, 0).getBlock();
    boolean inZone = false;
    for (String zoneName : ZoneManager.getZones().keySet()) {
      if (ZoneManager.isInsideZone(zoneName, blockUnderFeet.getLocation())) {
        inZone = true;
        break;
      }
    }

    if (!inZone) return;
    if (blockUnderFeet.getType() == Material.WHITE_CONCRETE
        || blockUnderFeet.getType().name().endsWith("_CONCRETE")) {
      ClaimedZone zone = blockToZone.get(blockUnderFeet);
      // Si la zone n'existe pas encore -> on la crée
      if (zone == null) {
        Set<Block> connected = findConnectedConcrete(blockUnderFeet);
        zone = new ClaimedZone(connected);
        allZones.add(zone);
        for (Block b : connected) blockToZone.put(b, zone);
      }

      // Démarrer ou relancer le claim
      startClaim(zone, team, p);
    }
    else {
      // Nettoyer les zones où le joueur n’est plus
      for (ClaimedZone z : new HashSet<>(allZones)) {
        if (z.getPlayersInZone().values().stream().anyMatch(set -> set.contains(p.getUniqueId()))) {
          z.removePlayer(team , p.getUniqueId());
        }
      }
    }
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent e) {
    Player p = e.getEntity();
    UUID id = p.getUniqueId();

    // Retirer le joueur de sa team Splatoon
    if (playerTeams.containsKey(id)) {
      String team = playerTeams.get(id);
      playerTeams.remove(id);
      paintLeft.remove(id);
      recharging.remove(id);

      // Retirer le joueur des zones où il est présent
      for (ClaimedZone zone : new HashSet<>(allZones)) {
        if (zone.getPlayersInZone().containsKey(team)) {
          zone.getPlayersInZone().get(team).remove(id);
        }
      }

      Bukkit.getLogger().info("[Splatoon] " + p.getName() + " a été retiré de l’équipe " + team + " après sa mort.");
    }
  }

  @EventHandler
  public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent e) {
    Player p = e.getPlayer();

    // Vérifie s'il a une équipe sur le scoreboard
    Team team = Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(p.getName());
    if (team != null && (team.getName().equalsIgnoreCase("rouge")
        || team.getName().equalsIgnoreCase("bleu_marine")
        || team.getName().equalsIgnoreCase("vert_profond")
        || team.getName().equalsIgnoreCase("jaune"))) {
      assignTeam(p, team.getName());
      Bukkit.getLogger().info("[Splatoon] " + p.getName() + " ajouté automatiquement à l’équipe " + team.getName());
    }
  }

  /* ---------------------------------------------------------- */
  /* ---------------------- OUTILS ----------------------------- */
  /* ---------------------------------------------------------- */

  private void startRecharge(Player p) {
    recharging.add(p.getUniqueId());
    p.sendActionBar(ChatColor.GREEN + "Recharge de ton pinceau en cours (3s)...");

    new BukkitRunnable() {
      int ticks = 0; // compteur de répétitions
      final int totalTicks = 6; // 6 répétitions = 3 secondes (10 ticks = 0.5s)

      @Override
      public void run() {
        if (ticks >= totalTicks) {
          // Fin de recharge
          recharging.remove(p.getUniqueId());
          paintLeft.put(p.getUniqueId(), rechargeValue);
          p.sendActionBar(ChatColor.GREEN + "Ton pinceau est rechargé !");
          p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
          cancel();
          return;
        }

        // Son de recharge dynamique
        // On utilise BLOCK_NOTE_BLOCK_HAT et on augmente progressivement le pitch
        float pitch = 0.8f + 0.07f * ticks; // pitch monte à chaque répétition
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.5f, pitch);

        ticks++;
      }
    }.runTaskTimer(plugin, 0L, 10L); // 10 ticks = 0.5 seconde
  }


  private boolean isEnemyWool(Material type, String team) {
    for (Map.Entry<String, Material> entry : teamWool.entrySet()) {
      if (!entry.getKey().equals(team) && entry.getValue() == type) {
        return true;
      }
    }
    return false;
  }

  private boolean isBrush(ItemStack item, String team) {
    if (item == null || !item.hasItemMeta()) return false;
    ItemMeta meta = item.getItemMeta();
    if (meta == null || !meta.hasDisplayName()) return false;

    ChatColor color = teamColorMap.get(team.toLowerCase());
    if (color == null) return false;

    return meta.getDisplayName().equals(color + " Pinceau " + team.toLowerCase());
  }


  /* ---------------------------------------------------------- */
  /*  Méthode utilitaire si tu veux donner le pinceau aux joueurs  */
  /* ---------------------------------------------------------- */
  public static ItemStack createBrush(String team) {
    ItemStack brush = new ItemStack(Material.BRUSH);
    ItemMeta meta = brush.getItemMeta();
    if (meta == null) return brush;

    ChatColor color = teamColorMap.get(team.toLowerCase());
    if (color == null) color = ChatColor.WHITE;

    meta.setDisplayName(color + " Pinceau " + team.toLowerCase());
    meta.setUnbreakable(true);
    brush.setItemMeta(meta);
    return brush;
  }

  public static ItemStack removeBrush(Player p) {
    ItemStack brush = null;
    for (ItemStack item : p.getInventory().getContents()) {
      if (item != null && item.getType() == Material.BRUSH) {
        brush = item;
        p.getInventory().remove(item);
        break;
      }
    }
    return brush;
  }

}
