package fr.salut.squidgame.component.commands;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.Arrays;
import java.util.List;

public class EpreuveCommand {

  @Getter
  private static final List<String> EPREUVES = Arrays.asList(
      "Lobby", "123Soleil", "ArcEnCiel", "BAP", "Bille", "BriseGlace", "CacheCache", "Carrousel",
      "ChaiseMusicale", "CordeASauter", "LTTE", "PRV", "Puissance4", "SquidGameAerien", "SalleGrise"
  );

  @Getter
  @Setter
  private static String epreuve = "Lobby";

  @Command("setepreuve")
  @CommandPermission("sg.admin.setepreuve")
  @AutoComplete("@epreuves")
  public void onSetEpreuve(CommandSender sender, String epreuve) {
    if (epreuve == null || EPREUVES.stream().noneMatch(e -> e.equalsIgnoreCase(epreuve))) {
      sender.sendMessage(ChatColor.RED + "Épreuve invalide. Utilisez une des épreuves suivantes : " + String.join(", ", EPREUVES));
      return;
    }

    // On choisit la forme exacte depuis la liste (corrige la casse)
    String selected = EPREUVES.stream().filter(e -> e.equalsIgnoreCase(epreuve)).findFirst().orElse("Lobby");

    setEpreuve(selected);
    Bukkit.getLogger().info("L'épreuve a été définie sur : " + selected);
    sender.sendMessage(ChatColor.GREEN + "L'épreuve a été définie sur : " + selected);
  }

  @Command("getepreuve")
  @CommandPermission("sg.admin.getepreuve")
  public void onGetEpreuve(CommandSender sender) {
    sender.sendMessage("§aL'épreuve actuelle est : " + getEpreuve());
  }
}
