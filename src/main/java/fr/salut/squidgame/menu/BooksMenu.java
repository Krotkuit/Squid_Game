package fr.salut.squidgame.menu;

import fr.salut.squidgame.SquidGame;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BooksMenu {

    private final Player owner;
    private final Inventory inventory;

    public BooksMenu(Player owner) {
        this.owner = owner;
        this.inventory = Bukkit.createInventory(null, 27, Component.text("Commands")); // 27 slots = InventorySize.NORMAL
        initContent();
    }

    private void initContent() {

        for (BookData book : BookManager.books.values()) {
            if (book.slot() < 0 || book.slot() >= inventory.getSize()) continue;

            ItemStack item = new ItemStack(book.material());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.YELLOW + book.name());
            item.setItemMeta(meta);

            BookData.setBookKey(item, book.key());

            inventory.setItem(book.slot(), item);
        }
    }

    public void open() {
        owner.openInventory(inventory);
    }

    public static void giveBook(String key, Player owner) {

        BookData data = BookManager.books.get(key);

        if (data==null){
            SquidGame.getInstance().getLogger().warning("Aucun data trouvé pour la key: " + key);
            return;
        }

        owner.addScoreboardTag("tempBook");
        World world = Bukkit.getWorld("world");
        if (world == null) world = owner.getWorld();
        Location loc = new Location(world, data.x(), data.y(), data.z());

        if (loc == null) {
            SquidGame.getInstance().getLogger().warning("la location n'a pas été bien initialisée");
            return;
        }

        world.getBlockAt(loc).setType(Material.REDSTONE_BLOCK);
        world.getBlockAt(loc).setType(Material.AIR);
        owner.closeInventory();
    }
}
