package fr.salut.squidgame.component.commands;

import fr.salut.squidgame.component.ListenerManager.MoveDetectListener;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;

@Command({"movedetection", "mvd"})
public class MoveDetectionCmd {

    boolean enable;

    @DefaultFor("~")
    public void bapDefault(Player sender){
        sender.sendMessage("§cÉtat invalide. Utilisez true ou false.");
    }

    @Subcommand("true")
    public void mvdTrue(Player sender){
        sender.sendMessage("§aDétection de mouvements activée: Oui");
        MoveDetectListener.setEnabled(enable);
        enable = true;
    }

    @Subcommand("false")
    public void mvdFalse(Player sender){
        sender.sendMessage("§aDétection de mouvements activée: Non");
        MoveDetectListener.setEnabled(enable);
        enable = false;
    }
}
