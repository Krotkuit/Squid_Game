package fr.salut.squidgame.component.ListenerManager.MiniGames;

import fr.salut.squidgame.SquidGame;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class CarrouselZoneCounter implements Listener {
  @Getter
  private static CarrouselZoneCounter instance;
  private final SquidGame plugin;
  private final Map<Integer, Cuboid> salles = new HashMap<>();

  // Constructeur principal
  public CarrouselZoneCounter(SquidGame plugin) {
    this.plugin = plugin;
    instance = this; // Initialisation de l'instance statique
    initSalles();
  }

  // Supprimez ce constructeur si inutile
  public CarrouselZoneCounter() {
    this.plugin = null; // Initialisation pour éviter l'erreur
    initSalles();
  }

  private void initSalles() {
    World world = Bukkit.getWorld("world");
    if (world == null) {
      throw new IllegalStateException("Le monde 'world' est introuvable.");
    }

    // Définition des 25 salles
    addSalle(1, world, -110.7, -59, -23.7, -108.3, -57.79, -18.3);
    addSalle(2, world, -104.7, -59, -19.7, -102.3, -57.79, -14.3);
    addSalle(3, world, -94.3, -59, -9.3, -99.7, -57.79, -11.7);
    addSalle(4, world, -90.3, -59, -3.3, -95.7, -57.79, -5.7);
    addSalle(5, world, -87.3, -59, 3.3, -91.7, -57.79, 5.7);
    addSalle(6, world, -86.3, -59, 13.7, -90.7, -57.79, 11.3);
    addSalle(7, world, -87.3, -59, 21.7, -91.7, -57.79, 19.3);
    addSalle(8, world, -90.3, -59, 28.3, -95.7, -57.79, 30.7);
    addSalle(9, world, -94.3, -59, 34.3, -99.7, -57.79, 36.7);
    addSalle(10, world, -104.7, -59, 44.7, -102.3, -57.79, 39.3);
    addSalle(11, world, -110.7, -59, 48.7, -108.3, -57.79, 43.3);
    addSalle(12, world, -119.7, -59, 51.7, -117.3, -57.79, 46.3);
    addSalle(13, world, -127.7, -59, 52.7, -125.3, -57.79, 47.3);
    addSalle(14, world, -135.7, -59, 51.7, -133.3, -57.79, 46.3);
    addSalle(15, world, -142.3, -59, 48.7, -144.7, -57.79, 43.3);
    addSalle(16, world, -148.3, -59, -44.7, -150.7, -57.79, 39.3);
    addSalle(17, world, -158.7, -59, 34.3, -153.3, -57.79, 36.7);
    addSalle(18, world, -162.7, -59, 28.3, -157.3, -57.79, 30.7);
    addSalle(19, world, -165.7, -59, 19.3, -160.3, -57.79, 21.7);
    addSalle(20, world, -166.7, -59, 11.0, -161.3, -57.79, 13.7);
    addSalle(21, world, -166.7, -59, 5.7, -160.3, -57.79, 3.3);
    addSalle(22, world, -163.7, -59, -3.3, -157.3, -57.79, -5.7);
    addSalle(23, world, -158.7, -59, -9.3, -153.3, -57.79, -11.7);
    addSalle(24, world, -148.3, -59, -19.7, -150.7, -57.79, -14.3);
    addSalle(25, world, -142.3, -59, -237.0, -144.7, -57.79, -18.3);
  }

  private void addSalle(int id, World world, double x1, double y1, double z1, double x2, double y2, double z2) {
    Location l1 = new Location(world, x1, y1, z1);
    Location l2 = new Location(world, x2, y2, z2);
    salles.put(id, new Cuboid(l1, l2));
  }

  public Map<Integer, Integer> countPlayersPerRoom() {
    Map<Integer, Integer> result = new HashMap<>();
    for (int id : salles.keySet()) {
      result.put(id, 0);
    }

    for (Player player : Bukkit.getOnlinePlayers()) {
      Team team = Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(player.getName());
      if (team != null && team.getName().equals("joueur")) {
        Location playerLocation = player.getLocation();
        for (Map.Entry<Integer, Cuboid> entry : salles.entrySet()) {
          if (entry.getValue().isInside(playerLocation)) {
            result.put(entry.getKey(), result.get(entry.getKey()) + 1);
            break;
          }
        }
      }
    }

    return result;
  }

  private static class Cuboid {
    private final double minX, maxX;
    private final double minY, maxY;
    private final double minZ, maxZ;
    private final World world;

    public Cuboid(Location l1, Location l2) {
      this.world = l1.getWorld();
      this.minX = Math.min(l1.getX(), l2.getX());
      this.maxX = Math.max(l1.getX(), l2.getX());
      this.minY = Math.min(l1.getY(), l2.getY());
      this.maxY = Math.max(l1.getY(), l2.getY());
      this.minZ = Math.min(l1.getZ(), l2.getZ());
      this.maxZ = Math.max(l1.getZ(), l2.getZ());
    }

    public boolean isInside(Location loc) {
      if (!loc.getWorld().equals(world)) return false;

      return loc.getX() >= minX && loc.getX() <= maxX &&
          loc.getY() >= minY && loc.getY() <= maxY &&
          loc.getZ() >= minZ && loc.getZ() <= maxZ;
    }
  }
}