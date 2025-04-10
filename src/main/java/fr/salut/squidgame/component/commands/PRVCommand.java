package fr.salut.squidgame.component.commands;

import fr.salut.squidgame.Main;
import fr.salut.squidgame.component.ListenerManager.MiniGames.PRV.PRVListener;
import fr.salut.squidgame.component.ListenerManager.MiniGames.PRV.PRVState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PRVCommand implements CommandExecutor {
    private final Main plugin;

    public PRVCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1 || !label.equalsIgnoreCase("prv")) {
            sender.sendMessage("§cUsage: /prv <ON|OFF|STOP>");
            return true;
        }

        switch (args[0].toUpperCase()) {
            case "ON":
                plugin.setPrvState(PRVState.ON);
                sender.sendMessage("§aPRV activé : toutes les procédures fonctionnent.");
                break;
            case "OFF":
                plugin.setPrvState(PRVState.OFF);
                PRVListener.resetPrisoners();
                sender.sendMessage("§cPRV désactivé : aucune procédure ne fonctionne.");
                break;
            case "STOP":
                plugin.setPrvState(PRVState.STOP);
                sender.sendMessage("§ePRV en mode STOP : seules les prisons fonctionnent.");
                break;
            default:
                sender.sendMessage("§cÉtat invalide. Utilisez ON, OFF ou STOP.");
        }
        return true;
    }
}