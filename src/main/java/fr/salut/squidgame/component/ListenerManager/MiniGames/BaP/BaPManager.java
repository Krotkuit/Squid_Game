package fr.salut.squidgame.component.ListenerManager.MiniGames.BaP;


import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.commands.BaPCommand;
import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.md_5.bungee.api.chat.TextComponent;
import java.util.*;
import java.util.List;

import static fr.salut.squidgame.component.ListenerManager.MiniGames.BaP.BaPState.STOP;

public class BaPManager implements Listener {
  private final Map<String, int[]> teamZones = new HashMap<>(); // Zones des équipes
  private final Map<String, List<int[]>> prisonZones = new HashMap<>(); // Zones des prisons

  @Getter
  private static final Set<Player> playersInPrison = new HashSet<>(); // Joueurs en prison
  private final Set<Player> recentSneakers = new HashSet<>();

  public BaPManager() {
    initializeZones();
  }


  public void initializeZones() {
    // Définir les zones des équipes avec les coordonnées des deux coins opposés
    teamZones.put("bleu", new int[]{173, -59, -205, 194, -56, -184});
    teamZones.put("vert", new int[]{173, -59, -227, 194, -56, -206});

    // Définir les zones des prisons avec les coordonnées des trois cubes
    prisonZones.put("vert", Arrays.asList(
        new int[]{165, -59, -205, 172, -56, -176},
        new int[]{165, -59, -183, 202, -56, -176},
        new int[]{195, -59, -205, 202, -56, -176}
    ));
    prisonZones.put("bleu", Arrays.asList(
        new int[]{165, -59, -235, 172, -56, -206},
        new int[]{165, -59, -235, 202, -56, -228},
        new int[]{195, -59, -235, 202, -56, -206}
    ));
  }
  private Location getPrisonSpawn(String teamName) {
      return switch (teamName.toLowerCase()) {
          case "bleu" -> new Location(Bukkit.getWorld("world"), 183, -59, -232); // Coordonnées fixes pour l'équipe "bleu"
          case "vert" -> new Location(Bukkit.getWorld("world"), 183, -59, -180); // Coordonnées fixes pour l'équipe "orange"
          default -> null; // Si l'équipe n'existe pas
      };
  }

  private Location getTeamSpawn(String teamName) {
      return switch (teamName.toLowerCase()) {
          case "bleu" -> new Location(Bukkit.getWorld("world"), 183, -59, -195); // Coordonnées du centre du camp bleu
          case "vert" -> new Location(Bukkit.getWorld("world"), 183, -59, -217); // Coordonnées du centre du camp orange
          default -> null; // Si l'équipe n'existe pas
      };
  }

  @EventHandler
  public void onPlayerSneak(PlayerToggleSneakEvent event) {
    Player player = event.getPlayer();
    if (event.isSneaking()) {
      recentSneakers.add(player);
      // Retirer le joueur du Set après 10 ticks
      new BukkitRunnable() {
        @Override
        public void run() {
          recentSneakers.remove(player);
        }
      }.runTaskLater(SquidGame.getInstance(), 10);
    }
  }

