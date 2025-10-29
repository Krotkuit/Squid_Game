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
      Bukkit.getLogger().warning("Le monde 'worlds/SquidGame/Carrousel' est introuvable.");
      return;
    }

    // Définition des 25 salles
    addSalle(1, world, 16.3, 65, 10.7, 18.7, 66.201, 2.3);
    addSalle(2, world, 22.3, 65, 14.7, 24.7, 66.201, 6.3);

    addSalle(3, world, 27.3, 65, 17.3, 35.7, 66.201, 19.7);
    addSalle(4, world, 31.3, 65, 23.3, 39.7, 66.201, 25.7);
    addSalle(5, world, 34.3, 65, 32.3, 42.7, 66.201, 34.7);
    addSalle(6, world, 35.3, 65, 40.3, 43.7, 66.201, 42.7);
    addSalle(7, world, 34.3, 65, 48.3, 42.7, 66.201, 50.7);
    addSalle(8, world, 31.3, 65, 57.3, 39.7, 66.201, 59.7);
    addSalle(9, world, 27.3, 65, 63.3, 35.7, 66.201, 65.7);

    addSalle(10, world, 24.7, 65, 68.3, 22.3, 66.201, 76.7);
    addSalle(11, world, 18.7, 65, 72.3, 16.3, 66.201, 80.7);
    addSalle(12, world, 9.7, 65, 75.3, 7.3, 66.201, 83.7);
    addSalle(13, world, 1.7, 65, 76.3, -0.7, 66.201, 84.7);
    addSalle(14, world, -6.3, 65, 75.3, -8.7, 66.201, 83.7);
    addSalle(15, world, -15.3, 65, 72.3, -17.7, 66.201, 80.7);
    addSalle(16, world, -21.3, 65, 68.3, -23.7, 66.201, 76.7);

    addSalle(17, world, -26.3, 65, 65.7, -34.7, 66.201, 63.3);
    addSalle(18, world, -30.3, 65, 59.7, -38.7, 66.201, 57.3);
    addSalle(19, world, -33.3, 65, 50.7, -41.7, 66.201, 48.3);
    addSalle(20, world, -34.3, 65, 42.7, -42.7, 66.201, 40.3);
    addSalle(21, world, -33.3, 65, 34.7, -41.7, 66.201, 32.3);
    addSalle(22, world, -30.3, 65, 25.7, -38.7, 66.201, 23.3);
    addSalle(23, world, -26.3, 65, 19.7, -34.7, 66.201, 17.3);

    addSalle(24, world, -23.7, 65, 14.7, -21.3, 66.201, 6.3);
    addSalle(25, world, -17.7, 65, 10.7, -15.3, 66.201, 2.3);
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