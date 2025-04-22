package fr.salut.squidgame.component.commands.games;

import fr.salut.squidgame.component.ListenerManager.MiniGames.CM.CMManager;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;

@Command("cm")
public class CMCommand {

    @Subcommand("load")
    void onLoad(Player sender){
        CMManager.putChairs();
    }

    @Subcommand("unload")
    void onRemove(Player sender){
        CMManager.removeChair();
    }
}
