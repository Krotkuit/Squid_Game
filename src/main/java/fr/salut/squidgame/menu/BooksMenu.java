package fr.salut.squidgame.menu;

import dev.xernas.menulib.Menu;
import dev.xernas.menulib.utils.InventorySize;
import dev.xernas.menulib.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooksMenu extends Menu {


    public BooksMenu(Player owner) {
        super(owner);
    }

    @Override
    public @NotNull String getName() {
        return "Commands";
    }

    @Override
    public @NotNull InventorySize getInventorySize() {
        return InventorySize.NORMAL;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {}

    @Override
    public @NotNull Map<Integer, ItemStack> getContent() {
        Map<Integer, ItemStack> map = new HashMap<>();

        map.put(9, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("123 Soleil");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.SOLEIL_GAME);
        }));

        map.put(11, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("Lobby");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.LOBBY);
        }));

        map.put(13, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("Carrousel");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.CARROUSEL);
        }));

        map.put(15, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("Squid Game");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.SQUID_GAME);
        }));

        map.put(17, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("Bras d'Argent");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.BRAS_DARGENT);
        }));

        return map;
    }

    private void giveBook (Books books){
        getOwner().addScoreboardTag("tempBook");
        World world = Bukkit.getWorld("world");
        if (world==null) world = getOwner().getWorld();
        world.getBlockAt(books.getLoc()).setType(Material.REDSTONE_BLOCK);
        world.getBlockAt(books.getLoc()).setType(Material.AIR);
        getOwner().removeScoreboardTag("tempBook");
        getOwner().closeInventory();
    }
}