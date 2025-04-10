package fr.salut.squidgame.component.commands;

import fr.salut.squidgame.component.ListenerManager.MiniGames.BaP.BaPState;
import fr.salut.squidgame.component.ListenerManager.MiniGames.BaP.BaPManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class BaPCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private static BaPState bapState = BaPState.OFF; // État initial

    public BaPCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static void setBaPState(BaPState newState) {
        if (newState == null) {
            throw new IllegalArgumentException("L'état BaPState ne peut pas être null.");
        }
        bapState = newState;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1 || !label.equalsIgnoreCase("bap")) {
            sender.sendMessage(ChatColor.RED + "Usage: /bap <ON|OFF|STOP>");
            return true;
        }

        switch (args[0].toUpperCase()) {
            case "ON":
                bapState = BaPState.ON;
                sender.sendMessage(ChatColor.GREEN + "BaP activé : le jeu est maintenant actif.");
                break;
            case "OFF":
                bapState = BaPState.OFF;
                BaPManager.getPlayersInPrison().clear();
                sender.sendMessage(ChatColor.RED + "BaP désactivé : le jeu est maintenant inactif.");
                break;
            case "STOP":
                bapState = BaPState.STOP;
                sender.sendMessage(ChatColor.YELLOW + "BaP en pause : le jeu est temporairement suspendu.");
                break;
            default:
                sender.sendMessage(ChatColor.RED + "État invalide. Utilisez ON, OFF ou STOP.");
        }
        return true;
    }

    public static BaPState getBaPState() {
        return bapState;
    }
}