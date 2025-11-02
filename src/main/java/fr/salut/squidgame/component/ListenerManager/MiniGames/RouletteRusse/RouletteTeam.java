package fr.salut.squidgame.component.ListenerManager.MiniGames.RouletteRusse;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class RouletteTeam {

  private final String name;
  @Getter
  public final List<Player> players = new ArrayList<>();
  private final Map<UUID, Integer> order = new HashMap<>();
  private final Map<UUID, Boolean> lastTurnPassed = new HashMap<>();
  private final Map<UUID, Boolean> hasUsedAnvil = new HashMap<>();
  private final Map<UUID, Boolean> hasCheckedAmmo = new HashMap<>();



  private static int currentIndex = 0;
  private static boolean forcedDoubleShot = false;
  private static boolean mustShootNext = false;
  private static int roundsPlayed = 0;
  public static int MAX_DEATHS = 3;
  private int deathCount = 0;

  private final Map<UUID, Integer> playerAmmo = new HashMap<>();
  private final int MAX_AMMO = 6; // nombre de tirs avant que le joueur meure

  public RouletteTeam(String name) {
    this.name = name;
  }

  // ---- CONFIGURATION ----
  public void addPlayer(Player player) {
    if (players.size() >= 5) {
      player.sendMessage(ChatColor.RED + "L’équipe " + name + " est déjà pleine !");
      return;
    }
    players.add(player);
    order.put(player.getUniqueId(), players.size());
    playerAmmo.put(player.getUniqueId(), new Random().nextInt(MAX_AMMO) + 1); // tire aléatoire entre 1 et MAX_AMMO
    //Bukkit.getLogger().info(ChatColor.AQUA + player.getName() + " a rejoint l’équipe " + ChatColor.BOLD + name);
    hasUsedAnvil.put(player.getUniqueId(), false);

  }


  public void removePlayer(Player player) {
    players.remove(player);
    order.remove(player.getUniqueId());
    lastTurnPassed.remove(player.getUniqueId());
    hasCheckedAmmo.put(player.getUniqueId(), false);
  }

  // ---- GESTION DU TOUR ----
  public void resetForNewGame() {
    currentIndex = 0;
    forcedDoubleShot = false;
    mustShootNext = false;
    roundsPlayed = 0;
    lastTurnPassed.clear();

    Collections.shuffle(players);
  }

  private void stopTeamGame() {
    for (Player p : players) {
      p.sendTitle(ChatColor.RED + "Fin de la partie !",
           "§7" + deathCount + " joueurs ont été éliminés !",
          10, 60, 10);
    }
    for (Player player : Bukkit.getOnlinePlayers()) {
      player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.8f, 1.0f);
      player.sendMessage(ChatColor.DARK_RED + "L’équipe " + ChatColor.BOLD + name +
          ChatColor.DARK_RED + " a terminé le jeu !");
    }
    players.clear();
  }


  public void startTurn() {
    if (players.isEmpty()) return;
    Player current = players.get(currentIndex);
    for (Player player : players) {
      player.sendMessage("§6C’est au tour de §l" + current.getName());
    }
    giveTurnItems(current);
  }

  public static void giveTurnItems(Player player) {
    PlayerInventory inv = player.getInventory();

    // Gun spéciale pour tirer
    ItemStack crossbow = new ItemStack(Material.CROSSBOW, 1);
    ItemMeta crossbowMeta = crossbow.getItemMeta();
    crossbowMeta.setDisplayName(ChatColor.RED + "Tir");
    crossbow.setItemMeta(crossbowMeta);

    // Papier spécial pour passer le tour
    ItemStack paper = new ItemStack(Material.PAPER, 1);
    ItemMeta paperMeta = paper.getItemMeta();
    paperMeta.setDisplayName(ChatColor.YELLOW + "Passer le tour");
    paper.setItemMeta(paperMeta);

    // Enclume spéciale pour reset le barillet
    ItemStack anvil = new ItemStack(Material.ARROW, 1);
    ItemMeta anvilMeta = anvil.getItemMeta();
    anvilMeta.setDisplayName(ChatColor.GRAY + "Réinitialiser le barillet");
    anvil.setItemMeta(anvilMeta);

    // Oeil de l'end spécial pour regarder le barillet
    ItemStack enderEye = new ItemStack(Material.ENDER_EYE, 1);
    ItemMeta enderEyeMeta = enderEye.getItemMeta();
    enderEyeMeta.setDisplayName(ChatColor.BLUE + "Regarder dans le barillet");
    enderEye.setItemMeta(enderEyeMeta);


    // Ajoute les items à l’inventaire
    inv.addItem(crossbow, paper, anvil, enderEye);

    player.sendMessage(ChatColor.GREEN + "C’est ton tour !");
  }

  public static void removeTurnItems(Player player) {
    if (player == null) return;
    PlayerInventory inv = player.getInventory();

    for (int i = 0; i < inv.getSize(); i++) {
      ItemStack item = inv.getItem(i);
      if (item == null) continue;
      ItemMeta meta = item.getItemMeta();
      if (meta == null) continue;

      String name = meta.getDisplayName();
      // Retire uniquement les items du tour
      if (name.equals(ChatColor.RED + "Tir") ||
          name.equals(ChatColor.YELLOW + "Passer le tour") ||
          name.equals(ChatColor.GRAY + "Réinitialiser le barillet") ||
          name.equals(ChatColor.BLUE + "Regarder dans le barillet")) {
        inv.setItem(i, null);
      }
    }
  }


  // ---- ACTIONS ----
  public void handleCheckAmmo(Player player) {
    if (!isCurrentPlayer(player)) {
      player.sendMessage(ChatColor.RED + "Ce n’est pas ton tour !");
      return;
    }

    UUID uuid = player.getUniqueId();

    // Vérifie s’il a déjà utilisé cette option
    if (hasCheckedAmmo.getOrDefault(uuid, false)) {
      player.sendMessage(ChatColor.RED + "Tu as déjà regardé dans ton barillet !");
      player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
      return;
    }

    int ammoLeft = playerAmmo.getOrDefault(uuid, MAX_AMMO);

    // Envoi d’un message discret
    player.sendMessage(ChatColor.GRAY + "§7Tu observes ton barillet... §8(§f" + ammoLeft + "§7 tours avant la mort§8)");
    player.playSound(player.getLocation(), Sound.ITEM_SPYGLASS_USE, 1.0f, 1.0f);

    // Marque l’utilisation
    hasCheckedAmmo.put(uuid, true);
  }


  public void handleReload(Player player) {
    if (!isCurrentPlayer(player)) {
      player.sendMessage(ChatColor.RED + "Ce n’est pas ton tour !");
      return;
    }

    UUID uuid = player.getUniqueId();

    // Vérifie s’il a déjà utilisé son enclume
    if (hasUsedAnvil.get(uuid)) {
      player.sendMessage(ChatColor.RED + "§cTu as déjà fait tourner ton barillet !");
      player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
      return;
    }

    // Réinitialise le barillet
    int newAmmo = new Random().nextInt(MAX_AMMO) + 1;
    playerAmmo.put(uuid, newAmmo);

    player.sendMessage(ChatColor.GRAY + "§7Tu fais tourner ton barillet...");
    //player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.2f);
    for (Player playerInTeam : players) {
      playerInTeam.playSound(player.getLocation(), Sound.ITEM_SPYGLASS_USE, 1.0f, 1.0f);
      playerInTeam.sendMessage("§5" + player.getName() + "§d a fait tourner son barillet !");
    }

    hasUsedAnvil.put(uuid, true);
  }


  public void handlePass(Player player) {
    if (!isCurrentPlayer(player)) {
      player.sendMessage(ChatColor.RED + "Ce n’est pas ton tour !");
      return;
    }

    if (Boolean.TRUE.equals(lastTurnPassed.get(player.getUniqueId()))) {
      player.sendMessage(ChatColor.RED + "Tu ne peux pas passer deux tours d’affilée !");
      return;
    }

    if (mustShootNext) {
      player.sendMessage(ChatColor.RED + "Tu dois tirer ce tour-ci, tu ne peux pas passer !");
      return;
    }

    lastTurnPassed.put(player.getUniqueId(), true);

    for (Player playerInTeam : players) {
      playerInTeam.sendMessage("§6" + player.getName() + "§e a choisi de passer son tour !");
      playerInTeam.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 0.5f);
      playerInTeam.sendTitle("§eC'est à §6" + players.get((currentIndex + 1 + players.size()) % players.size()).getName() + "§e de jouer !",
          "§aLe joueur §2" + players.get(currentIndex).getName() + "§a a passé son tour",
          10, 70, 20);
    }
    mustShootNext = true;
    nextPlayer(true);
  }

  public void handleShoot(Player player) {
    if (!isCurrentPlayer(player)) {
      player.sendMessage(ChatColor.RED + "Ce n’est pas ton tour !");
      return;
    }

    //Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " appuie sur la gâchette...");

    UUID uuid = player.getUniqueId();
    int ammoLeft = playerAmmo.getOrDefault(uuid, MAX_AMMO);

    ammoLeft--; // décrémente le compteur
    playerAmmo.put(uuid, ammoLeft);

    for (Player teammate : players) {
      teammate.sendMessage("§6" + player.getName() + "§e a choisi de tirer !");
      teammate.playSound(player.getLocation(),
          Sound.ENTITY_PLAYER_HURT,
          1.0f,
          0.5f);
    }

    if (ammoLeft <= 0) {
      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        //onlinePlayer.sendMessage(ChatColor.DARK_RED + player.getName() + " éliminé(e) !");
          onlinePlayer.playSound(player.getLocation(),
              Sound.ITEM_CROSSBOW_SHOOT,
              1.0f,
              1.0f);
      }
      player.setHealth(0.0); // élimine le joueur
      if (roundsPlayed > 0) {
        for (Player teammate : players) {
          teammate.sendTitle("§eC'est à §6" + players.get((currentIndex + 2 + players.size()) % players.size()).getName() + "§e de jouer !",
              "§aLe joueur §2" + players.get(currentIndex).getName() + "§a a été éliminé",
              10, 70, 20);
        }
      } else {
        for (Player teammate : players) {
          teammate.sendTitle("§eC'est à §6" + players.get((currentIndex + 1 + players.size()) % players.size()).getName() + "§e de jouer !",
              "§aLe joueur §2" + players.get(currentIndex).getName() + "§a a été éliminé",
              10, 70, 20);
        }
      }

      removeTurnItems(player);
      playerAmmo.remove(uuid);
      players.remove(player);

      deathCount++;

// ✅ Vérifier si le nombre de morts dépasse le seuil
      if (deathCount >= MAX_DEATHS || players.size() <= 1) {
        stopTeamGame();
        return;
      }

      // Reset les flags
      mustShootNext = false;
      forcedDoubleShot = false;
      if (roundsPlayed == 1) {
        roundsPlayed = 0;
        nextPlayer(false);
        return;
      }


      if (currentIndex >= players.size() || currentIndex == 0) {
        currentIndex = players.size()-1; // boucle sur le premier joueur restant
      }
      else {
        currentIndex = currentIndex - 1;
      }
      if (!players.isEmpty()) {
        nextPlayer(false);
      }

      return;
    }
    else if (mustShootNext && roundsPlayed == 1) { // Cas où le joueur doit tirer une 2ème fois
      for (Player playerInTeam : players) {
        playerInTeam.sendMessage("§2" + player.getName() + "§a a survécu au 1er tir obligatoire !");
        playerInTeam.sendTitle("§6" + players.get(currentIndex).getName() + "§e doit encore tirer !",
            "§aIl reste un tir",
            10, 70, 20);
      }

      //Bukkit.broadcastMessage("Il reste 1 tir obligatoire !");
      //Bukkit.broadcastMessage("Le joueur actuel est : " + players.get(currentIndex).getName());
      //Bukkit.broadcastMessage("Le joueur suivant est : " + players.get(currentIndex).getName());
      mustShootNext = true;
    }
    else if (mustShootNext && roundsPlayed == 2) { // Cas où le joueur a tiré 2 fois
      for (Player playerInTeam : players) {
        playerInTeam.sendTitle("§eC'est à §6" + players.get((currentIndex + 2 + players.size()) % players.size()).getName() + "§e de jouer !",
            "§aLe joueur §2" + players.get(currentIndex).getName() + "§a n'a pas été éliminé",
            10, 70, 20);
      }
    }
    else if (mustShootNext) { // Cas où le joueur ne peut pas passer
      for (Player playerInTeam : players) {
        playerInTeam.sendMessage("§2" + player.getName() + "§a a survécu au tir obligatoire !");
        playerInTeam.sendMessage("§dLe prochain tir sera un double tir !");
        playerInTeam.sendTitle("§6" + players.get((currentIndex - 1 + players.size()) % players.size()).getName() + "§e doit tirer 2 fois !",
            "§aLe joueur §2" + players.get(currentIndex).getName() + "§a n'a pas été éliminé",
            10, 70, 20);
      }

      //Bukkit.broadcastMessage("Le joueur actuel est : " + players.get(currentIndex).getName());
      forcedDoubleShot = true;
      currentIndex = (currentIndex - 1 + players.size()) % players.size();

      //Bukkit.broadcastMessage("Le joueur suivant est : " + players.get(currentIndex).getName());
      mustShootNext = false;
    } else {
      for (Player playerInTeam : players) {
        playerInTeam.sendTitle("§eC'est à §6" + players.get((currentIndex + 1 + players.size()) % players.size()).getName() + "§e de jouer !",
            "§aLe joueur §2" + players.get(currentIndex).getName() + "§a n'a pas été éliminé",
            10, 70, 20);
      }
    }

    //Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " a survécu ! Il reste " + ammoLeft + " tir(s) avant la mort.");
    lastTurnPassed.put(uuid, false);

    nextPlayer(false);
  }


  public void nextPlayer(boolean fromPass) {
    if (players.isEmpty()) return;

    // Joueur qui vient de jouer
    Player previousPlayer = null;

    if (currentIndex < players.size()  && forcedDoubleShot && roundsPlayed > 0) {
      previousPlayer = players.get(currentIndex);
    } else if (currentIndex < players.size()  && forcedDoubleShot) {
      int previousIndex = (currentIndex + 1) % players.size();
      previousPlayer = players.get(previousIndex);
    } else if (currentIndex < players.size()) {
      previousPlayer = players.get(currentIndex);
    }

    // Si le joueur précédent est encore dans la liste, on retire ses items
    if (previousPlayer != null && players.contains(previousPlayer)) {
      removeTurnItems(previousPlayer);
    }


    // Détermine le prochain joueur
    if (fromPass) { // Cas où le joueur a passé son tour
      forcedDoubleShot = false;
      currentIndex = (currentIndex + 1) % players.size();
    } else if (forcedDoubleShot || roundsPlayed == 1){ // Cas où le joueur doit tirer deux fois de suite
      forcedDoubleShot = false;
      mustShootNext = true;
      roundsPlayed++;
    } else if (roundsPlayed == 2) { // Cas où le joueur qui a tiré deux fois de suite a fini son tour
      currentIndex = (currentIndex + 2) % players.size();
      roundsPlayed = 0;
      mustShootNext = false;
    } else { // Cas normal
      currentIndex = (currentIndex + 1) % players.size();
      mustShootNext = false;
    }

    // Donne le tour au joueur suivant
    Player nextPlayer = players.get(currentIndex);
    for (Player player : players) {
      player.sendMessage("§eC’est le tour de §6" + nextPlayer.getName());
    }

    giveTurnItems(nextPlayer);

  }



  public boolean isCurrentPlayer(Player player) {
    return players.get(currentIndex).equals(player);
  }
}
