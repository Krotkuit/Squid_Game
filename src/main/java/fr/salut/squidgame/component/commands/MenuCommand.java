package fr.salut.squidgame.component.commands;

import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.menu.BooksMenu;
import fr.skytasul.glowingentities.GlowingEntities;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class MenuCommand {
    @Command("books")
    @CommandPermission("sqg.admins.commands.books")
    public void onBooks(Player sender){
        new BooksMenu(sender).open();
        GlowingEntities glowingEntities = new GlowingEntities(SquidGame.getInstance());
        try {
            glowingEntities.setGlowing(sender, sender, ChatColor.DARK_AQUA);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
