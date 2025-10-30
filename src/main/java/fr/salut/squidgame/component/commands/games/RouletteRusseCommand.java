package fr.salut.squidgame.component.commands.games;

import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.ListenerManager.MiniGames.RouletteRusse.RouletteTeam;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static fr.salut.squidgame.component.ListenerManager.MiniGames.RouletteRusse.RouletteRusseManager.startGame;
import static fr.salut.squidgame.component.ListenerManager.MiniGames.RouletteRusse.RouletteRusseManager.stopGame;
import fr.salut.squidgame.component.ListenerManager.MiniGames.RouletteRusse.RouletteRusseManager;

@Command("rouletterusse")
@CommandPermission("spg.admins.commands.rouletterusse")
public class RouletteRusseCommand {

  private final SquidGame plugin;

  public RouletteRusseCommand(SquidGame plugin) {
    this.plugin = plugin;
  }

  @DefaultFor("~")
  public void defaultCommand(CommandSender sender) {
    sender.sendMessage(ChatColor.RED + "État invalide. Utilisez ON, OFF ou STOP.");
  }

  @Subcommand("ON")
  public void onRouletteRusse(CommandSender sender) {
    sender.sendMessage(ChatColor.GREEN + "La Roulette Russe vient de commencer !");
    startGame();
  }

  @Subcommand("OFF")
  public void offRouletteRusse(CommandSender sender) {
    stopGame();
    sender.sendMessage(ChatColor.RED + "La Roulette Russe est maintenant désactivée !");
  }

  @Subcommand("STOP")
  public void stopRouletteRusse(CommandSender sender) {
    sender.sendMessage(ChatColor.YELLOW + "La Roulette Russe est mise en pause !");
    for (Player player : Bukkit.getOnlinePlayers()) {
      player.sendTitle(ChatColor.RED + "Roulette Russe arrêtée", "", 10, 70, 20);
    }
  }

  @Subcommand("MORT")
  public void mortRouletteRusse(CommandSender sender, int score) {
    sender.sendMessage(ChatColor.DARK_RED + "Chaque pertie s'arrêtera après " + score + " morts.");
    RouletteTeam.MAX_DEATHS = score;
  }
}
