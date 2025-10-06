package fr.salut.squidgame.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BookMenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (!event.getView().getOriginalTitle().equalsIgnoreCase("Commands")) return; // ⚠️ si getTitle() casse, on change par inv.getHolder()

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() != Material.BOOK) return;

        String key = BookData.getBookKey(clicked);

        if (key==null) return;

        BooksMenu.giveBook(key, player);
    }
}
