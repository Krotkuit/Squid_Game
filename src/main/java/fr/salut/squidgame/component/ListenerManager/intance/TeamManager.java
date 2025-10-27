package fr.salut.squidgame.component.ListenerManager.intance;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamManager{

  private static final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

  public static void Team_Instance() {


    Team team;

    // Team mort
    team = scoreboard.getTeam("mort");
    if (team == null) {
      team = scoreboard.registerNewTeam("mort");
      team.setPrefix(ChatColor.GRAY + "[Mort] ");
      team.setColor(ChatColor.GRAY);  // S'assure que le pseudo reste gris
      team.setOption(org.bukkit.scoreboard.Team.Option.COLLISION_RULE, org.bukkit.scoreboard.Team.OptionStatus.NEVER);
    }

    // Team joueur
    team = scoreboard.getTeam("joueur");
    if (team == null) {
      team = scoreboard.registerNewTeam("joueur");
      team.setPrefix(ChatColor.GREEN + "[Joueur] ");
      team.setColor(ChatColor.WHITE);
    }


    // Team garde
    team = scoreboard.getTeam("garde");
    if (team == null) {
      team = scoreboard.registerNewTeam("garde");
      team.setPrefix(ChatColor.LIGHT_PURPLE + "[Garde] ");
      team.setColor(ChatColor.WHITE);
    }
  }

  public static boolean hasTeamOnlinePlayers(Team team){
    for (OfflinePlayer player : team.getPlayers()){
      if (player.isOnline()){
        return true;
      }
    }
    return false;
  }

  public static List<Player> getTeamOnlinePlayers(Team team){

    List<Player> players = new ArrayList<>();

    for (OfflinePlayer player : team.getPlayers()){
      if (player.isOnline()){
        players.add(player.getPlayer());
      }
    }
    return players;
  }
}
