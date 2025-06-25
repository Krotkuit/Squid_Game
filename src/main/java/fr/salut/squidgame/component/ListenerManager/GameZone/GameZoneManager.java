package fr.salut.squidgame.component.ListenerManager.GameZone;

import fr.salut.squidgame.component.ListenerManager.BlockDetector;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;

public class GameZoneManager implements Listener {

  private final Map<String, Zone> gameZones = new HashMap<>();

  public GameZoneManager() {
    // Définir les zones pour chaque épreuve
    gameZones.put("123Soleil", new Zone(
        new Location(Bukkit.getWorld("world"), -54, -60, 71),
        new Location(Bukkit.getWorld("world"), -102, -40, 256)
    ));
    gameZones.put("Biscuit_Team", new Zone(
        new Location(Bukkit.getWorld("world"), -40, 60, -40),
        new Location(Bukkit.getWorld("world"), -20, 80, -20)
    ));
    // Ajouter d'autres épreuves ici...
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    Player player = event.getPlayer();
    Location toLocation = event.getTo();
    // Vérifie si le joueur a le tag "joueur"

    // Vérifie si le joueur n'est pas dans l'équipe "garde"
    //if (player.getScoreboard().getEntryTeam(player.getName()) != null &&
    //    player.getScoreboard().getEntryTeam(player.getName()).getName().equalsIgnoreCase("garde")) {
    //  return;
    //}
    String currentEpreuve = BlockDetector.getEpreuve(); // Récupère l'épreuve actuelle
    Zone zone = gameZones.get(currentEpreuve);

    if (zone == null) return; // Pas de zone définie pour cette épreuve
    boolean isOutsideZone = !zone.isInside(toLocation);

    // Vérifie si un spectateur observe ce joueur
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      if (onlinePlayer.getGameMode() == GameMode.SPECTATOR && onlinePlayer.getSpectatorTarget() == player && isOutsideZone) {
        // Action si un spectateur observe ce joueur
        onlinePlayer.setSpectatorTarget(null);
        onlinePlayer.sendActionBar("§cVous suivez un joueur qui ne peut pas quitter la zone !");
      }
    }
    if (!player.getScoreboardTags().contains("joueur")) return;
    // Vérifie si le joueur sort de la zone
    if (isOutsideZone) {
      event.setCancelled(true); // Annule le mouvement
      player.sendActionBar("§cVous ne pouvez pas quitter la zone de l'épreuve !");
    }

    if (player.getGameMode() == GameMode.SPECTATOR && isOutsideZone) {
      Location center = zone.getCenter();
      player.teleport(center); // Téléporte le spectateur au centre de la zone
      player.sendActionBar("§cVous avez été téléporté au centre de la zone de jeu !");
    }


  }
  @EventHandler
  public void onPlayerTeleport(PlayerTeleportEvent event) {
    Player player = event.getPlayer();
    Location toLocation = event.getTo();

    String currentEpreuve = BlockDetector.getEpreuve(); // Récupère l'épreuve actuelle
    Zone zone = gameZones.get(currentEpreuve);

    if (zone == null) return; // Pas de zone définie pour cette épreuve

    boolean isOutsideZone = !zone.isInside(toLocation);

    // Vérifie si un spectateur observe ce joueur
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      if (onlinePlayer.getGameMode() == GameMode.SPECTATOR && onlinePlayer.getSpectatorTarget() == player && isOutsideZone) {
        // Retire l'observateur de la vue du joueur
        onlinePlayer.setSpectatorTarget(null);
        onlinePlayer.sendActionBar("§cVous suivez un joueur qui ne peut pas quitter la zone !");
      }
    }

    // Vérifie si le joueur a le tag "joueur"
    if (!player.getScoreboardTags().contains("joueur")) return;

    // Annule la téléportation si elle est en dehors de la zone
    if (isOutsideZone) {
      event.setCancelled(true);
      player.sendActionBar("§cVous ne pouvez pas vous téléporter en dehors de la zone de l'épreuve !");
    }

    if (player.getGameMode() == GameMode.SPECTATOR && isOutsideZone) {
        Location center = zone.getCenter();
        player.teleport(center); // Téléporte le spectateur au centre de la zone
        player.sendActionBar("§cVous avez été téléporté au centre de la zone de jeu !");
    }
  }

  private static class Zone {
    private final Location corner1;
    private final Location corner2;

    public Zone(Location corner1, Location corner2) {
      this.corner1 = corner1;
      this.corner2 = corner2;
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
      double centerX = (corner1.getX() + corner2.getX()) / 2;
      double centerY = (corner1.getY() + corner2.getY()) / 2;
      double centerZ = (corner1.getZ() + corner2.getZ()) / 2;
      return new Location(corner1.getWorld(), centerX, centerY, centerZ);
    }
  }

}