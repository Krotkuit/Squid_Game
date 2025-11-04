package fr.salut.squidgame.component.commands.games;

import fr.salut.squidgame.component.ListenerManager.MiniGames.BaP.BaPState;
import fr.salut.squidgame.component.ListenerManager.MiniGames.LoupGlace.LoupGlaceState;
import fr.salut.squidgame.component.ListenerManager.MiniGames.LoupGlace.LoupGlaceManager;
import lombok.Getter;
import lombok.Setter;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("loupglace")
@CommandPermission("sgq.admins.commands.loupglace")
public class LoupGlaceCommand {

  @Getter @Setter
  private static LoupGlaceState loupGlaceState = LoupGlaceState.OFF;


  @Subcommand("start")
  void onStart() {
    LoupGlaceManager.startLoupGlace();
    loupGlaceState = LoupGlaceState.ON;
  }

  @Subcommand("stop")
  void onStop() {
    loupGlaceState = LoupGlaceState.STOP;
  }

  @Subcommand("off")
  void onOff() {
    LoupGlaceManager.offLoupGlace();
    loupGlaceState = LoupGlaceState.OFF;
  }

  @Subcommand("shuffle")
  void onShuffle() {
    LoupGlaceManager.shuffleLoupGlaceTeams();

  }

}
