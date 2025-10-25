package fr.salut.squidgame.component.commands.games;

import fr.salut.squidgame.component.ListenerManager.MiniGames.Splatoon.SplatoonManager;
import fr.salut.squidgame.component.ListenerManager.MiniGames.Splatoon.SplatoonState;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("splatoon")
@CommandPermission("spg.admin.splatoon")
public class SplatoonCommand {

  @Getter
  private static SplatoonState splatoonState = SplatoonState.OFF;

  @Subcommand("ON")
  public void splatoonON(Player sender) {
    splatoonState = SplatoonState.ON;
    SplatoonManager.startEpreuve();
    sender.sendMessage(ChatColor.GREEN + "Splatoon activé !");
  }

  @Subcommand("OFF")
  public void splatoonOFF(Player sender) {
    splatoonState = SplatoonState.OFF;
    SplatoonManager.clearGame();
    sender.sendMessage(ChatColor.GRAY + "Jeu Splatoon arrêté et réinitialisé.");
  }

  @Subcommand("STOP")
  public void splatoonSTOP(Player sender) {
    splatoonState = SplatoonState.STOP;
    sender.sendMessage(ChatColor.RED + "Splatoon mis en pause.");
  }

  // Optionnel : une méthode utilitaire pour savoir si le jeu est actif
  public static boolean isGameRunning() {
    return splatoonState == SplatoonState.ON;
  }
}
