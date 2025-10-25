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

public class SplatoonManager implements Listener {

  // Quantité de peinture restante pour chaque joueur
  private static final Map<UUID, Integer> paintLeft = new HashMap<>();

  // Association joueur → équipe (4 équipes déjà existantes)
  private static final Map<UUID, String> playerTeams = new HashMap<>();

  // Joueurs en train de recharger
  private static final Set<UUID> recharging = new HashSet<>();

  // Mapping équipe → couleur (pour blocs)
  private final Map<String, Material> teamConcrete = Map.of(
      "rouge", Material.RED_CONCRETE,
      "bleu", Material.BLUE_CONCRETE,
      "vert", Material.GREEN_CONCRETE,
      "jaune", Material.YELLOW_CONCRETE
  );

  private final Map<String, Material> teamWool = Map.of(
      "rouge", Material.RED_WOOL,
      "bleu", Material.BLUE_WOOL,
      "vert", Material.GREEN_WOOL,
      "jaune", Material.YELLOW_WOOL
  );

  private static final Map<String, ChatColor> teamColorMap = Map.of(
      "rouge", ChatColor.RED,
      "bleu", ChatColor.BLUE,
      "vert", ChatColor.GREEN,
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

    // Initialiser peinture si nécessaire
    for (UUID playerId : playerTeams.keySet()) {
      paintLeft.putIfAbsent(playerId, 10);
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
    paintLeft.clear();
    playerTeams.clear();
    recharging.clear();
  }


  public static void initializeTeamsFromScoreboard() {
    for (Player p : Bukkit.getOnlinePlayers()) {
      Team team = Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(p.getName());
      if (team != null && (team.getName().equalsIgnoreCase("rouge") ||
          team.getName().equalsIgnoreCase("bleu") ||
          team.getName().equalsIgnoreCase("vert") ||
          team.getName().equalsIgnoreCase("jaune"))){
          assignTeam(p, team.getName()); // Assure-toi que les noms match "rouge", "bleu", etc.
      }
    }
  }


  public static void assignTeam(Player p, String teamName) {
    playerTeams.put(p.getUniqueId(), teamName.toLowerCase());
    paintLeft.put(p.getUniqueId(), 10);
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

    // Si c’est déjà ta couleur → rien faire
    if (block.getType() == teamWoolColor) {
      p.sendActionBar(ChatColor.GRAY + "Ce bloc est déjà de ta couleur !");
      return;
    }

    // Si ce n’est ni une laine blanche, ni une laine ennemie → ignorer
    if (block.getType() != Material.WHITE_WOOL && !isEnemyWool(block.getType(), team)) {
      p.sendActionBar(ChatColor.GRAY + "Tu ne peux peindre que sur une laine blanche ou ennemie !");
      return;
    }

    // Sinon → peindre
    Material newColor = teamWoolColor;
    block.setType(newColor);
    p.getWorld().playSound(block.getLocation(), Sound.BLOCK_SLIME_BLOCK_PLACE, 1.0f, 1.2f);

    paintLeft.put(p.getUniqueId(), left - 1);
    p.sendActionBar(ChatColor.GRAY + "Peinture restante: " + ChatColor.WHITE + paintLeft.get(p.getUniqueId()) + "/10");

    if (paintLeft.get(p.getUniqueId()) <= 0) {
      p.sendActionBar(ChatColor.YELLOW + "Retourne à ta base pour recharger ton pinceau !");
      p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.9f);
    }
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent e) {
    Player p = e.getPlayer();

    // 🔴 Si le jeu est en pause/STOP → message + annulation
    if (SplatoonCommand.getSplatoonState() == SplatoonState.STOP) {
      p.sendActionBar(ChatColor.RED + "Le jeu est en pause !");
      e.setCancelled(true);
      return;
    }
    if (!SplatoonCommand.isGameRunning()) return;
    if (!playerTeams.containsKey(p.getUniqueId())) return;

    Block blockUnder = p.getLocation().subtract(0, 1, 0).getBlock();
    Material type = blockUnder.getType();
    String team = playerTeams.get(p.getUniqueId());

    // Si le joueur est sur SA couleur de base → recharge
    if (teamConcrete.get(team.toLowerCase()).equals(type)) {
      if (paintLeft.get(p.getUniqueId()) < 10 && !recharging.contains(p.getUniqueId())) {
        startRecharge(p);

      }
    }

    // Si le joueur est sur une laine adverse → Slowness I
    else if (isEnemyWool(type, team)) {
      p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1, 0, false, false));
    }
  }

  /* ---------------------------------------------------------- */
  /* ---------------------- OUTILS ----------------------------- */
  /* ---------------------------------------------------------- */

  private void startRecharge1(Player p) {
    recharging.add(p.getUniqueId());
    p.sendActionBar(ChatColor.GREEN + "Recharge de ton pinceau en cours (3s)...");

    new BukkitRunnable() {
      @Override
      public void run() {
        recharging.remove(p.getUniqueId());
        paintLeft.put(p.getUniqueId(), 10);
        p.sendActionBar(ChatColor.GREEN + "Ton pinceau est rechargé !");
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
      }
    }.runTaskLater(plugin, 60L); // 3 secondes
  }

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
          paintLeft.put(p.getUniqueId(), 10);
          p.sendActionBar(ChatColor.GREEN + "Ton pinceau est rechargé !");
          p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f); // son final
          cancel();
          return;
        }

        // Son de recharge dynamique
        // On utilise BLOCK_NOTE_BLOCK_HAT et on augmente progressivement le pitch
        float pitch = 0.8f + 0.07f * ticks; // pitch monte à chaque répétition
        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.5f, pitch);

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

}
