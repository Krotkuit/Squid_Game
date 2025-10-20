package fr.salut.squidgame.component.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EpreuveCommand implements TabExecutor, TabCompleter {

  private static final List<String> EPREUVES = Arrays.asList(
    "Lobby", "123Soleil", "ArcEnCiel", "BAP", "CacheCache", "Carrousel", "ChaiseMusicale",
    "CordeASauter", "LTTE", "PRV", "Puissance4", "SquidGameAerien", "SalleGrise"
  );

  private static String epreuve = "Lobby"; // Valeur par défaut

  public static void setEpreuve(String newEpreuve) {
    epreuve = newEpreuve;
  }

  public static String getEpreuve() {
    return epreuve;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (label.equalsIgnoreCase("setepreuve")) {
      if (args.length < 1) {
        sender.sendMessage("§cVeuillez spécifier une épreuve !");
        return false;
      }

      String newEpreuve = args[0];
      if (!EPREUVES.contains(newEpreuve)) {
        sender.sendMessage("§cÉpreuve invalide. Utilisez une des épreuves suivantes : " + String.join(", ", EPREUVES));
        return false;
      }

      setEpreuve(newEpreuve);
      Bukkit.broadcastMessage("§aL'épreuve a été définie sur : " + newEpreuve);
      return true;
    }

    if (label.equalsIgnoreCase("getepreuve")) {
      sender.sendMessage("§aL'épreuve actuelle est : " + getEpreuve());
      return true;
    }

    return false;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    if (alias.equalsIgnoreCase("setepreuve") && args.length == 1) {
      List<String> completions = new ArrayList<>();
      for (String epreuve : EPREUVES) {
        if (epreuve.toLowerCase().startsWith(args[0].toLowerCase())) {
          completions.add(epreuve);
        }
      }
      return completions;
    }
    return null;
  }
}