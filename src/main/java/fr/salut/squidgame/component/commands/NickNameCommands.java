package fr.salut.squidgame.component.commands;

import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;

public class NickNameCommands {
    @Command("name set")
    @Description("change le nom du joueur")
    private void onNameSet (Player sender, @Named("name") String name){
        if (name==null){
            sender.sendMessage("name null");
            return;
        }
        if (sender.getDisplayName().equals(name)){
            sender.sendMessage("§4Vous portez déjà ce nom");
            return;
        }
        sender.setDisplayName(name);
        sender.setPlayerListName(name);
        sender.sendMessage("Votre nom a été changer en : " + name);
    }

    @Command("name reset")
    @Description("change le nom du joueur sur celui de base")
    private void onNameReset (Player sender){
        if (!sender.getDisplayName().equals(sender.getName())){
            sender.setDisplayName(sender.getName());
            sender.setPlayerListName(sender.getName());
            sender.sendMessage("Votre nom a été remis a celui de base :" + sender.getName());
            return;
        }
        sender.sendMessage("§4Votre nom est déjà définit sur celui de base ");
    }
}
