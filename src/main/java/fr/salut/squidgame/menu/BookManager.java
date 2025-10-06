package fr.salut.squidgame.menu;


import fr.salut.squidgame.SquidGame;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class BookManager {

    private final JavaPlugin plugin;
    public static final Map<String, BookData> books = new HashMap<>();

    public BookManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadBooks(boolean reload) {
        if (reload) plugin.reloadConfig();

        File file = new File(plugin.getDataFolder(), "books.yml");
        if (!file.exists()) plugin.saveResource("books.yml", false);

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("books");
        books.clear();

        if (section == null) {
            plugin.getLogger().warning("Aucune section 'books' trouvée dans books.yml !");
            return;
        }

        for (String key : section.getKeys(false)) {
            ConfigurationSection s = section.getConfigurationSection(key);
            if (s == null) continue;

            String name = s.getString("name", key);
            double x = s.getDouble("x");
            double y = s.getDouble("y");
            double z = s.getDouble("z");
            int slot = s.getInt("slot", -1);
            String matName = s.getString("material", "BOOK");
            Material material = Material.matchMaterial(matName);
            if (material == null) material = Material.BOOK;

            BookData book = new BookData(key, name, x, y, z, slot, material);
            books.put(key, book);
        }

        plugin.getLogger().info("Books chargés: " + books.size());
    }

    public Collection<BookData> getAllBooks() {
        return books.values();
    }

    public BookData getBook(String key) {
        return books.get(key);
    }
}

/**
 * Représente un livre dans la config
 */
record BookData(String key, String name, double x, double y, double z, int slot, Material material) {

    @Override
    public String toString() {
        return String.format("[%s] %s (x=%.1f, y=%.1f, z=%.1f, slot=%d, mat=%s)", key, name, x, y, z, slot, material);
    }

    public static ItemStack setBookKey(ItemStack item, String key) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        NamespacedKey dataKey = new NamespacedKey(SquidGame.getInstance(), "book_id");
        meta.getPersistentDataContainer().set(dataKey, PersistentDataType.STRING, key);

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Récupère la data personnalisée depuis un ItemStack
     */
    public static String getBookKey(ItemStack item) {
        if (!item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();
        NamespacedKey dataKey = new NamespacedKey(SquidGame.getInstance(), "book_id");
        PersistentDataContainer container = meta.getPersistentDataContainer();

        return container.get(dataKey, PersistentDataType.STRING);
    }
}


