package fr.salut.squidgame.component.commands;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;

@Command("tchat")
public class TchatCommad implements Listener {

    public static boolean tchatLock;

    @Subcommand("lock")
    public void onLockTchat(CommandSender sender){
        tchatLock = true;

        if (sender instanceof Player){
            sender.sendMessage("§aTchat bloqué pour les joueurs");
        }
    }

    @Subcommand("unlock")
    public void onUnlockTchat(CommandSender sender){
        tchatLock = false;

        if (sender instanceof Player){
            sender.sendMessage("§aTchat débloqué pour les joueurs");
        }
    }

    @EventHandler
    public void onPlayerTchatUse(AsyncChatEvent e){
        Player sender = e.getPlayer();

        if (!sender.isOp() && tchatLock){
            sender.sendMessage("§cTchat Bloqué !");
            e.setCancelled(true);
        }
    }
}
