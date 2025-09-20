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

        String name = clicked.getItemMeta() != null ? clicked.getItemMeta().getDisplayName() : "";
        if (name.isEmpty()) return;

        switch (name) {
            case "Lobby" -> BooksMenu.giveBook(Books.LOBBY, player);
            case "123 Soleil" -> BooksMenu.giveBook(Books.SOLEIL_GAME, player);
            case "Carrousel" -> BooksMenu.giveBook(Books.CARROUSEL, player);
            case "Balle aux prisonniers" -> BooksMenu.giveBook(Books.BaP, player);
            case "Bras d'Argent" -> BooksMenu.giveBook(Books.BRAS_DARGENT, player);
            case "Loup Touche Touche Explosif" -> BooksMenu.giveBook(Books.LTTE, player);
            case "Poule Renard Vipère" -> BooksMenu.giveBook(Books.PRV, player);
            case "Chaise Musicale" -> BooksMenu.giveBook(Books.CM, player);
            case "Squid Game" -> BooksMenu.giveBook(Books.SQUID_GAME, player);
            case "Croque Carotte" -> BooksMenu.giveBook(Books.CROQUE_CAROTTE, player);
            case "Arc-en-Ciel" -> BooksMenu.giveBook(Books.ARC_EN_CIEL, player);
            case "Puissance 4" -> BooksMenu.giveBook(Books.PUISSANCE_4, player);
            case "Billes" -> BooksMenu.giveBook(Books.BILLES, player);
            case "Find the Button" -> BooksMenu.giveBook(Books.FIND_THE_BUTTON, player);
            case "Corde à Sauter" -> BooksMenu.giveBook(Books.CORDE_A_SAUTER, player);
            case "Squid Game Aerien" -> BooksMenu.giveBook(Books.SQUID_GAME_AERIEN, player);
        }
    }
}
