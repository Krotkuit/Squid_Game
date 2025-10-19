package fr.salut.squidgame.component.commands;

import fr.salut.squidgame.component.ListenerManager.LifeMode.LifeListener;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

@Command("lifemode")
@CommandPermission("spg.admin.lifemode")
public class LifeModeCommand {

  private final LifeListener lifeListener;

  public LifeModeCommand() {
    this.lifeListener = LifeListener.getInstance();
  }

  // --------- /lifemode mode on/off ---------
  public enum Mode {
    ON, OFF
  }

  @Subcommand("mode")
  public void setMode(CommandSender sender, Mode mode) {
    if (mode == null) {
      sender.sendMessage(ChatColor.RED + "Usage: /lifemode mode <ON|OFF>");
      return;
    }
    boolean enable = mode == Mode.ON;
    lifeListener.setLifeModeEnabled(enable);
    sender.sendMessage(ChatColor.GREEN + "Life mode " + (enable ? "enabled." : "disabled."));
  }

  // --------- /lifemode lifenumber <number> ---------
  @Subcommand("lifenumber")
  public void setLifeNumber(CommandSender sender, Integer number) {
    if (number == null) {
      sender.sendMessage(ChatColor.RED + "Usage: /lifemode lifenumber <number>");
      return;
    }
    lifeListener.setDefaultLives(number);
    sender.sendMessage(ChatColor.GREEN + "Default lives set to " + number + ".");
  }

  // --------- /lifemode player revive ---------
  @Subcommand("player revive")
  public void revivePlayers(CommandSender sender) {
    if (!lifeListener.isLifeModeEnabled()) {
      sender.sendMessage(ChatColor.RED + "Le mode de vie est désactivé. Impossible d'exécuter la commande /lifemode player revive.");
      return;
    }

    for (Player player : Bukkit.getOnlinePlayers()) {
      Team team = player.getScoreboard().getEntryTeam(player.getName());
      if (team != null && team.getName().equalsIgnoreCase("garde")) continue;

      Integer lives = lifeListener.getPlayerLives().get(player);
      if (lives != null && lives > 0) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join joueur " + player.getName());
        sender.sendMessage(ChatColor.GREEN + "Player " + player.getName() + " revived.");
      }
    }
  }

  // --------- /lifemode player reset <number> ---------
  @Subcommand("player reset")
  public void resetPlayers(CommandSender sender, Integer number) {
    if (number == null) {
      sender.sendMessage(ChatColor.RED + "Usage: /lifemode player reset <number>");
      return;
    }
    if (!lifeListener.isLifeModeEnabled()) {
      sender.sendMessage(ChatColor.RED + "Le mode de vie est désactivé. Impossible d'exécuter la commande /lifemode player reset <number>.");
      return;
    }

    for (Player player : Bukkit.getOnlinePlayers()) {
      lifeListener.getPlayerLives().put(player, number);
      lifeListener.updatePlayerXP(player, number); // Met à jour l'XP
    }

    sender.sendMessage(ChatColor.GREEN + "All players' lives have been reset to " + number + ".");
  }

}
