package fr.salut.squidgame.component.commands;

import fr.salut.squidgame.SquidGame;
import lombok.Getter;
import revxrsal.commands.bukkit.BukkitCommandHandler;

public class CommandManager {
    @Getter
    static CommandManager instance;
    @Getter
    static BukkitCommandHandler handler;

    public CommandManager() {
        instance = this;
        SquidGame plugin = SquidGame.getInstance();
        handler = BukkitCommandHandler.create(plugin);
    }
}