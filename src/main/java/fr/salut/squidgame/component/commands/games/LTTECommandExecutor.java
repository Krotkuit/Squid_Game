package fr.salut.squidgame.component.commands;

import fr.salut.squidgame.SquidGame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LTTECommandExecutor implements CommandExecutor {
  private static final SquidGame plugin = SquidGame.getInstance();
  private static int bombTimer = 150; // Temps par défaut en secondes (2min30)
  private static double bombProbability = 0.05; // Probabilité par défaut (5%)

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length < 1) {
      sender.sendMessage(ChatColor.RED + "Usage: /ltte <ON|OFF|STOP|timer <score>|prob <score>>");
      return false;
    }

    switch (args[0].toLowerCase()) {
      case "timer":
        if (args.length < 2) {
          sender.sendMessage(ChatColor.RED + "Usage: /ltte timer <score>");
          return false;
        }
        try {
          bombTimer = Integer.parseInt(args[1]);
          sender.sendMessage(ChatColor.GREEN + "Le temps de la bombe a été défini à " + bombTimer + " secondes.");
        } catch (NumberFormatException e) {
          sender.sendMessage(ChatColor.RED + "Veuillez entrer un nombre valide pour le temps.");
        }
        break;

      case "prob":
        if (args.length < 2) {
          sender.sendMessage(ChatColor.RED + "Usage: /ltte prob <score>");
          return false;
        }
        try {
          bombProbability = Double.parseDouble(args[1]);
          if (bombProbability <= 0) {
            sender.sendMessage(ChatColor.RED + "La probabilité doit être supérieure à 0.");
            return false;
          }
          sender.sendMessage(ChatColor.GREEN + "La probabilité de la bombe a été définie à " + bombProbability + ".");
        } catch (NumberFormatException e) {
          sender.sendMessage(ChatColor.RED + "Veuillez entrer un nombre valide pour la probabilité.");
        }
        break;

      default:
        sender.sendMessage(ChatColor.RED + "Commande inconnue. Usage: /ltte <ON|OFF|STOP|timer <score>|prob <score>>");
        break;
    }
    return true;
  }

  public static int getBombTimer() {
    return bombTimer;
  }

  public static double getBombProbability() {
    return bombProbability;
  }
}