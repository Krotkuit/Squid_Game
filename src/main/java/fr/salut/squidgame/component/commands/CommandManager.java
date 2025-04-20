package fr.salut.squidgame.component.commands;

import fr.salut.squidgame.SquidGame;
import revxrsal.commands.bukkit.BukkitCommandHandler;

public class CommandManager {
    public static CommandManager instance;
    public static BukkitCommandHandler handler;

    public CommandManager() {
        instance = this;
        SquidGame plugin = SquidGame.getInstance();
        handler = BukkitCommandHandler.create(plugin);
    }
}