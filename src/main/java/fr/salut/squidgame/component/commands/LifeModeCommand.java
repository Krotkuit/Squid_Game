package fr.salut.squidgame.component.commands;

import fr.salut.squidgame.component.ListenerManager.LifeMode.LifeListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LifeModeCommand implements TabExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length < 1) {
      sender.sendMessage(ChatColor.RED + "Usage: /lifemode <mode|lifenumber|player>");
      return true;
    }

    LifeListener lifeListener = LifeListener.getInstance();

    switch (args[0].toLowerCase()) {
      case "mode":
        if (args.length < 2) {
          sender.sendMessage(ChatColor.RED + "Usage: /lifemode mode <on|off>");
          return true;
        }
        boolean enabled = args[1].equalsIgnoreCase("on");
        lifeListener.setLifeModeEnabled(enabled);
        sender.sendMessage(ChatColor.GREEN + "Life mode " + (enabled ? "enabled" : "disabled") + ".");
        break;

      case "lifenumber":
        if (args.length < 2) {
          sender.sendMessage(ChatColor.RED + "Usage: /lifemode lifenumber <number>");
          return true;
        }
        try {
          int lives = Integer.parseInt(args[1]);
          lifeListener.setDefaultLives(lives);
          sender.sendMessage(ChatColor.GREEN + "Default lives set to " + lives + ".");
        } catch (NumberFormatException e) {
          sender.sendMessage(ChatColor.RED + "Invalid number.");
        }
        break;

      case "player":
        if (args.length < 2) {
          sender.sendMessage(ChatColor.RED + "Usage: /lifemode player <revive|reset>");
          return true;
        }
        switch (args[1].toLowerCase()) {
          case "revive":
            for (Player player : Bukkit.getOnlinePlayers()) {
              Integer lives = lifeListener.getPlayerLives().get(player);
              if (lives != null && lives > 0) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join joueur " + player.getName());
                sender.sendMessage(ChatColor.GREEN + "Player " + player.getName() + " revived.");
              }
            }
            break;

          case "reset":
            if (args.length < 3) {
              sender.sendMessage(ChatColor.RED + "Usage: /lifemode player reset <number>");
              return true;
            }
            try {
              int resetLives = Integer.parseInt(args[2]);
              for (Player player : Bukkit.getOnlinePlayers()) {
                lifeListener.getPlayerLives().put(player, resetLives);
                lifeListener.updatePlayerXP(player, resetLives); // Met à jour l'XP
              }
              sender.sendMessage(ChatColor.GREEN + "All players' lives have been reset to " + resetLives + ".");
            } catch (NumberFormatException e) {
              sender.sendMessage(ChatColor.RED + "Invalid number.");
            }
            break;

          default:
            sender.sendMessage(ChatColor.RED + "Unknown subcommand for player.");
            break;
        }
        break;

      default:
        sender.sendMessage(ChatColor.RED + "Unknown subcommand.");
        break;
    }
    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    List<String> completions = new ArrayList<>();
    if (args.length == 1) {
      completions.add("mode");
      completions.add("lifenumber");
      completions.add("player");
    } else if (args.length == 2) {
      if (args[0].equalsIgnoreCase("mode")) {
        completions.add("on");
        completions.add("off");
      } else if (args[0].equalsIgnoreCase("player")) {
        completions.add("revive");
        completions.add("reset");
      }
    } else if (args.length == 3) {
      if (args[0].equalsIgnoreCase("player") && args[1].equalsIgnoreCase("reset")) {
        completions.add("<number>");
      }
    }
    return completions;
  }
}