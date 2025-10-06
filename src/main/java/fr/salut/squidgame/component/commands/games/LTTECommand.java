package fr.salut.squidgame.component.commands.games;

import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.ListenerManager.MiniGames.LTTE.LTTEManager;
import fr.salut.squidgame.component.ListenerManager.MiniGames.LTTE.LTTEState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;

@Command("ltte")
public class LTTECommand {

    private final SquidGame plugin;

    public LTTECommand(SquidGame plugin) {
        this.plugin = plugin;
    }

    @DefaultFor("~")
    public void bapDefault(CommandSender sender) {
        sender.sendMessage("§cÉtat invalide. Utilisez ON, OFF ou STOP.");
    }

    @Subcommand("ON")
    public void ltteON(CommandSender sender) {
        LTTEManager.clearPlayersWithTNT();
        plugin.setLTTEState(LTTEState.ON);
        LTTEManager.startGame();
        sender.sendMessage("§aLTTE activé : toutes les procédures fonctionnent.");
    }

    @Subcommand("OFF")
    public void ltteOFF(CommandSender sender) {
        plugin.setLTTEState(LTTEState.OFF);
        LTTEManager.clearPlayersWithTNT();
        sender.sendMessage("§cLTTE désactivé : aucune procédure ne fonctionne.");
        for (Player player1 : Bukkit.getOnlinePlayers()) {
            for (Player player2 : Bukkit.getOnlinePlayers()) {
              try {
                SquidGame.getInstance().getGlowingEntities().unsetGlowing(player1, player2);
                SquidGame.getInstance().getGlowingEntities().unsetGlowing(player2, player1);
              } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
              }
            }
        }
    }

    @Subcommand("STOP")
    public void ltteSTOP(CommandSender sender) {
        plugin.setLTTEState(LTTEState.STOP);
        sender.sendMessage("§eLTTE en mode STOP : seules les prisons fonctionnent.");
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.RED + "L'épreuve a été arrêtée", "", 10, 70, 20);
        }
    }

    @Subcommand("timer")
    public void onSetTimer(CommandSender sender, int score) {
        if (score < 30) {
            sender.sendMessage("§cLe temps doit être supérieur ou égal à 30 secondes.");
            return;
        }

        LTTECommandExecutor.setBombTimer(score);
        sender.sendMessage("§aLe temps de la bombe a été défini à : " + score + " secondes.");
    }

    @Subcommand("prob")
    public void onSetProb(CommandSender sender, double score) {
        if (score <= 0) {
            sender.sendMessage("§cLa valeur doit être strictement positive.");
            return;
        }

        LTTECommandExecutor.setBombProbability(score);
        sender.sendMessage("§aLa probabilité de la bombe a été définie à : " + score + ".");
    }
}