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
  private final List<Player> players = new ArrayList<>();
  private final Map<UUID, Integer> order = new HashMap<>();
  private final Map<UUID, Boolean> lastTurnPassed = new HashMap<>();
  private final Map<UUID, Boolean> hasUsedAnvil = new HashMap<>();


  private int currentIndex = 0;
  private boolean forcedDoubleShot = false;
  private boolean mustShootNext = false;
  private int roundsPlayed = 0;

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
    Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " a rejoint l’équipe " + ChatColor.BOLD + name);
    hasUsedAnvil.put(player.getUniqueId(), false);

  }


  public void removePlayer(Player player) {
    players.remove(player);
    order.remove(player.getUniqueId());
    lastTurnPassed.remove(player.getUniqueId());
  }

  // ---- GESTION DU TOUR ----
  public void resetForNewGame() {
    currentIndex = 0;
    forcedDoubleShot = false;
    lastTurnPassed.clear();
  }

  public void startTurn() {
    if (players.isEmpty()) return;
    Player current = players.get(currentIndex);
    Bukkit.broadcastMessage(ChatColor.GOLD + "C’est au tour de " + ChatColor.BOLD + current.getName() + " (" + name + ")");
    giveTurnItems(current);
  }

  private void giveTurnItems(Player player) {
    PlayerInventory inv = player.getInventory();

    // Flèche spéciale pour tirer
    ItemStack arrow = new ItemStack(Material.ARROW, 1);
    ItemMeta arrowMeta = arrow.getItemMeta();
    arrowMeta.setDisplayName(ChatColor.RED + "Tir - Roulette Russe");
    arrow.setItemMeta(arrowMeta);

    // Papier spécial pour passer le tour
    ItemStack paper = new ItemStack(Material.PAPER, 1);
    ItemMeta paperMeta = paper.getItemMeta();
    paperMeta.setDisplayName(ChatColor.YELLOW + "Passer le tour - Roulette Russe");
    paper.setItemMeta(paperMeta);

    // Enclume spéciale pour reset le barillet
    ItemStack anvil = new ItemStack(Material.ANVIL, 1);
    ItemMeta anvilMeta = anvil.getItemMeta();
    anvilMeta.setDisplayName(ChatColor.GRAY + "Réinitialiser le barillet - Roulette Russe");
    anvil.setItemMeta(anvilMeta);

    // Ajoute les items à l’inventaire
    inv.addItem(arrow, paper, anvil);

    player.sendMessage(ChatColor.GREEN + "C’est ton tour ! Tu as reçu : une flèche (tir) et un papier (passer ton tour).");
  }

  public void removeTurnItems(Player player) {
    if (player == null) return;
    PlayerInventory inv = player.getInventory();

    for (int i = 0; i < inv.getSize(); i++) {
      ItemStack item = inv.getItem(i);
      if (item == null) continue;
      ItemMeta meta = item.getItemMeta();
      if (meta == null) continue;

      String name = meta.getDisplayName();
      // Retire uniquement les items du tour
      if (name.equals(ChatColor.RED + "Tir - Roulette Russe") ||
          name.equals(ChatColor.YELLOW + "Passer le tour - Roulette Russe") ||
          name.equals(ChatColor.GRAY + "Réinitialiser le barillet - Roulette Russe")) {
        inv.setItem(i, null);
      }
    }

    player.sendMessage(ChatColor.GRAY + "Tes items du tour ont été retirés.");
  }


  // ---- ACTIONS ----
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

    player.sendMessage(ChatColor.GRAY + "§7Tu fais tourner ton barillet... (§f" + newAmmo + "§7 tours avant la mort)");
    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.2f);

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
    Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + " a choisi de passer son tour !");
    mustShootNext = true;
    nextPlayer(true);
  }

  public void handleShoot(Player player) {
    if (!isCurrentPlayer(player)) {
      player.sendMessage(ChatColor.RED + "Ce n’est pas ton tour !");
      return;
    }

    Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " appuie sur la gâchette...");

    UUID uuid = player.getUniqueId();
    int ammoLeft = playerAmmo.getOrDefault(uuid, MAX_AMMO);

    ammoLeft--; // décrémente le compteur
    playerAmmo.put(uuid, ammoLeft);

    for (Player teammate : players) {
        teammate.playSound(player.getLocation(),
            Sound.ENTITY_PLAYER_HURT,
            1.0f,
            1.3f);
    }

    if (ammoLeft <= 0) {
      Bukkit.broadcastMessage(ChatColor.DARK_RED + player.getName() + " est mort(e) !");
      for (Player teammate : players) {
        if (!teammate.equals(player)) {
          teammate.playSound(player.getLocation(),
              Sound.ENTITY_ARROW_SHOOT,
              1.0f,
              1.0f);
        }
      }

      removeTurnItems(player);
      playerAmmo.remove(uuid);
      players.remove(player);

      // Reset les flags
      mustShootNext = false;
      forcedDoubleShot = false;

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
    else if (mustShootNext && roundsPlayed > 0) {
      Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " a survécu au tir obligatoire !");
      Bukkit.broadcastMessage("Il reste 1 tir obligatoire !");
      Bukkit.broadcastMessage("Le joueur actuel est : " + players.get(currentIndex).getName());
      Bukkit.broadcastMessage("Le joueur suivant est : " + players.get(currentIndex).getName());
      mustShootNext = true;
    }
    else if (mustShootNext) {
      Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " a survécu au tir obligatoire !");
      Bukkit.broadcastMessage("Le prochain tir sera un double tir !");
      Bukkit.broadcastMessage("Le joueur actuel est : " + players.get(currentIndex).getName());
      forcedDoubleShot = true;
      currentIndex = (currentIndex - 1 + players.size()) % players.size();
      Bukkit.broadcastMessage("Le joueur suivant est : " + players.get(currentIndex).getName());
      mustShootNext = false;
    }

    Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " a survécu ! Il reste " + ammoLeft + " tir(s) avant la mort.");
    lastTurnPassed.put(uuid, false);

    nextPlayer(false);
  }


  private void nextPlayer(boolean fromPass) {
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
    Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "C’est maintenant au tour de " + nextPlayer.getName());
    giveTurnItems(nextPlayer);

  }



  public boolean isCurrentPlayer(Player player) {
    return players.get(currentIndex).equals(player);
  }
}
