package fr.salut.squidgame.component.commands.games;

import fr.salut.squidgame.component.ListenerManager.MiniGames.CM.CMManager;
import org.bukkit.command.CommandSender;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;

@Command("cm")
public class CMCommand {

    @Subcommand("load")
    void onLoad(CommandSender sender){
        CMManager.putChairs();
    }

    @Subcommand("unload")
    void onRemove(CommandSender sender){
        CMManager.removeChair();
    }
}
