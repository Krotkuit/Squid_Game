package fr.salut.squidgame.component.ListenerManager;

import fr.salut.squidgame.component.ListenerManager.GameZone.GameZoneManager;
import fr.salut.squidgame.component.ListenerManager.compteur.MAJ_compteur;
import fr.salut.squidgame.component.commands.EpreuveCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

import static fr.salut.squidgame.component.ListenerManager.GameZone.GameZoneManager.gameZones;

public class DeathListener implements Listener {


  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    // mise à jour du compteur
    new MAJ_compteur();

    // rétablir les items
    Player player = event.getPlayer();
    Team team = player.getScoreboard().getEntryTeam(player.getName());

    // supprime le message de mort
    event.deathMessage(Component.empty());
    if (team == null) return;
    if (team.getName().equalsIgnoreCase("garde") || team.getName().equalsIgnoreCase("mort")) return;

    team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("mort");
    // Ajouter le joueur à l'équipe "mort"
    team.addEntry(player.getName());

    String deathMessage = ChatColor.GRAY + "Joueur " + ChatColor.GREEN + player.getDisplayName() + ChatColor.GRAY + " est éliminé";

    // Envoi du message dans le chat
    Bukkit.broadcastMessage(deathMessage);

  }

  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent event) {
    Player player = event.getPlayer();
    Team team = player.getScoreboard().getEntryTeam(player.getName());
    if (team.getName().equalsIgnoreCase("garde")) return;

    player.getInventory().clear();

    player.setGameMode(GameMode.ADVENTURE);

    String currentEpreuve = EpreuveCommand.getEpreuve();
    GameZoneManager.Zone zone = gameZones.get(currentEpreuve);
    if (zone == null) return;
    Location zoneCenter = zone.getCenter();

    Bukkit.getScheduler().runTaskLater(
      Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("SquidGame")), () -> {
        //player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false, false));
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(zoneCenter);
      }, 1L);
  }
}
