package fr.salut.squidgame.component.commands.games;

import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.ListenerManager.MiniGames.PRV.PRVListener;
import fr.salut.squidgame.component.ListenerManager.MiniGames.PRV.PRVState;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;

@Command("prv")
public class PRVCommand {

    SquidGame plugin = SquidGame.getInstance();

    @DefaultFor("~")
    public void bapDefault(Player sender){
        sender.sendMessage("§cÉtat invalide. Utilisez ON, OFF ou STOP.");
    }

    @Subcommand("ON")
    public void prvON(Player sender){
        plugin.setPrvState(PRVState.ON);
        sender.sendMessage("§aPRV activé : toutes les procédures fonctionnent.");
    }

    @Subcommand("OFF")
    public void prvOFF(Player sender){
        plugin.setPrvState(PRVState.OFF);
        PRVListener.resetPrisoners();
        sender.sendMessage("§cPRV désactivé : aucune procédure ne fonctionne.");
    }

    @Subcommand("STOP")
    public void prvSTOP(Player sender){
        plugin.setPrvState(PRVState.STOP);
        sender.sendMessage("§ePRV en mode STOP : seules les prisons fonctionnent.");
    }

}
