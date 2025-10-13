package fr.salut.squidgame.component.commands;

import fr.salut.squidgame.SquidGame;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("sgreload")
public class ConfigsCommand {

    @Subcommand("books")
    @CommandPermission("sqg.admins.commands.sgreload.books")
    public void onSGReloadBooks(){
        SquidGame.getInstance().getBooksManager().loadBooks(true);
    }

    @Subcommand("spec")
    @CommandPermission("sqg.admins.commands.sgreload.spec")
    public void onSGReloadSpec(){
        SquidGame.getInstance().getGameZoneManager().loadSpectator(true);
    }
}