  @EventHandler
  public void onSnowballLaunch(ProjectileLaunchEvent event) {
    BaPState gameState = BaPCommand.getBaPState();

    if (event.getEntity() instanceof Snowball) {
      if (gameState == STOP) {
        event.setCancelled(true); // Annule le lancement de la Snowball
        if (event.getEntity().getShooter() instanceof Player shooter) {
          shooter.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Le jeu est en état STOP, vous ne pouvez pas lancer la balle !"));
        }
      }
    }
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    BaPState gameState = BaPCommand.getBaPState();

    if (gameState == BaPState.OFF) return;

    Player player = event.getPlayer();
    Location to = event.getTo();
    Location from = event.getFrom();
    Team team = player.getScoreboard().getEntryTeam(player.getName());

    if (team == null) {
      player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Vous n'êtes pas assigné à une équipe !"));
      return;
    }

    String teamName = team.getName().toLowerCase();
    int[] zoneBounds = teamZones.get(teamName);
    List<int[]> prisonBounds = prisonZones.get(teamName);

    if (zoneBounds == null || prisonBounds == null) {
      return;
    }

    if (playersInPrison.contains(player)) {
      if (!isInsideAnyBounds(to, prisonBounds)) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Vous ne pouvez pas quitter la prison !"));
        event.setTo(from);
      }
    } else {
      if (!isInsideBounds(to, zoneBounds)) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Vous ne pouvez pas quitter votre zone !"));
        event.setTo(from);
      }
    }
  }

  @EventHandler
  public void onSnowballHit(EntityDamageByEntityEvent event) {
    BaPState gameState = BaPCommand.getBaPState();

    if (gameState != BaPState.ON) return;
    if (event.getDamager() instanceof Snowball snowball && event.getEntity() instanceof Player hitPlayer) {
      if (snowball.getShooter() instanceof Player shooter) {
        Team hitPlayerTeam = hitPlayer.getScoreboard().getEntryTeam(hitPlayer.getName());
        Team shooterTeam = shooter.getScoreboard().getEntryTeam(shooter.getName());

        if (recentSneakers.contains(hitPlayer)) {
          // Le joueur rattrape la balle
          hitPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BLUE + "Vous avez rattrapé la balle !"));
          hitPlayer.playSound(hitPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);
          return;
        }

        if (hitPlayerTeam != null && shooterTeam != null) {
          if (playersInPrison.contains(shooter) && !playersInPrison.contains(hitPlayer) && !hitPlayerTeam.equals(shooterTeam)) {
            // Libérer le lanceur
            playersInPrison.remove(shooter);
            Location shooterSpawn = getTeamSpawn(shooterTeam.getName());
            if (shooterSpawn != null) {
              shooter.teleport(shooterSpawn);
              shooter.playSound(shooter.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
              shooter.sendMessage(ChatColor.GREEN + "Vous êtes libéré et de retour dans votre camp !");
            }

            // Emprisonner l'adversaire
            playersInPrison.add(hitPlayer);
            Location hitPlayerPrison = getPrisonSpawn(hitPlayerTeam.getName());
            if (hitPlayerPrison != null) {
              hitPlayer.teleport(hitPlayerPrison);
              hitPlayer.playSound(hitPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);
              hitPlayer.setCompassTarget(snowball.getLocation());
              hitPlayer.sendMessage(ChatColor.RED + "Vous avez été touché et envoyé en prison !");
            }
          } else if (!hitPlayerTeam.equals(shooterTeam)) {
            // Comportement normal : emprisonner l'adversaire
            playersInPrison.add(hitPlayer);
            Location hitPlayerPrison = getPrisonSpawn(hitPlayerTeam.getName());
            if (hitPlayerPrison != null) {
              hitPlayer.teleport(hitPlayerPrison);
              shooter.playSound(shooter.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
              hitPlayer.playSound(hitPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);
              // Diriger la boussole du joueur vers la position de la Snowball
              hitPlayer.setCompassTarget(snowball.getLocation());
              hitPlayer.sendMessage(ChatColor.RED + "Vous avez été touché et envoyé en prison !");
            }
          }
          checkIfTeamIsCompletelyImprisoned();
        }
      }
    }
  }

  private final Set<UUID> processedSnowballs = new HashSet<>();

  @EventHandler
  public void onSnowballMiss(ProjectileHitEvent event) {
    BaPState gameState = BaPCommand.getBaPState();

    if (gameState != BaPState.ON) return;

    if (event.getEntity() instanceof Snowball snowball) {

      // Vérifier si la Snowball a déjà été traitée
      if (processedSnowballs.contains(snowball.getUniqueId())) return;
      processedSnowballs.add(snowball.getUniqueId());

      if (snowball.getShooter() instanceof Player shooter) {

        // Trouver le joueur le plus proche dans les équipes "bleu" ou "vert"
        Player closestPlayer = null;
        double closestDistance = Double.MAX_VALUE;

        for (Player player : Bukkit.getOnlinePlayers()) {
          Team team = player.getScoreboard().getEntryTeam(player.getName());
          if (team == null) continue;

          String teamName = team.getName().toLowerCase();
          if (!teamName.equals("bleu") && !teamName.equals("vert")) continue;

          double distance = player.getLocation().distance(snowball.getLocation());
          if (distance < closestDistance) {
            closestDistance = distance;
            closestPlayer = player;
          }
        }

        // Donner une Snowball au joueur le plus proche
        if (closestPlayer != null) {
          closestPlayer.getInventory().addItem(new ItemStack(Material.SNOWBALL, 1));
          closestPlayer.playSound(closestPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);
          closestPlayer.setCompassTarget(snowball.getLocation());
          closestPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "Vous avez reçu la balle !"));
        } else {
          shooter.sendMessage(ChatColor.RED + "Aucun joueur proche dans les équipes orange ou vert pour recevoir la balle !");
        }
      }
    }
  }

  private void checkIfTeamIsCompletelyImprisoned() {
    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    for (String teamName : teamZones.keySet()) {
      Team team = scoreboard.getTeam(teamName);
      if (team == null) continue;

      boolean allImprisoned = true;
      for (String entry : team.getEntries()) {
        Player player = Bukkit.getPlayer(entry);
        if (player != null && !playersInPrison.contains(player)) {
          allImprisoned = false;
          break;
        }
      }

      if (allImprisoned) {
        BaPCommand.setBaPState(STOP);
        Bukkit.broadcastMessage(ChatColor.GOLD + "L'équipe " + teamName + " est entièrement emprisonnée ! Le jeu est terminé.");
        return;
      }
    }
  }

  private boolean isInsideBounds(Location loc, int[] bounds) {
    if (bounds == null) {
      Bukkit.getLogger().warning("Les limites sont nulles !");
      return false;
    }

    int x1 = Math.min(bounds[0], bounds[3]);
    int y1 = Math.min(bounds[1], bounds[4]);
    int z1 = Math.min(bounds[2], bounds[5]);
    int x2 = Math.max(bounds[0], bounds[3]);
    int y2 = Math.max(bounds[1], bounds[4]);
    int z2 = Math.max(bounds[2], bounds[5]);

      return loc.getX() >= x1 && loc.getX() <= x2 &&
          loc.getY() >= y1 && loc.getY() <= y2 &&
          loc.getZ() >= z1 && loc.getZ() <= z2;
  }

  private boolean isInsideAnyBounds(Location loc, List<int[]> boundsList) {
    if (boundsList == null || boundsList.isEmpty()) {
      Bukkit.getLogger().warning("Les limites sont nulles ou vides !");
      return false;
    }

    for (int[] bounds : boundsList) {
      if (isInsideBounds(loc, bounds)) {
        return true; // Le joueur est dans l'un des cubes
      }
    }

    return false; // Le joueur n'est dans aucun des cubes
  }
}