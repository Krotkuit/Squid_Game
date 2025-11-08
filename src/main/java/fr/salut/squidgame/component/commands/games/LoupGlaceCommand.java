package fr.salut.squidgame.component.commands.games;

import fr.salut.squidgame.component.ListenerManager.MiniGames.BaP.BaPState;
import fr.salut.squidgame.component.ListenerManager.MiniGames.LoupGlace.LoupGlaceState;
import fr.salut.squidgame.component.ListenerManager.MiniGames.LoupGlace.LoupGlaceManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("loupglace")
@CommandPermission("sgq.admins.commands.loupglace")
public class LoupGlaceCommand {

  @Getter @Setter
  private static LoupGlaceState loupGlaceState = LoupGlaceState.OFF;


  @Subcommand("start")
  void onStart(CommandSender sender) {
    LoupGlaceManager.startLoupGlace();
    loupGlaceState = LoupGlaceState.ON;
    sender.sendMessage("§aLe LoupGlacé est lancé !");
  }

  @Subcommand("stop")
  void onStop(CommandSender sender) {
    loupGlaceState = LoupGlaceState.STOP;

    sender.sendMessage("§6Le LoupGlacé est stoppé !");
  }

  @Subcommand("off")
  void onOff(CommandSender sender) {
    LoupGlaceManager.offLoupGlace();
    loupGlaceState = LoupGlaceState.OFF;

    sender.sendMessage("§cLe LoupGlacé est arrêté !");
  }

  @Subcommand("shuffle")
  void onShuffle(CommandSender sender) {
    LoupGlaceManager.shuffleLoupGlaceTeams();

    sender.sendMessage("§3Le LoupGlacé lance le suffle !");
  }

}
