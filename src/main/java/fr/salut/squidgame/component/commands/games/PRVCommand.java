package fr.salut.squidgame.component.commands.games;

import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.ListenerManager.MiniGames.PRV.PRVListener;
import fr.salut.squidgame.component.ListenerManager.MiniGames.PRV.PRVState;
import org.bukkit.command.CommandSender;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("prv")
@CommandPermission("spg.admins.commands.prv")
public class PRVCommand {

    SquidGame plugin = SquidGame.getInstance();

    @DefaultFor("~")
    public void bapDefault(CommandSender sender){
        sender.sendMessage("§cÉtat invalide. Utilisez ON, OFF ou STOP.");
    }

    @Subcommand("ON")
    public void prvON(CommandSender sender){
        plugin.setPrvState(PRVState.ON);
        sender.sendMessage("§aPRV activé : toutes les procédures fonctionnent.");
    }

    @Subcommand("OFF")
    public void prvOFF(CommandSender sender){
        plugin.setPrvState(PRVState.OFF);
        PRVListener.resetPrisoners();
        sender.sendMessage("§cPRV désactivé : aucune procédure ne fonctionne.");
    }

    @Subcommand("STOP")
    public void prvSTOP(CommandSender sender){
        plugin.setPrvState(PRVState.STOP);
        sender.sendMessage("§ePRV en mode STOP : seules les prisons fonctionnent.");
    }

}
