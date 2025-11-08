package fr.salut.squidgame.component.commands.games;

import fr.salut.squidgame.component.ListenerManager.MiniGames.Splatoon.SplatoonManager;
import fr.salut.squidgame.component.ListenerManager.MiniGames.Splatoon.SplatoonState;
import fr.salut.squidgame.component.ListenerManager.MiniGames.Splatoon.ZoneManager;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.salut.squidgame.component.ListenerManager.MiniGames.Splatoon.SplatoonManager.*;

@Command("splatoon")
@CommandPermission("spg.admin.splatoon")
public class SplatoonCommand {

  @Getter
  private static SplatoonState splatoonState = SplatoonState.OFF;

  @Subcommand("ON")
  public void splatoonON(CommandSender sender) {
    splatoonState = SplatoonState.ON;
    SplatoonManager.startEpreuve();
    sender.sendMessage(ChatColor.GREEN + "Splatoon activé !");
  }

  @Subcommand("OFF")
  public void splatoonOFF(CommandSender sender) {
    splatoonState = SplatoonState.OFF;
    SplatoonManager.clearGame();
    SplatoonManager.clearAllZones();
    sender.sendMessage(ChatColor.GRAY + "Jeu Splatoon arrêté et réinitialisé.");
  }

  @Subcommand("STOP")
  public void splatoonSTOP(CommandSender sender) {
    splatoonState = SplatoonState.STOP;
    sender.sendMessage(ChatColor.RED + "Splatoon mis en pause.");
  }


  @Subcommand("brushvalue")
  public void setBrushValue(CommandSender sender, int value) {
    if (value < 1 || value > 5) {
      sender.sendMessage(ChatColor.RED + "La valeur du pinceau doit être compris entre 1 et 5.");
      return;
    }

    brushValue = value;
    sender.sendMessage(ChatColor.GREEN + "La portée de la peinture est maintenant de §e" + brushValue + "§a bloc(s).");
  }

  @Subcommand("rechargevalue")
  public void setRechargeValue(Player sender, int value) {
    if (value < 1 || value > 20) {
      sender.sendMessage(ChatColor.RED + "Le nombre de recharges doit être compris entre 1 et 20.");
      return;
    }

    rechargeValue = value;
    sender.sendMessage(ChatColor.GREEN + "Les pinceaux ont maintenant §e" + rechargeValue + "§a unités de peinture.");
  }

  @Subcommand("setknockback")
  public void setKnockbackLevel(CommandSender sender, int value) {
    sender.sendMessage(ChatColor.GREEN + "La puissance du recule a été set à §e" + value);
    SplatoonManager.setKnockbackLevel(value);
  }

  @Subcommand("count")
  public void onCountBlocks(CommandSender sender) {
    int redCount = 0;
    int yellowCount = 0;
    int greenCount = 0;
    int blueCount = 0;

    // Parcours toutes les zones définies
    for (String zoneName : ZoneManager.getZones().keySet()) {
      ZoneManager.ZoneData zone = ZoneManager.getZones().get(zoneName);

      World world = zone.world;
      int minX = Math.min(zone.xyz1[0], zone.xyz2[0]);
      int maxX = Math.max(zone.xyz1[0], zone.xyz2[0]);
      int minY = Math.min(zone.xyz1[1], zone.xyz2[1]);
      int maxY = Math.max(zone.xyz1[1], zone.xyz2[1]);
      int minZ = Math.min(zone.xyz1[2], zone.xyz2[2]);
      int maxZ = Math.max(zone.xyz1[2], zone.xyz2[2]);

      // Parcours archaïque de tous les blocs de la zone
      for (int x = minX; x <= maxX; x++) {
        for (int y = minY; y <= maxY; y++) {
          for (int z = minZ; z <= maxZ; z++) {
            Block block = world.getBlockAt(x, y, z);

            // Vérifie si le bloc est vraiment dans la zone
            if (!ZoneManager.isInsideZone(zoneName, block.getLocation())) continue;

            Material mat = block.getType();
            if (mat == Material.RED_WOOL) redCount++;
            else if (mat == Material.YELLOW_WOOL) yellowCount++;
            else if (mat == Material.GREEN_WOOL) greenCount++;
            else if (mat == Material.BLUE_WOOL) blueCount++;
          }
        }
      }
    }

    // Met les résultats dans une map pour tri
    Map<String, Integer> results = new HashMap<>();
    results.put("Rouge", redCount);
    results.put("Jaune", yellowCount);
    results.put("Vert", greenCount);
    results.put("Bleu", blueCount);

    // Tri décroissant
    List<Map.Entry<String, Integer>> sorted = results.entrySet().stream()
        .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
        .toList();

    // Affichage des résultats triés
    sender.sendMessage("§aNombre de blocs peints par équipe :");

    for (Map.Entry<String, Integer> entry : sorted) {
      String team = entry.getKey();
      int count = entry.getValue();

      ChatColor color;
      switch (team) {
        case "Rouge": color = ChatColor.RED; break;
        case "Jaune": color = ChatColor.YELLOW; break;
        case "Vert": color = ChatColor.GREEN; break;
        case "Bleu": color = ChatColor.BLUE; break;
        default: color = ChatColor.WHITE; break;
      }

      sender.sendMessage(color + team + " : " + count);
    }
  }

  // Optionnel : une méthode utilitaire pour savoir si le jeu est actif
  public static boolean isGameRunning() {
    return splatoonState == SplatoonState.ON;
  }
}
