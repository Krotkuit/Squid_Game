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

        map.put(0, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("Lobby");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.LOBBY);
        }));

        map.put(3, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("123 Soleil");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.SOLEIL_GAME);
        }));

        map.put(5, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("Carrousel");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.CARROUSEL);
        }));

        map.put(7, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("Balle aux prisonniers");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.BaP);
        }));

        map.put(9, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("Bras d'Argent");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.BRAS_DARGENT);
        }));

        map.put(11, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("Loup Touche Touche Explosif");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.LTTE);
        }));

        map.put(13, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("Poule Renard Vipère");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.PRV);
        }));

        map.put(15, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("Chaise Musicale");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.CM);
        }));

        map.put(17, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("Squid Game");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.SQUID_GAME);
        }));
        map.put(19, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("Croque Carotte");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.CROQUE_CAROTTE);
        }));
        map.put(21, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("Arc-en-Ciel");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.ARC_EN_CIEL);
        }));
        map.put(23, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("Puissance 4");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.PUISSANCE_4);
        }));
        map.put(25, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("Billes");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.BILLES);
        }));
        map.put(26, new ItemBuilder(this, Material.BOOK, itemMeta ->{
            itemMeta.setDisplayName("Find the Button");
            List<String> lore = new ArrayList<>();
            lore.add("§ldonne le livre du " + itemMeta.getDisplayName());
            itemMeta.setLore(lore);
        }).setOnClick(inventoryClickEvent ->{
            giveBook(Books.FIND_THE_BUTTON);
        }));

        return map;
    }

    private void giveBook (Books books){
        getOwner().addScoreboardTag("tempBook");
        World world = Bukkit.getWorld("world");
        if (world==null) world = getOwner().getWorld();
        world.getBlockAt(books.getLoc()).setType(Material.REDSTONE_BLOCK);
        world.getBlockAt(books.getLoc()).setType(Material.AIR);
        getOwner().closeInventory();
    }
}