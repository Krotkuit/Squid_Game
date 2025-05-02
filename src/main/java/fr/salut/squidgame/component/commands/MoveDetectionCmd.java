package fr.salut.squidgame.component.commands;

import fr.salut.squidgame.component.ListenerManager.MoveDetectListener;
import org.bukkit.command.CommandSender;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;

@Command({"movedetection", "mvd"})
public class MoveDetectionCmd {

    boolean enable;

    @DefaultFor("~")
    public void bapDefault(CommandSender sender){
        sender.sendMessage("§cÉtat invalide. Utilisez true ou false.");
    }

    @Subcommand("true")
    public void mvdTrue(CommandSender sender){
        sender.sendMessage("§aDétection de mouvements activée: Oui");
        enable = true;
        MoveDetectListener.setEnabled(enable);
    }

    @Subcommand("false")
    public void mvdFalse(CommandSender sender){
        sender.sendMessage("§aDétection de mouvements activée: Non");
        enable = false;
        MoveDetectListener.setEnabled(enable);
    }
}
