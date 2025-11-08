package fr.salut.squidgame.component.ListenerManager.MiniGames.BaP;

import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.commands.games.BaPCommand;
import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

import static fr.salut.squidgame.component.ListenerManager.MiniGames.BaP.BaPState.STOP;

public class BaPManager implements Listener {

  // ** Attributs principaux **
  private final Map<String, int[]> teamZones = new HashMap<>(); // Zones des équipes
  private final Map<String, List<int[]>> prisonZones = new HashMap<>(); // Zones des prisons
  @Getter
  private static final Set<Player> playersInPrison = new HashSet<>(); // Joueurs en prison
  private final Set<Player> recentSneakers = new HashSet<>();

  // ** Constructeur **
  public BaPManager() {
    initializeZones();
  }

  // ** Initialisation des zones **
  public void initializeZones() {
    teamZones.put("bleu_roi", new int[]{39, 65, -35, 60, 68, -14});
    teamZones.put("vert_profond", new int[]{39, 65, -57, 60, 68, -36});

    prisonZones.put("vert_profond", Arrays.asList(
        new int[]{31, 65, -35, 38, 68, -6},
        new int[]{31, 65, -13, 68, 68, -6},
        new int[]{61, 65, -35, 68, 68, -6}
    ));
    prisonZones.put("bleu_roi", Arrays.asList(
        new int[]{31, 65, -65, 38, 68, -36},
        new int[]{31, 65, -65, 68, 68, -58},
        new int[]{61, 65, -65, 68, 68, -36}
    ));
  }

  // ** Gestion des événements **

  @EventHandler
  public void onPlayerSneak(PlayerToggleSneakEvent event) {
    Player player = event.getPlayer();
    if (event.isSneaking()) {
      recentSneakers.add(player);
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
    if (event.getEntity() instanceof Snowball && BaPCommand.getBaPState() == STOP) {
      event.setCancelled(true);
      if (event.getEntity().getShooter() instanceof Player shooter) {
        shooter.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Le jeu est en état STOP, vous ne pouvez pas lancer la balle !"));
      }
    }
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    if (BaPCommand.getBaPState() == BaPState.OFF) return;

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

    if (zoneBounds == null || prisonBounds == null) return;

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
    if (BaPCommand.getBaPState() != BaPState.ON) return;

    if (event.getDamager() instanceof Snowball snowball && event.getEntity() instanceof Player hitPlayer) {
      if (!(snowball.getShooter() instanceof Player shooter)) {
        // Donne la balle au joueur touché
        giveSnowball(hitPlayer, ChatColor.GREEN + "Vous avez reçu la balle !");
        return;
      }

      Team hitPlayerTeam = hitPlayer.getScoreboard().getEntryTeam(hitPlayer.getName());
      Team shooterTeam = shooter.getScoreboard().getEntryTeam(shooter.getName());

      if (hitPlayerTeam == null || shooterTeam == null) return;

      if (playersInPrison.contains(hitPlayer)) {
        giveSnowball(hitPlayer, ChatColor.GREEN + "Vous avez reçu la balle !");
        return;
      }

      if (playersInPrison.contains(shooter) && !hitPlayerTeam.equals(shooterTeam)) {
        releasePlayer(shooter);
        imprisonPlayer(hitPlayer, hitPlayerTeam);
        return;
      }

      if (hitPlayerTeam.equals(shooterTeam)) {
        giveSnowball(hitPlayer, ChatColor.GREEN + "Vous avez reçu la balle !");
      } else {
        imprisonPlayer(hitPlayer, hitPlayerTeam);
      }
    }
  }

  @EventHandler
  public void onSnowballMiss(ProjectileHitEvent event) {
    if (BaPCommand.getBaPState() != BaPState.ON) return;

    if (event.getEntity() instanceof Snowball snowball && event.getHitEntity() == null) {
      Player closestPlayer = null;
      double closestDistance = Double.MAX_VALUE;

      for (Player player : Bukkit.getOnlinePlayers()) {
        // Vérifie si les mondes sont identiques
        if (!player.getWorld().equals(snowball.getWorld())) continue;

        Team team = player.getScoreboard().getEntryTeam(player.getName());
        if (team == null) continue;

        String teamName = team.getName().toLowerCase();
        if (!teamName.equals("bleu_roi") && !teamName.equals("vert_profond")) continue;

        double distance = player.getLocation().distance(snowball.getLocation());
        if (distance < closestDistance) {
          closestDistance = distance;
          closestPlayer = player;
        }
      }

      if (closestPlayer != null) {
        giveSnowball(closestPlayer, ChatColor.GREEN + "Vous avez reçu la balle !");
        closestPlayer.playSound(closestPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);
        closestPlayer.setCompassTarget(snowball.getLocation());
      } else {
        Bukkit.getLogger().warning("Aucun joueur proche dans les équipes bleu_roi ou vert_profond pour recevoir la balle !");
      }
    }
  }

  // ** Méthodes utilitaires **

  private void giveSnowball(Player player, String message) {
    if (!player.getInventory().contains(Material.SNOWBALL)) {
      int activeSlot = player.getInventory().getHeldItemSlot();
      ItemStack snowball = new ItemStack(Material.SNOWBALL, 1);
      player.getInventory().setItem(activeSlot, snowball);
    }
    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);

    applyGlowingToPlayersWithSnowball();
  }

