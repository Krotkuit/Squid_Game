package fr.salut.squidgame.component.commands;

import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.ListenerManager.MiniGames.LTTE.LTTEManager;
import fr.salut.squidgame.component.ListenerManager.MiniGames.LTTE.LTTEState;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;

@Command("ltte")
public class LTTECommand {

    private final SquidGame plugin;

    public LTTECommand(SquidGame plugin){
        this.plugin = plugin;
    }

    @DefaultFor("~")
    public void bapDefault(Player sender){
        sender.sendMessage("§cÉtat invalide. Utilisez ON, OFF ou STOP.");
    }

    @Subcommand("ON")
    public void ltteON(Player sender){
        LTTEManager.clearPlayersWithTNT();
        plugin.setLTTEState(LTTEState.ON);
        LTTEManager.startGame();
        sender.sendMessage("§aLTTE activé : toutes les procédures fonctionnent.");
    }

    @Subcommand("OFF")
    public void ltteOFF(Player sender){
        plugin.setLTTEState(LTTEState.OFF);
        LTTEManager.clearPlayersWithTNT();
        sender.sendMessage("§cLTTE désactivé : aucune procédure ne fonctionne.");
    }

    @Subcommand("STOP")
    public void ltteSTOP(Player sender){
        plugin.setLTTEState(LTTEState.STOP);
        sender.sendMessage("§eLTTE en mode STOP : seules les prisons fonctionnent.");
    }
}
