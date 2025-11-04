package fr.salut.squidgame.component.ListenerManager.MiniGames.LoupGlace;

import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.commands.games.LoupGlaceCommand;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

import fr.salut.squidgame.component.ListenerManager.intance.TeamManager;

import java.util.*;

import static fr.salut.squidgame.component.ListenerManager.MiniGames.BaP.BaPState.STOP;


public class LoupGlaceManager implements Listener {

  // Variables

  private static final SquidGame plugin = SquidGame.getInstance();
  private static final HashMap<UUID, Boolean> playerFrozenStatus = new HashMap<>();
  private static final Map<UUID, BukkitTask> snowTasks = new HashMap<>();
  private static BukkitTask frozenTask;
  private static final Map<UUID, Location> playerLastLocation = new HashMap<>();
  private static Team loupGlaceTeam;
  private static Team safeTeam;
  private static final Map<String, String> allowedTeam = Map.of(
      "bleu_roi", "Bleu Roi",
      "vert_profond", "Vert Profond"
  );

  // Functions
  public static void startLoupGlace() {
    // Start the Loup Glace mini-game
    resetLoupGlace();
    new LoupGlaceManager().startFrozenStatusTask();
  }

  public static void offLoupGlace() {
    // Start the Loup Glace mini-game
    resetLoupGlace();
    if (frozenTask != null) frozenTask.cancel();
  }

  private static void resetLoupGlace() {
    playerFrozenStatus.clear();
    playerLastLocation.clear();
    // Reset the Loup Glace mini-game
  }

  public static void shuffleLoupGlaceTeams() {
    // Select wich team is the Loup Glace
    List<String> keys = new ArrayList<>(allowedTeam.keySet());

    Random r = new Random();
    String loupGlaceTeamName = keys.get(r.nextInt(keys.size()));
    String safeTeamName = keys.stream()
        .filter(name -> !name.equals(loupGlaceTeamName))
        .findFirst()
        .orElse(null);

    loupGlaceTeam = TeamManager.getTeam(loupGlaceTeamName);
    safeTeam = TeamManager.getTeam(safeTeamName);
    String loupGlaceTeamString = allowedTeam.get(loupGlaceTeamName);

    shuffleActionBarTask(loupGlaceTeamString);
  }

  private static void shuffleActionBarTask(String loupGlaceTeamSelected) {

    new BukkitRunnable() {
      int ticks = 200; // 10 secondes
      private boolean showLoupGlace = true;

      @Override
      public void run() {

        if (ticks <= 0) {
          for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar("§aÉquipe sélectionnée : §b" + loupGlaceTeamSelected);
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.5f);
            player.sendTitle(
                "§cLes Loups Glacés seront :",                 // Title principal
                "§b" + loupGlaceTeamSelected,  // Subtitle (nom de l'équipe)
                10,                               // fadeIn (ticks)
                100,                               // stay (ticks)
                10                                // fadeOut (ticks)
            );
          }
          cancel();
          return;
        }

        // Déterminer l'intervalle actuel (plus petit = plus rapide)
        int interval;
        if (ticks > 150) interval = 2;
        else if (ticks > 100) interval = 4;
        else if (ticks > 50) interval = 8;
        else interval = 12;

        if (ticks % interval == 0) {
          String teamToShow = showLoupGlace
              ? allowedTeam.get(loupGlaceTeam.getName())
              : allowedTeam.get(safeTeam.getName());

          showLoupGlace = !showLoupGlace; // bascule pour le prochain intervalle
          for (Player player : Bukkit.getOnlinePlayers()) {
            // Ne joue le son qu'une fois par intervalle
            player.sendActionBar("§e" + teamToShow);
            float pitch = 0.8f + (ticks / 200f) * 0.4f;
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HAT, 1, pitch);
          }
        }

