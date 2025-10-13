package fr.salut.squidgame.component.ListenerManager.GameZone;

import fr.salut.squidgame.component.commands.EpreuveCommand;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class GameZoneManager implements Listener {

  public static final Map<String, Zone> gameZones = new HashMap<>();

  private final JavaPlugin plugin;

  public void loadSpectator(boolean reload) {
    if (reload) {
      plugin.reloadConfig();
      gameZones.clear();
    }

    File file = new File(plugin.getDataFolder(), "specConfig.yml");
    if (!file.exists()) plugin.saveResource("specConfig.yml", false);

    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    ConfigurationSection section = config.getConfigurationSection("zone");

    if (section == null) {
      plugin.getLogger().warning("Aucune section 'zone' trouvée dans specConfig.yml !");
      return;
    }

    for (String key : section.getKeys(false)) {
      ConfigurationSection s = section.getConfigurationSection(key);
      if (s == null) continue;

      World world = Bukkit.getWorld(s.getString("world", key));
      if (world == null){
        plugin.getLogger().warning("World : " + s.getString("world", key) + " not found, no spectator set to this world!");
        continue;
      }

      Zone zone = new Zone();

      for (String subKey : s.getKeys(false)){

        ConfigurationSection subSection = s.getConfigurationSection(subKey);

        if (subSection == null) continue;

        if (subKey.startsWith("sub")) {

          List<Double> xyz1 = subSection.getDoubleList("xyz1");
          List<Double> xyz2 = subSection.getDoubleList("xyz2");

          zone.addSubZone(
                  new Location(world, xyz1.get(0), xyz1.get(1), xyz1.get(2)),
                  new Location(world, xyz2.get(0), xyz2.get(1), xyz2.get(2))
          );
        }

        if (subKey.startsWith("cyl")) {

          List<Double> center = subSection.getDoubleList("center");
          double rad = subSection.getDouble("radius");
          double minY = subSection.getDouble("minY");
          double maxY = subSection.getDouble("maxY");

          zone.addSubZone(new Location(world, center.get(0), center.get(1), center.get(2)), rad, minY, maxY);
        }
      }

      gameZones.put(key, zone);

      //plugin.getLogger().info("zone create for : " + key + ", subzones : " + zone.subZones.toArray().length);
    }

    plugin.getLogger().info("zone created : " + gameZones.size());
  }

  public GameZoneManager(JavaPlugin plugin) {

    this.plugin = plugin;

    loadSpectator(false);

    // Définir les zones pour chaque épreuve
    Zone StairsZone = new Zone();
    StairsZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 216.3, -60, 25.7),
        new Location(Bukkit.getWorld("world"), 279.7, 12.201, -36.7)
    );
    gameZones.put("Escaliers", StairsZone);

    Zone zoneFtB = new Zone();
    // [ Aire de jeu ] //
    zoneFtB.addSubZone(
        new Location(Bukkit.getWorld("world"), 144.3, -35, 265.3),
        new Location(Bukkit.getWorld("world"), 223.7, 43.21, 504.7)
    );
    zoneFtB.addSubZone(
        new Location(Bukkit.getWorld("world"), 144.7, -12.79, 401.3),
        new Location(Bukkit.getWorld("world"), 138.3, -19, 408.7)
    );
    zoneFtB.addSubZone(
        new Location(Bukkit.getWorld("world"), 132.3, -15.79, 405.3),
        new Location(Bukkit.getWorld("world"), 138.7, -19, 407.7)
    );
    zoneFtB.addSubZone(
        new Location(Bukkit.getWorld("world"), 132.3, -19, 424.7),
        new Location(Bukkit.getWorld("world"), 136.7, -15.79, 405.3)
    );
    zoneFtB.addSubZone(
        new Location(Bukkit.getWorld("world"), 136.7, -19, 421.3),
        new Location(Bukkit.getWorld("world"), 127.3, -16.79, 424.7)
    );
    zoneFtB.addSubZone(
        new Location(Bukkit.getWorld("world"), 127.7, -19, 418.3),
        new Location(Bukkit.getWorld("world"), 120.3, -15.79, 427.7)
    );
    zoneFtB.addSubZone(
        new Location(Bukkit.getWorld("world"), 125.7, -19, 429.7),
        new Location(Bukkit.getWorld("world"), 122.3, -15.79, 416.3)
    );
    zoneFtB.addSubZone(
        new Location(Bukkit.getWorld("world"), 120.3, -19, 418.3),
        new Location(Bukkit.getWorld("world"), 127.7, -15.79, 427.7)
    );zoneFtB.addSubZone(
        new Location(Bukkit.getWorld("world"), 121.3, -19, 417.3),
        new Location(Bukkit.getWorld("world"), 126.7, -15.79, 428.7)
    );
    gameZones.put("Find_the_Button", zoneFtB);

    Zone CroqueCarotteZone = new Zone();
    CroqueCarotteZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 22.3, -61, -116.3),
        new Location(Bukkit.getWorld("world"), 63.7, -43.79, -156.7)
    );
    CroqueCarotteZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 44.7, -60, -97.3),
        new Location(Bukkit.getWorld("world"), 40.3, -53.79, -116.3)
    );
    CroqueCarotteZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 67.7, -50, -165.7),
        new Location(Bukkit.getWorld("world"), 11.3, -33.79, -107.3)
    );
    CroqueCarotteZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 47.7, -60, -89.3),
        new Location(Bukkit.getWorld("world"), 37.3, -56.79, -94.7)
    );
    CroqueCarotteZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 36.3, -60, -93.7),
        new Location(Bukkit.getWorld("world"), 48.7, -56.79, -90.3)
    );
    CroqueCarotteZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 46.7, -60, -95.7),
        new Location(Bukkit.getWorld("world"), 38.3, -56.79, -88.3)
    );
    CroqueCarotteZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 44.7, -60, -94.3),
        new Location(Bukkit.getWorld("world"), 39.3, -57.79, -97.7)
    );
    gameZones.put("Croque_Carotte", CroqueCarotteZone);

    Zone biscuitTeamZone = new Zone();
    biscuitTeamZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -40, 60, -40),
        new Location(Bukkit.getWorld("world"), -20, 80, -20)
    );
    gameZones.put("Biscuit_Team", biscuitTeamZone);

    Zone SquidGameZone = new Zone();
    SquidGameZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -281.7, -60, -92.7),
        new Location(Bukkit.getWorld("world"), -228.3, -39.79, -8.3)
    );
    SquidGameZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -281.7, -150, -92.7),
        new Location(Bukkit.getWorld("world"), -228.3, -43.79, -8.3)
    );
    SquidGameZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -228.7, -60, -54.7),
        new Location(Bukkit.getWorld("world"), -220.3, -56.79, -46.3)
    );
    SquidGameZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -225.7, -60, -44.3),
        new Location(Bukkit.getWorld("world"), -221.3, -56.79, -55.7)
    );
    SquidGameZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -222.3, -60, -56.7),
        new Location(Bukkit.getWorld("world"), -225.7, -56.79, -44.3)
    );
    SquidGameZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -226.7, -60, -45.3),
        new Location(Bukkit.getWorld("world"), -221.3, -56.79, -55.7)
    );

    gameZones.put("Squid_Game", SquidGameZone);

    Zone SalleBlancheZone = new Zone();
    SalleBlancheZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -221.7, -60, -41.7),
        new Location(Bukkit.getWorld("world"), -181.3, -50.79, -15.3)
    );
    SalleBlancheZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -146.3, -60, 216.3),
        new Location(Bukkit.getWorld("world"), -154.7, -57.79, 226.7)
    );

    gameZones.put("Salle_Blanche", SalleBlancheZone);

  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    handlePlayerMovement(event.getPlayer(), event.getFrom(), event.getTo(), event);
  }
  @EventHandler
  public void onPlayerTeleport(PlayerTeleportEvent event) {
    handlePlayerMovement(event.getPlayer(), event.getFrom(), event.getTo(), event);
  }

  private void handlePlayerMovement(Player player, Location fromLocation, Location toLocation, Cancellable event) {
    if (toLocation == null) return;

    String currentEpreuve = EpreuveCommand.getEpreuve();
    Zone zone = gameZones.get(currentEpreuve);

    if (zone == null) return;


//    Bukkit.getLogger().info("toLocation : " + toLocation);
//    Bukkit.getLogger().info("zone : " + zone.subZones);

    boolean isInsideZone = zone.isInside(toLocation);
//    Bukkit.getLogger().info("isInsideZone : " + isInsideZone);
    if (!isInsideZone) {
      boolean wasInsideZone = zone.isInside(fromLocation);
//      Bukkit.getLogger().info("wasInsideZone : " + wasInsideZone);
      // Gestion des spectateurs
      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        if (onlinePlayer.getGameMode() == GameMode.SPECTATOR
            && onlinePlayer.getSpectatorTarget() == player) {
          var team = onlinePlayer.getScoreboard().getEntryTeam(onlinePlayer.getName());
          if (team != null && team.getName().equalsIgnoreCase("mort")) {
            onlinePlayer.setSpectatorTarget(null);
            onlinePlayer.sendMessage("§cVous ne pouvez plus observer ce joueur car il a quitté la zone autorisée.");
          }
        }
      }

      if (!player.getScoreboardTags().contains("joueur")) return;

      if (wasInsideZone) {
        event.setCancelled(true);
        player.sendActionBar("§cVous ne pouvez pas quitter la zone !");
      } else {
        Location zoneCenter = zone.getCenter();
        if (zoneCenter != null) {
//          Bukkit.getLogger().info("worlds/SquidGame/" + EpreuveCommand.getEpreuve());
//          Bukkit.getLogger().info("zoneCenter.getWorld().getName() : " + zoneCenter.getWorld().getName());
//          Bukkit.getLogger().info("Le joueur est hors de la zone, téléportation au centre : " + zoneCenter);
          player.teleport(zoneCenter);
          player.sendActionBar("§cVous avez été téléporté au centre de la zone !");
        }
      }
    }
  }

  public class Zone {
    private final List<SubZone> subZones = new ArrayList<>();

    // Ajoute une sous-zone (pavé droit)
    public void addSubZone(Location corner1, Location corner2) {
      subZones.add(new SubZone(corner1, corner2));
    }

    // Ajoute une sous-zone (cylindre)
    public void addSubZone(Location center, double radius, double minY, double maxY) {
      subZones.add(new SubZone(center, radius, minY, maxY));
    }

    // Vérifie si une position est dans l'une des sous-zones
    public boolean isInside(Location location) {
      if (location == null || location.getWorld() == null) {
        Bukkit.getLogger().warning("Location ou monde null dans isInside.");
        return false;
      }

      // Vérifie si la position est dans l'une des sous-zones du lobby
      Zone lobbyZone = gameZones.get("Lobby");
      if (lobbyZone != null) {
        for (SubZone subZone : lobbyZone.subZones) {
          if (subZone.isInside(location)) {
            return true;
          }
        }
      }

      // Vérifie si la position est dans l'une des sous-zones de la salle grise
      Zone SalleGriseZone = gameZones.get("SalleGrise");
      if (SalleGriseZone != null) {
        for (SubZone subZone : SalleGriseZone.subZones) {
          if (subZone.isInside(location)) {
            return true;
          }
        }
      }

//      String worldName = location.getWorld().getName();
//      Bukkit.getLogger().info("Vérification de la zone pour le monde : " + worldName);

      // Vérifie si le monde est valide et correspond à l'épreuve actuelle
      if ("Lobby".equalsIgnoreCase(EpreuveCommand.getEpreuve())) {
        if (location.getWorld() == null || !location.getWorld().getName().equals("world")) {
          return false;
        }
      } else {
        if (location.getWorld() == null || !location.getWorld().getName().equals("worlds/SquidGame/" + EpreuveCommand.getEpreuve())) {
          return false;
        }
      }


      // Vérifie si la position est dans l'une des sous-zones de cette zone
      for (SubZone subZone : subZones) {

        if (subZone.cyl){
          if (isInCylinder(location, subZone.cylCenter, subZone.radius, subZone.minY, subZone.maxY)) return true;
        } else {
          if (subZone.isInside(location)) {
            return true;
          }
        }
      }

//      Bukkit.getLogger().info("Le joueur n'est dans aucune sous-zone.");
      return false;
    }

    // Retourne le centre de la première sous-zone (par défaut)
    public Location getCenter() {
      if (subZones.isEmpty()) return null;
      return subZones.get(0).getCenter();
    }

    // Classe interne pour représenter une sous-zone
    private static class SubZone {
      private final boolean cyl;

      @Getter
      private Location corner1;
      @Getter
      private Location corner2;
      @Getter
      private Location cylCenter;
      @Getter
      private double radius;
      @Getter
      private double minY;
      @Getter
      private double maxY;

      public SubZone(Location corner1, Location corner2) {
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.cyl = false;
      }

      public SubZone(Location center, double radius, double minY, double maxY) {
        this.cylCenter = center;
        this.radius = radius;
        this.minY = minY;
        this.maxY = maxY;
        this.cyl = true;
      }

        public boolean isInside(Location location) {
        if (location == null || location.getWorld() == null) return false;
        if (!location.getWorld().equals(corner1.getWorld())) return false;

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return x >= Math.min(corner1.getX(), corner2.getX()) && x <= Math.max(corner1.getX(), corner2.getX())
            && y >= Math.min(corner1.getY(), corner2.getY()) && y <= Math.max(corner1.getY(), corner2.getY())
            && z >= Math.min(corner1.getZ(), corner2.getZ()) && z <= Math.max(corner1.getZ(), corner2.getZ());
      }

      public Location getCenter() {

        System.out.println(corner1.getWorld());

        double centerX = (corner1.getX() + corner2.getX()) / 2;
        double centerY = (corner1.getY() + corner2.getY()) / 2;
        double centerZ = (corner1.getZ() + corner2.getZ()) / 2;

        return new Location(
            "Lobby".equalsIgnoreCase(EpreuveCommand.getEpreuve())
                ? Bukkit.getWorld("world")
                : Bukkit.getWorld("worlds/SquidGame/" + EpreuveCommand.getEpreuve()),
            centerX,
            centerY,
            centerZ
        );
      }
    }
  }

  public boolean isInCylinder(Location loc, Location center, double radius, double minY, double maxY) {

    if (loc.getWorld() == null || center.getWorld() == null) return false;
    if (!loc.getWorld().equals(center.getWorld())) return false;

    if (loc.getY() < minY || loc.getY() > maxY) return false;

    double dx = loc.getX() - center.getX();
    double dz = loc.getZ() - center.getZ();
    double distanceSquared = dx * dx + dz * dz;

    return distanceSquared <= radius * radius;
  }

}