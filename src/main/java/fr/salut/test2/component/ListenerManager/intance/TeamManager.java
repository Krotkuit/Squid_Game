package fr.salut.test2.component.ListenerManager.intance;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class TeamManager {
  public static void Team_Instance()  {

    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard scoreboard = manager.getMainScoreboard();
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
}
