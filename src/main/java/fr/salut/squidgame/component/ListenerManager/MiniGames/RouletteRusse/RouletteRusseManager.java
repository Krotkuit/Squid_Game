package fr.salut.squidgame.component.ListenerManager.MiniGames.RouletteRusse;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import fr.salut.squidgame.component.ListenerManager.MiniGames.RouletteRusse.RouletteTeam;

import java.util.*;

public class RouletteRusseManager implements Listener {


  // Liste des équipes de la roulette russe
  private static final Map<String, RouletteTeam> teams = new HashMap<>();

  @Getter
  private static final List<String> allowedTeams = List.of(
      "noir", "gris_fonce", "gris_clair", "blanc", "marron", "rose", "magenta",
      "indigo", "bleu_marine", "bleu_roi", "bleu_ciel", "cyan", "vert_profond", "vert_citron",
      "jaune", "or", "orange", "saumon", "bordeaux", "rouge"
  );

  // Indique si le jeu est en cours
  @Getter
  private static boolean gameRunning = false;

  @EventHandler
  public void onPlayerUse(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    ItemStack item = event.getItem();
    if (item == null || !item.hasItemMeta()) return;

    RouletteTeam team = getTeamOf(player);
    if (team == null) return;

    // Vérifie que c’est bien son tour
    if (!team.isCurrentPlayer(player)) return;

    String displayName = item.getItemMeta().getDisplayName();

    if (!team.isCurrentPlayer(player)) return;

    String shootName = ChatColor.RED + "Tir";
    String passName = ChatColor.YELLOW + "Passer le tour";
    String reloadName = ChatColor.GRAY + "Réinitialiser le barillet";
    String checkName = ChatColor.BLUE + "Regarder dans le barillet";

    if (shootName.equals(displayName)) {
      event.setCancelled(true);
      team.handleShoot(player);
    } else if (passName.equals(displayName)) {
      event.setCancelled(true);
      team.handlePass(player);
    } else if (reloadName.equals(displayName)) {
      event.setCancelled(true);
      team.handleReload(player);
    } else if (checkName.equals(displayName)) {
      event.setCancelled(true);
      team.handleCheckAmmo(player);
    }
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    if (!gameRunning) return;

    Player player = event.getPlayer();
    RouletteTeam team = getTeamOf(player);

    if (team != null) {
      boolean wasCurrent = team.isCurrentPlayer(player); // stocker avant suppression
      //team.removePlayer(player);
      Bukkit.getLogger().info("" + player.getName() + " a quitté sa table. " + team.players.size() + " joueurs restants.");
      if (wasCurrent && !team.players.isEmpty()) {
        team.nextPlayer(false);
        team.removeTurnItems(player);
      }
    }
  }

  @EventHandler
  public void onJoinEvent(PlayerJoinEvent event) {
    if (!gameRunning) return;

    Player player = event.getPlayer();
    RouletteTeam team = getTeamOf(player);

    if (team == null) {
      var scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

      // Parcourir toutes les équipes autorisées pour retrouver le joueur
      for (String teamName : allowedTeams) {
        Team scoreboardTeam = scoreboard.getTeam(teamName);

        if (scoreboardTeam != null && scoreboardTeam.getEntries().contains(player.getName())) {
          // Récupérer ou créer la RouletteTeam correspondante
          team = teams.get(teamName);
          if (team == null) {
            team = new RouletteTeam(teamName);
            teams.put(teamName, team);
          }

          // Ajouter le joueur
          //team.addPlayer(player);

          Bukkit.getLogger().info(ChatColor.GREEN + "Joueur " + player.getName() + " réintégré à l'équipe " + teamName);
          break;
        }
      }
    }
  }

  // ---- GESTION DU JEU ----
  public static void startGame() {
    loadTeamsFromScoreboard();
    if (teams.isEmpty()) {
      Bukkit.getLogger().info(ChatColor.RED + "Aucune équipe n’est enregistrée pour la Roulette Russe !");
      return;
    }

    gameRunning = true;
    for (RouletteTeam team : teams.values()) {
      team.resetForNewGame();
      team.startTurn();
      for (Player player : team.getPlayers()) {
        player.sendMessage(ChatColor.AQUA + "La Roulette Russe commence !");
        player.playSound(player.getLocation(), Sound.ITEM_SPYGLASS_USE, 1.0f, 0.3f);
        player.sendTitle("§bLe jeu commence !",
            "§eLe joueur qui commence est : §6" + team.players.get(0).getName(),
            10, 70, 20);
      }

    }
  }

