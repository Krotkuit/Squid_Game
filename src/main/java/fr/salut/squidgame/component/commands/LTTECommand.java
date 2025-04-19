package fr.salut.squidgame.component.commands;

import fr.salut.squidgame.Main;
import fr.salut.squidgame.component.ListenerManager.MiniGames.LTTE.LTTEManager;
import fr.salut.squidgame.component.ListenerManager.MiniGames.LTTE.LTTEState;
import fr.salut.squidgame.component.ListenerManager.MiniGames.PRV.PRVListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerJoinEvent;


public class LTTECommand implements CommandExecutor {
    private final Main plugin;

    public LTTECommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(args);
        if (args.length < 1 || !label.equalsIgnoreCase("ltte")) {
            sender.sendMessage("§cUsage: /ltte <ON|OFF|STOP>");
            return true;
        }

        switch (args[0].toUpperCase()) {
            case "ON":
                LTTEManager.clearPlayersWithTNT();
                plugin.setLTTEState(LTTEState.ON);
                LTTEManager.startGame();
                sender.sendMessage("§aLTTE activé : toutes les procédures fonctionnent.");

                break;
            case "OFF":
                plugin.setLTTEState(LTTEState.OFF);
                LTTEManager.clearPlayersWithTNT();
                sender.sendMessage("§cLTTE désactivé : aucune procédure ne fonctionne.");
                break;
            case "STOP":
                plugin.setLTTEState(LTTEState.STOP);
                sender.sendMessage("§eLTTE en mode STOP : seules les prisons fonctionnent.");
                break;
            default:
                sender.sendMessage("§cÉtat invalide. Utilisez ON, OFF ou STOP.");
        }
        return true;
    }
}