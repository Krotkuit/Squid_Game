package fr.salut.squidgame.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BooksMenu {

    private final Player owner;
    private final Inventory inventory;

    public BooksMenu(Player owner) {
        this.owner = owner;
        this.inventory = Bukkit.createInventory(null, 27, Component.text("Commands")); // 27 slots = InventorySize.NORMAL
        initContent();
    }

    private void initContent() {
        addBook(0, "Lobby");
        addBook(3, "123 Soleil");
        addBook(5, "Carrousel");
        addBook(7, "Balle aux prisonniers");
        addBook(9, "Bras d'Argent");
        addBook(11, "Loup Touche Touche Explosif");
        addBook(13, "Poule Renard Vipère");
        addBook(15, "Chaise Musicale");
        addBook(17, "Squid Game");
        addBook(19, "Croque Carotte");
        addBook(21, "Arc-en-Ciel");
        addBook(23, "Puissance 4");
        addBook(25, "Billes");
        addBook(26, "Find the Button");
    }

    private void addBook(int slot, String name) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        inventory.setItem(slot, item);
    }

    public void open() {
        owner.openInventory(inventory);
    }

    public static void giveBook(Books books, Player owner) {
        owner.addScoreboardTag("tempBook");
        World world = Bukkit.getWorld("world");
        if (world == null) world = owner.getWorld();
        world.getBlockAt(books.getLoc()).setType(Material.REDSTONE_BLOCK);
        world.getBlockAt(books.getLoc()).setType(Material.AIR);
        owner.closeInventory();
    }
}