  private void releasePlayer(Player player) {
    playersInPrison.remove(player);
    Team team = player.getScoreboard().getEntryTeam(player.getName());
    Location spawn = getTeamSpawn(team.getName());
    if (spawn != null) {
      player.teleport(spawn);
      player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
      player.sendMessage(ChatColor.GREEN + "Vous êtes libéré et de retour dans votre camp !");
    }
  }

  private void imprisonPlayer(Player player, Team team) {
    playersInPrison.add(player);
    Location prison = getPrisonSpawn(team.getName());
    if (prison != null) {
      giveSnowball(player, ChatColor.RED + "Vous avez été touché et envoyé en prison !");
      player.teleport(prison);
    }
    checkIfTeamIsCompletelyImprisoned();
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
        // Exécute la commande pour réinitialiser le timer
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard players set timer Timer 0");
        Bukkit.broadcastMessage(ChatColor.GOLD + "L'équipe " + teamName + " est entièrement emprisonnée ! Le jeu est terminé.");
        return;
      }
    }
  }

  public void notifyGuardsOfPrisoners() {
    Team gardeTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("garde");
    if (gardeTeam == null) return;

    // Compte le nombre de joueurs emprisonnés par équipe
    int bleuMarinePrisoners = (int) playersInPrison.stream().filter(player -> isPlayerInTeam(player, "bleu_roi")).count();
    int vertProfondPrisoners = (int) playersInPrison.stream().filter(player -> isPlayerInTeam(player, "vert_profond")).count();

    // Envoie les informations à chaque garde
    for (String playerName : gardeTeam.getEntries()) {
      Player gardePlayer = Bukkit.getPlayer(playerName);
      if (gardePlayer != null && gardePlayer.isOnline()) {
        gardePlayer.sendMessage(ChatColor.GOLD + "Résumé des joueurs emprisonnés :");
        gardePlayer.sendMessage(ChatColor.YELLOW + "Bleu Marine : " + ChatColor.RED + bleuMarinePrisoners + " joueur(s)");
        gardePlayer.sendMessage(ChatColor.YELLOW + "Vert Profond : " + ChatColor.RED + vertProfondPrisoners + " joueur(s)");
      }
    }
  }

  public static void applyGlowingToPlayersWithSnowball() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      Team team = player.getScoreboard().getEntryTeam(player.getName());
      if (team == null) continue;

      player.setGlowing(false);
      String teamName = team.getName().toLowerCase();
      if ((teamName.equals("bleu_roi") || teamName.equals("vert_profond"))
          && player.getInventory().contains(Material.SNOWBALL)) {
        player.setGlowing(true);
      } else {
        player.setGlowing(false);
      }
    }
  }

  private boolean isPlayerInTeam(Player player, String teamName) {
    Team team = player.getScoreboard().getEntryTeam(player.getName());
    return team != null && team.getName().equalsIgnoreCase(teamName);
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
        return true;
      }
    }

    return false;
  }

  private Location getPrisonSpawn(String teamName) {
    return switch (teamName.toLowerCase()) {
      case "bleu_roi" -> new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 49, 65, -62, 0, 0);
      case "vert_profond" -> new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 49, 65, -10, 180, 0);
      default -> null;
    };
  }

  private Location getTeamSpawn(String teamName) {
    return switch (teamName.toLowerCase()) {
      case "bleu_roi" -> new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 49, 65, -25, 0, 0);
      case "vert_profond" -> new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 49, 65, -47, 180, 0);
      default -> null;
    };
  }

}