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
    World world = Bukkit.getWorld("worlds/SquidGame/Carrousel");
    if (world == null) {
      throw new IllegalStateException("Le monde 'worlds/SquidGame/Carrousel' est introuvable.");
    }

    // Définition des 25 salles
    addSalle(1, world, 16.3, 65, 2.2, 18.9, 66.21, 12.7);
    addSalle(2, world, 22.3, 65, 6.2, 24.8, 66.21, 16.7);
    addSalle(3, world, 25.3, 65, 17.2, 35.8, 66.21, 19.8);
    addSalle(4, world, 29.3, 65, 23.2, 39.8, 66.21, 25.8);
    addSalle(5, world, 32.3, 65, 32.2, 42.8, 66.21, 34.8);
    addSalle(6, world, 33.3, 65, 40.2, 43.8, 66.21, 42.8);
    addSalle(7, world, 32.3, 65, 48.2, 42.8, 66.21, 50.8);
    addSalle(8, world, 29.3, 65, 57.2, 39.8, 66.21, 59.8);
    addSalle(9, world, 25.3, 65, 63.2, 35.8, 66.21, 65.8);
    addSalle(10, world, 22.3, 65, 76.9, 24.8, 66.21, 66.3);
    addSalle(11, world, 16.3, 65, 80.9, 18.8, 66.21, 70.3);
    addSalle(12, world, 7.3, 65, 83.9, 9.8, 66.21, 73.3);
    addSalle(13, world, -0.9, 65, 84.9, 1.8, 66.21, 74.3);
    addSalle(14, world, -8.9, 65, 83.9, -6.2, 66.21, 73.3);
    addSalle(15, world, -17.9, 65, 80.9, -15.2, 66.21, 70.3);
    addSalle(16, world, -23.9, 65, 76.9, -21.2, 66.21, 66.3);
    addSalle(17, world, -34.9, 65, 63.2, -24.3, 66.21, 65.8);
    addSalle(18, world, -38.9, 65, 57.2, -28.3, 66.21, 59.8);
    addSalle(19, world, -41.9, 65, 48.2, -31.3, 66.21, 50.8);
    addSalle(20, world, -42.9, 65, 40.2, -32.3, 66.21, 42.8);
    addSalle(21, world, -41.9, 65, 32.2, -31.3, 66.21, 34.8);
    addSalle(22, world, -38.9, 65, 23.2, -28.3, 66.21, 25.8);
    addSalle(23, world, -34.9, 65, 17.2, -24.3, 66.21, 19.8);
    addSalle(24, world, -21.3, 65, 16.7, -23.9, 66.21, 6.2);
    addSalle(25, world, -17.9, 65, 2.2, -15.3, 66.21, 12.7);


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