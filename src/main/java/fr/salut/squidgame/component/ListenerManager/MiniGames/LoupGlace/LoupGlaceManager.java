package fr.salut.squidgame.component.ListenerManager.MiniGames.LoupGlace;

import fr.salut.squidgame.component.commands.games.LoupGlaceCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import fr.salut.squidgame.component.ListenerManager.intance.TeamManager;
import fr.salut.squidgame.SquidGame.plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;



public class LoupGlaceManager implements Listener {

  // Variables
  private final HashMap <UUID, Boolean> playerFrozenStatus = new HashMap<>();
  private static Team loupGlaceTeam;
  private static final List<String> allowedTeam = List.of(
    "bleu_roi", "vert_profond"
  );
  @NotNull Plugin plugin;

  // Functions
  private void startLoupGlace() {
    // Start the Loup Glace mini-game

  }

  private void stopLoupGlace() {
    // Stop the Loup Glace mini-game
  }

  private void resetLoupGlace() {
    // Reset the Loup Glace mini-game
  }

  public static void shuffleLoupGlaceTeams() {
    // Select wich team is the Loup Glace
    Random r = new Random();
    String loupGlaceTeamName = allowedTeam.get(r.nextInt(allowedTeam.size()));
    loupGlaceTeam = TeamManager.getTeam(loupGlaceTeamName);

    shuffleActionBarTask(loupGlaceTeam);
  }

  private static void shuffleActionBarTask(Team loupGlaceTeamSelected) {
    Integer ticks = 1000;
    new BukkitRunnable() {

      @Override
      public void run() {

      }
    }.runTaskTimer(plugin, 0, 1);
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

    //Cas où les deux joueurs sont dans l'équipe Loup Glace
    if (teamDamager != null && teamTarget != null && teamDamager.equals(teamTarget) && teamDamager.equals(loupGlaceTeam)) {
      damager.sendActionBar("§cVous ne pouvez pas attaquer un membre de votre équipe !");
      event.setCancelled(true);
      return;
    }
    // Cas où seul le damager est dans l'équipe Loup Glace
    else if (teamDamager != null && teamTarget != null && teamDamager.equals(teamTarget) && !teamDamager.equals(loupGlaceTeam)) {
      damager.sendActionBar("§cVous n'êtes pas dans l'équipe Loup Glace !");
      event.setCancelled(true);
      return;
    }
    // Cas où les deux joueurs ne sont pas dans l'équipe Loup Glace
    else if (teamDamager != null && teamTarget != null && !teamDamager.equals(loupGlaceTeam) && !teamTarget.equals(loupGlaceTeam)) {
      if (playerFrozenStatus.getOrDefault(target.getUniqueId(), false)) {
        damager.sendActionBar("§cVous venez de libérer votre camarade !");
        playerFrozenStatus.put(target.getUniqueId(), false);
        target.sendActionBar("§aVous avez été libéré par " + damager.getName() + " !");
        event.setCancelled(true);
        return;
      }
    }

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

  }


  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    LoupGlaceState state = LoupGlaceCommand.getLoupGlaceState();
    if (state == LoupGlaceState.OFF) return;

    Player player = event.getPlayer();
    if (playerFrozenStatus.getOrDefault(player.getUniqueId(), false)) {
      event.setCancelled(true);
      player.sendActionBar("§bVous êtes gelé et ne pouvez pas bouger !");
    }
  }

}