  public static void stopGame() {
    if (!gameRunning) {
      Bukkit.getLogger().info(ChatColor.RED + "Aucun jeu de Roulette Russe n’est en cours.");
      return;
    }

    for (RouletteTeam team : teams.values()) {  // <-- ici on prend les valeurs de la map
      for (Player player : team.getPlayers()) {
        team.removeTurnItems(player);
      }
    }

    gameRunning = false;
    Bukkit.getLogger().info(ChatColor.RED + "La Roulette Russe est terminée !");
    teams.clear();
  }

  // ---- GESTION DES ÉQUIPES ----
  private static void loadTeamsFromScoreboard() {
    teams.clear();

    var scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    for (String teamName : allowedTeams) {
      Team scoreboardTeam = scoreboard.getTeam(teamName);

      // Si la team n'existe pas sur le scoreboard → on l'ignore
      if (scoreboardTeam == null) continue;

      // Si la team n’a aucun joueur → on l’ignore
      if (scoreboardTeam.getEntries().isEmpty()) continue;

      RouletteTeam rrTeam = new RouletteTeam(teamName);

      for (String entry : scoreboardTeam.getEntries()) {
        Player player = Bukkit.getPlayer(entry);
        if (player != null && player.isOnline()) {
          rrTeam.addPlayer(player);
        }
      }

      if (!rrTeam.getPlayers().isEmpty()) {
        teams.put(teamName, rrTeam);
        Bukkit.getLogger().info(ChatColor.GREEN + "Équipe détectée : " + ChatColor.YELLOW + teamName
            + ChatColor.GRAY + " (" + rrTeam.getPlayers().size() + " joueurs)");
        for (Player p : rrTeam.getPlayers()) {
          Bukkit.getLogger().info(ChatColor.GRAY + " - " + ChatColor.AQUA + p.getName());
        }
      }
    }

    if (teams.isEmpty()) {
      Bukkit.getLogger().info(ChatColor.RED + "Aucune des équipes autorisées n’a été trouvée sur le serveur !");
    }
  }



  public static void addTeam(String teamName) {
    String lower = teamName.toLowerCase();

    // Vérifie si le nom est autorisé
    if (!allowedTeams.contains(lower)) {
      Bukkit.getLogger().info(ChatColor.RED + "L’équipe " + teamName + " n’est pas autorisée !");
      Bukkit.getLogger().info(ChatColor.GRAY + "Équipes autorisées : " + ChatColor.YELLOW + String.join(", ", allowedTeams));
      return;
    }

    // Vérifie si elle existe déjà
    if (teams.containsKey(lower)) {
      Bukkit.getLogger().info(ChatColor.RED + "L’équipe " + teamName + " existe déjà !");
      return;
    }

    teams.put(lower, new RouletteTeam(lower));
    Bukkit.getLogger().info(ChatColor.GREEN + "Équipe " + lower + " ajoutée !");
  }

  public static void addPlayerToTeam(String teamName, Player player) {
    RouletteTeam team = teams.get(teamName);
    if (team == null) {
      player.sendMessage(ChatColor.RED + "L’équipe " + teamName + " n’existe pas.");
      return;
    }
    team.addPlayer(player);
  }

  public static void removePlayer(Player player) {
    for (RouletteTeam team : teams.values()) {
      team.removePlayer(player);
    }
  }

  public static RouletteTeam getTeamOf(Player player) {
    for (RouletteTeam team : teams.values()) {
      if (team.getPlayers().contains(player)) return team;
    }
    return null;
  }

  // ---- ACTIONS DE JEU ----
  public static void playerShoots(Player player) {
    RouletteTeam team = getTeamOf(player);
    if (team == null || !gameRunning) return;
    team.handleShoot(player);
  }

  public static void playerPasses(Player player) {
    RouletteTeam team = getTeamOf(player);
    if (team == null || !gameRunning) return;
    team.handlePass(player);
  }

  public static Set<String> getActiveTeams() {
    return teams.keySet();
  }
}