        ticks--;
      }

    }.runTaskTimer(plugin, 0, 1);

  }

  private void checkIfTeamIsCompletelyFreezed() {
    boolean allImprisoned = true;
    for (String entry : safeTeam.getEntries()) {
      Player player = Bukkit.getPlayer(entry);
      if (player != null && !playerFrozenStatus.get(player.getUniqueId())) {
        allImprisoned = false;
        break;
      }
    }

    if (allImprisoned) {
      LoupGlaceCommand.setLoupGlaceState(LoupGlaceState.STOP);
      // Exécute la commande pour réinitialiser le timer
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard players set timer Timer 0");
      Bukkit.broadcastMessage(ChatColor.GOLD + "L'équipe " + allowedTeam.get(safeTeam.getName()) + " est entièrement gelée ! Le jeu est terminé.");
      return;
    }

  }


  private void startFrozenStatusTask() {

    if (frozenTask != null && !frozenTask.isCancelled()) return;
    frozenTask = new BukkitRunnable() {
      @Override
      public void run() {
        LoupGlaceState state = LoupGlaceCommand.getLoupGlaceState();
        if (state == LoupGlaceState.OFF) return;
        for (UUID uuid : playerFrozenStatus.keySet()) {
          if (playerFrozenStatus.getOrDefault(uuid, false)) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
              applyPowderSnowVisual(player);
            }
          }
        }
      }
    }.runTaskTimer(plugin, 0L, 1L);
  }


  public static void applyPowderSnowVisual(Player player) {
    UUID uuid = player.getUniqueId();

    // Si le joueur a déjà un Runnable actif, ne rien faire
    if (snowTasks.containsKey(uuid)) return;

    BukkitTask task = new BukkitRunnable() {
      @Override
      public void run() {
        LoupGlaceState state = LoupGlaceCommand.getLoupGlaceState();
        if (state == LoupGlaceState.OFF) return;
        // si le joueur n'est plus gelé ou est déconnecté, arrêter le Runnable
        if (!playerFrozenStatus.getOrDefault(uuid, false) || !player.isOnline()) {
          snowTasks.remove(uuid);
          cancel();
          return;
        }

        // Spawn particules
        player.getWorld().spawnParticle(
            Particle.SNOWFLAKE,
            player.getLocation().add(0, 2, 0),
            5,
            0.3, 0.3, 0.3,
            0.05
        );
      }
    }.runTaskTimer(plugin, 0L, 20L); // toutes les 20 ticks

    snowTasks.put(uuid, task); // stocke le Runnable
  }

  // Event Handlers

  @EventHandler
  public void onPlayerInteract(EntityDamageByEntityEvent event) {
    LoupGlaceState state = LoupGlaceCommand.getLoupGlaceState();
    if (state == LoupGlaceState.OFF) return;

    if (!(event.getDamager() instanceof Player damager)) return;
    if (!(event.getEntity() instanceof Player target)) return;

    if (state == LoupGlaceState.STOP) {
      damager.sendActionBar("§cLe Loup Glace est actuellement en pause.");
      event.setCancelled(true);
      return;
    }

    Team teamDamager = TeamManager.getTeam(damager);
    Team teamTarget = TeamManager.getTeam(target);

    if (teamDamager != null && !allowedTeam.containsKey(teamDamager.getName())) return;
    if (teamTarget != null && !allowedTeam.containsKey(teamTarget.getName())) return;

    //Cas où les deux joueurs sont dans l'équipe Loup Glace
    if (teamDamager != null && teamTarget != null && teamDamager.equals(teamTarget) && teamDamager.equals(loupGlaceTeam)) {
      damager.sendActionBar("§cVous ne pouvez pas attaquer un membre de votre équipe !");
      event.setCancelled(true);
    }
    // Cas où les deux joueurs ne sont pas dans l'équipe Loup Glace
    else if (teamDamager != null && teamTarget != null && !teamDamager.equals(loupGlaceTeam) && !teamTarget.equals(loupGlaceTeam)) {
      if (playerFrozenStatus.getOrDefault(damager.getUniqueId(), false)) {
        damager.sendActionBar("§cVous ne pouvez pas libérer votre camarade car vous êtes gelé !");
        return;
      }
      if (playerFrozenStatus.getOrDefault(target.getUniqueId(), false)) {
        damager.sendActionBar("§cVous venez de libérer votre camarade !");
        playerFrozenStatus.put(target.getUniqueId(), false);
        target.sendActionBar("§aVous avez été libéré par " + damager.getName() + " !");
        event.setCancelled(true);
      }
    } else if (teamDamager != null && teamTarget != null && teamDamager.equals(loupGlaceTeam) && !teamTarget.equals(loupGlaceTeam)) {
      // Cas normal où le damager est dans l'équipe Loup Glace et le target ne l'est pas

      // Cas où la target est déjà gelé
      if (playerFrozenStatus.getOrDefault(target.getUniqueId(), false)) {
        damager.sendActionBar("§cLe joueur est déjà gelé !");
        event.setCancelled(true);
        return;
      }

      // Cas où la target n'est pas gelé
      playerFrozenStatus.put(target.getUniqueId(), true);
      damager.sendActionBar("§aVous avez gelé " + target.getName() + " !");
      target.sendActionBar("§bVous avez été gelé par " + damager.getName() + " !");
      event.setCancelled(true);
      checkIfTeamIsCompletelyFreezed();
    }
  }


  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    LoupGlaceState state = LoupGlaceCommand.getLoupGlaceState();
    if (state == LoupGlaceState.OFF) return;

    Player player = event.getPlayer();
    if (playerFrozenStatus.getOrDefault(player.getUniqueId(), false)) {
      Location from = event.getFrom();
      Location to = event.getTo();

      // Bloque uniquement le mouvement horizontal (X et Z), garde Y pour tomber
      if (from.getX() != to.getX() || from.getZ() != to.getZ() || from.getY() < to.getY()) {
        event.setCancelled(true);
      }
      player.sendActionBar("§bVous êtes gelé et ne pouvez pas bouger !");
    }
  }


  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    LoupGlaceState state = LoupGlaceCommand.getLoupGlaceState();
    if (state == LoupGlaceState.OFF) return;

    Player player = event.getPlayer();
    if (!TeamManager.getTeam(player).equals(loupGlaceTeam) && playerFrozenStatus.get(player.getUniqueId())) {
      playerFrozenStatus.put(player.getUniqueId(), false);
    }
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    LoupGlaceState state = LoupGlaceCommand.getLoupGlaceState();
    if (state == LoupGlaceState.OFF) return;

    Player player = event.getPlayer();
    playerLastLocation.put(player.getUniqueId(), player.getLocation());
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    LoupGlaceState state = LoupGlaceCommand.getLoupGlaceState();
    if (state == LoupGlaceState.OFF) return;

    Player player = event.getPlayer();
    Location lastLocation = playerLastLocation.get(player.getUniqueId());
    if (lastLocation != null) {
      player.teleport(lastLocation);
    }
  }

}
