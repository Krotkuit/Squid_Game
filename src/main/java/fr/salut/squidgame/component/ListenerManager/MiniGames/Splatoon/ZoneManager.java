package fr.salut.squidgame.component.ListenerManager.MiniGames.Splatoon;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Bukkit.getLogger;

public class ZoneManager {
  private FileConfiguration config;
  private File configFile;
  @Getter
  private static final Map<String, ZoneData> zones = new HashMap<>();

  public void loadSplatoonZones(boolean reload) {
    if (reload) {
      zones.clear(); // vide les anciennes zones avant de recharger
      config = YamlConfiguration.loadConfiguration(configFile); // recharge le fichier
    }
    loadZones(); // appelle la méthode privée pour remplir la Map
  }

  public ZoneManager(JavaPlugin plugin) {
    configFile = new File(plugin.getDataFolder(), "SplatoonGameZone.yml");
    if (!configFile.exists()) plugin.saveResource("SplatoonGameZone.yml", false);
    config = YamlConfiguration.loadConfiguration(configFile);
    loadZones();
  }

  private void loadZones() {
    for (String zoneName : config.getKeys(false)) {
      ConfigurationSection section = config.getConfigurationSection(zoneName);
      if (section == null) continue;

      String worldName = section.getString("world");
      if (worldName == null) {
        getLogger().warning("Zone " + zoneName + " n'a pas de world défini !");
        continue;
      }

      // Récupère le monde Bukkit (Multiverse fonctionne)
      World world = Bukkit.getWorld(worldName);
      if (world == null) {
        getLogger().warning("Le monde '" + worldName + "' pour la zone " + zoneName + " n'est pas chargé !");
        continue;
      }

      List<Integer> xyz1 = section.getIntegerList("xyz1");
      List<Integer> xyz2 = section.getIntegerList("xyz2");

      if (xyz1.size() != 3 || xyz2.size() != 3) {
        getLogger().warning("Zone " + zoneName + " a des coordonnées invalides !");
        continue;
      }

      zones.put(zoneName, new ZoneData(world, xyz1, xyz2));
      getLogger().info("Zone chargée : " + zoneName + " dans le monde " + world.getName());
    }

    getLogger().info("Nombre total de zones chargées : " + zones.size());
  }


  public static boolean isInsideZone(String zoneName, Location loc) {
    if (!zones.containsKey(zoneName)) return false;
    ZoneData z = zones.get(zoneName);

    // Vérifie que le nom du monde correspond
    if (!loc.getWorld().equals(z.world)) return false;

    int x1 = Math.min(z.xyz1[0], z.xyz2[0]);
    int y1 = Math.min(z.xyz1[1], z.xyz2[1]);
    int z1 = Math.min(z.xyz1[2], z.xyz2[2]);
    int x2 = Math.max(z.xyz1[0], z.xyz2[0]);
    int y2 = Math.max(z.xyz1[1], z.xyz2[1]);
    int z2 = Math.max(z.xyz1[2], z.xyz2[2]);

    int px = loc.getBlockX();
    int py = loc.getBlockY();
    int pz = loc.getBlockZ();

    return px >= x1 && px <= x2
        && py >= y1 && py <= y2
        && pz >= z1 && pz <= z2;
  }


  public static class ZoneData {
    public final World world;  // changé de String à World
    public final int[] xyz1;
    public final int[] xyz2;

    public ZoneData(World world, List<Integer> xyz1, List<Integer> xyz2) {
      this.world = world;
      this.xyz1 = xyz1.stream().mapToInt(i -> i).toArray();
      this.xyz2 = xyz2.stream().mapToInt(i -> i).toArray();
    }
  }

}
