package fr.salut.squidgame.component.commands.games;

import fr.salut.squidgame.component.ListenerManager.MiniGames.LoupGlace.LoupGlaceState;
import fr.salut.squidgame.component.ListenerManager.MiniGames.LoupGlace.LoupGlaceManager;
import lombok.Getter;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("loupglace")
@CommandPermission("sgq.admins.commands.loupglace")
public class LoupGlaceCommand {

  @Getter
  private static LoupGlaceState loupGlaceState = LoupGlaceState.OFF;


  @Subcommand("start")
  void onStart() {
    // LoupGlaceManager.startLoupGlace();
    loupGlaceState = LoupGlaceState.ON;
  }

  @Subcommand("stop")
  void onStop() {
    // LoupGlaceManager.stopLoupGlace();
    loupGlaceState = LoupGlaceState.STOP;
  }

  @Subcommand("off")
  void onOff() {
    // LoupGlaceManager.resetLoupGlace();
    loupGlaceState = LoupGlaceState.OFF;
  }

  @Subcommand("shuffle")
  void onShuffle() {
    LoupGlaceManager.shuffleLoupGlaceTeams();

  }

}
