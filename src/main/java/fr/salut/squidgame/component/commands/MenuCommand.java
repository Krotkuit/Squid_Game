package fr.salut.squidgame.component.commands;

import fr.salut.squidgame.menu.BooksMenu;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class MenuCommand {

    @Command("books")
    @CommandPermission("sqg.admins.commands.books")
    public void onBooks(Player sender){
        new BooksMenu(sender).open();
    }
}
