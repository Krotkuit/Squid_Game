package fr.salut.squidgame.component.ListenerManager.MiniGames.RouletteRusse;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class RouletteRusseManager implements Listener {

  // Liste des équipes de la roulette russe
  private static Map<String, RouletteTeam> teams = new HashMap<>();

  @Getter
  private static final List<String> allowedTeams = List.of(
      "dark_red", "rouge", "orange", "jaune", "vert_clair", "vert", "vert_profond",
      "bleu", "cyan", "bleu_marine", "bleu_profond", "violet", "magenta", "rose",
      "blanc", "light_gray", "gris", "dark_gray", "black", "marron"
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

    String shootName = ChatColor.RED + "Tir - Roulette Russe";
    String passName = ChatColor.YELLOW + "Passer le tour - Roulette Russe";
    String reloadName = ChatColor.GRAY + "Réinitialiser le barillet - Roulette Russe";

    if (shootName.equals(displayName)) {
      event.setCancelled(true);
      team.handleShoot(player);
    } else if (passName.equals(displayName)) {
      event.setCancelled(true);
      team.handlePass(player);
    } else if (reloadName.equals(displayName)) {
      event.setCancelled(true);
      team.handleReload(player);
    }

  }



  // ---- GESTION DU JEU ----
  public static void startGame() {
    loadTeamsFromScoreboard();
    if (teams.isEmpty()) {
      Bukkit.broadcastMessage(ChatColor.RED + "Aucune équipe n’est enregistrée pour la Roulette Russe !");
      return;
    }

    gameRunning = true;
    Bukkit.broadcastMessage(ChatColor.GOLD + "La Roulette Russe commence !");
    for (RouletteTeam team : teams.values()) {
      team.resetForNewGame();
      team.startTurn();
    }
  }

  public static void stopGame() {
    if (!gameRunning) {
      Bukkit.broadcastMessage(ChatColor.RED + "Aucun jeu de Roulette Russe n’est en cours.");
      return;
    }

    for (RouletteTeam team : teams.values()) {  // <-- ici on prend les valeurs de la map
      for (Player player : team.getPlayers()) {
        team.removeTurnItems(player);
      }
    }

    gameRunning = false;
    Bukkit.broadcastMessage(ChatColor.RED + "La Roulette Russe est terminée !");
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
        Bukkit.broadcastMessage(ChatColor.GREEN + "Équipe détectée : " + ChatColor.YELLOW + teamName
            + ChatColor.GRAY + " (" + rrTeam.getPlayers().size() + " joueurs)");
      }
    }

    if (teams.isEmpty()) {
      Bukkit.broadcastMessage(ChatColor.RED + "Aucune des équipes autorisées n’a été trouvée sur le serveur !");
    }
  }



  public static void addTeam(String teamName) {
    String lower = teamName.toLowerCase();

    // Vérifie si le nom est autorisé
    if (!allowedTeams.contains(lower)) {
      Bukkit.broadcastMessage(ChatColor.RED + "L’équipe " + teamName + " n’est pas autorisée !");
      Bukkit.broadcastMessage(ChatColor.GRAY + "Équipes autorisées : " + ChatColor.YELLOW + String.join(", ", allowedTeams));
      return;
    }

    // Vérifie si elle existe déjà
    if (teams.containsKey(lower)) {
      Bukkit.broadcastMessage(ChatColor.RED + "L’équipe " + teamName + " existe déjà !");
      return;
    }

    teams.put(lower, new RouletteTeam(lower));
    Bukkit.broadcastMessage(ChatColor.GREEN + "Équipe " + lower + " ajoutée !");
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
