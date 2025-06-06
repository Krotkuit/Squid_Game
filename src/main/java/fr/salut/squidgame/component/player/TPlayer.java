package fr.salut.squidgame.component.player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


@Getter
public class TPlayer {

  @Setter
  private Player player;
  private boolean dead = false;
  private boolean admin = false;

  public TPlayer(Player player) {
    this.player = player;
    setDead(false);
  }

    public void setDead(boolean dead) {
    //Bukkit.getScoreboardManager().getMainScoreboard().getTeam("mort").addEntry(player.getName());
    //Bukkit.getScoreboardManager().getMainScoreboard().getTeam("joueur").addEntry(player.getName());
    // Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("garde");
    if (dead) Bukkit.getScoreboardManager().getMainScoreboard().getTeam("mort").addEntry(player.getName());
    else Bukkit.getScoreboardManager().getMainScoreboard().getTeam("joueur").addEntry(player.getName());
    /*if (dead) this.player.setPlayerListName(ChatColor.GRAY + "[Mort] " + this.player.getName());
    else this.player.setPlayerListName(ChatColor.GREEN + "[Joueur] " + ChatColor.WHITE + this.player.getName());*/
    this.dead = dead;
  }

  public void updateTab() {
    if (admin) {
      Bukkit.getScoreboardManager().getMainScoreboard().getTeam("garde").addEntry(player.getName());
      return;
    }
    if (dead) Bukkit.getScoreboardManager().getMainScoreboard().getTeam("mort").addEntry(player.getName());
    else Bukkit.getScoreboardManager().getMainScoreboard().getTeam("joueur").addEntry(player.getName());
  }

  public void setAdmin(boolean admin) {

    if (admin) this.player.setPlayerListName(ChatColor.LIGHT_PURPLE + "[Garde] " + ChatColor.WHITE + this.player.getName());
    else this.player.setPlayerListName(ChatColor.WHITE + this.player.getName());
    this.admin = admin;

  }
}
