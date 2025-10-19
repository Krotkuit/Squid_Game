package fr.salut.squidgame.component.commands.games;

import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.ListenerManager.MiniGames.CarrouselZoneCounter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.*;

@Command("carrousel")
@CommandPermission("sqg.admins.commands.carrousel")
public class CarrouselCommand {

  @Subcommand("room")
  public boolean tellPlayerNumberRoom(CommandSender sender) {
    CarrouselZoneCounter counter = CarrouselZoneCounter.getInstance();
    if (counter == null) {
      sender.sendMessage("§cErreur : CarrouselZoneCounter n'est pas initialisé.");
      return true;
    }

    Map<Integer, Integer> counts = counter.countPlayersPerRoom();
    List<Map.Entry<Integer, Integer>> nonEmpty = new ArrayList<>();

    for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
      if (entry.getValue() > 0) {
        nonEmpty.add(entry);
      }
    }

    nonEmpty.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

    // Récupération de l'équipe "garde"
    Team gardeTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("garde");
    if (gardeTeam == null) {
      sender.sendMessage("§cErreur : L'équipe 'garde' n'existe pas.");
      return true;
    }

    // Envoi des messages à tous les membres de l'équipe "garde"
    for (String playerName : gardeTeam.getEntries()) {
      Player gardePlayer = Bukkit.getPlayer(playerName);
      if (gardePlayer != null && gardePlayer.isOnline()) {
        if (nonEmpty.isEmpty()) {
          gardePlayer.sendMessage("§7Aucune salle occupée.");
        } else {
          gardePlayer.sendMessage("§6Salles occupées :");
          for (Map.Entry<Integer, Integer> entry : nonEmpty) {
            gardePlayer.sendMessage(String.format("§eSalle %d : §a%d joueur%s",
                entry.getKey(),
                entry.getValue(),
                entry.getValue() > 1 ? "s" : ""));
          }
        }
      }
    }
    return true;
  }
}