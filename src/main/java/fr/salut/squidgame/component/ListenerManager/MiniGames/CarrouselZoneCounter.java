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
    addSalle(1, world, 16.3, 65, 2.3, 18.7, 66.21, 10.7);
    addSalle(2, world, 22.3, 65, 6.3, 24.7, 66.21, 14.7);
    addSalle(3, world, 32.7, 65, 16.7, 27.3, 66.21, 24.7);
    addSalle(4, world, 36.7, 65, 22.7, 31.3, 66.21, 30.7);
    addSalle(5, world, 39.7, 65, 29.3, 34.3, 66.21, 36.7);
    addSalle(6, world, 40.7, 65, 39.7, 35.3, 66.21, 44.7);
    addSalle(7, world, 39.7, 65, 47.7, 34.3, 66.21, 52.7);
    addSalle(8, world, 36.7, 65, 54.3, 31.3, 66.21, 60.7);
    addSalle(9, world, 32.7, 65, 60.3, 27.3, 66.21, 66.7);
    addSalle(10, world, 22.3, 65, 70.7, 24.7, 66.21, 78.7);
    addSalle(11, world, 16.3, 65, 74.7, 18.7, 66.21, 82.7);
    addSalle(12, world, 7.3, 65, 77.7, 9.7, 66.21, 85.7);
    addSalle(13, world, -0.7, 65, 78.7, 2.7, 66.21, 86.7);
    addSalle(14, world, -8.7, 65, 77.7, -6.3, 66.21, 84.7);
    addSalle(15, world, -15.3, 65, 74.7, -17.7, 66.21, 82.7);
    addSalle(16, world, -21.3, 65, 12.7, -23.7, 66.21, 20.7);
    addSalle(17, world, -31.7, 65, 60.3, -26.3, 66.21, 66.7);
    addSalle(18, world, -35.7, 65, 54.3, -30.3, 66.21, 60.7);
    addSalle(19, world, -38.7, 65, 45.3, -33.3, 66.21, 52.7);
    addSalle(20, world, -39.7, 65, 37.0, -34.3, 66.21, 44.7);
    addSalle(21, world, -39.7, 65, 31.7, -33.3, 66.21, 36.7);
    addSalle(22, world, -36.7, 65, 22.7, -30.3, 66.21, 30.7);
    addSalle(23, world, -31.7, 65, 16.7, -26.3, 66.21, 24.7);
    addSalle(24, world, -21.3, 65, 6.3, -23.7, 66.21, 14.7);
    addSalle(25, world, -15.3, 65, 9.7, -17.7, 66.21, 18.7);


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