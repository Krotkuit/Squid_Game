package fr.salut.squidgame.component.commands;

import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.menu.BookManager;
import fr.salut.squidgame.menu.BooksMenu;
import fr.skytasul.glowingentities.GlowingEntities;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("books")
public class MenuCommand {

    @Subcommand("open")
    public void onBooks(Player sender){
        new BooksMenu(sender).open();
        //GlowingEntities glowingEntities = new GlowingEntities(SquidGame.getInstance());
        //try {
        //    glowingEntities.setGlowing(sender, sender, ChatColor.DARK_AQUA);
        //} catch (ReflectiveOperationException e) {
        //    throw new RuntimeException(e);
        //}
    }

    @Subcommand("reload")
    @CommandPermission("sqg.admins.commands.books.reload")
    public void onBooksRelaod(Player sender){
        SquidGame.getInstance().getBooksManager().loadBooks(true);
    }
}
