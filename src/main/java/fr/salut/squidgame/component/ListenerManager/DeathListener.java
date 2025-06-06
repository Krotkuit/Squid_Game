package fr.salut.squidgame.component.ListenerManager;

import fr.salut.squidgame.component.ListenerManager.compteur.MAJ_compteur;
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

    int Respawn_X;
    int Respawn_Y;
    int Respawn_Z;
    if (Objects.equals(BlockDetector.getEpreuve(), "123Soleil")) {
      Respawn_X = -79;
      Respawn_Y = -43;
      Respawn_Z = 125;
    } else if (Objects.equals(BlockDetector.getEpreuve(), "Biscuit_Team")) {
      Respawn_X = -334;
      Respawn_Y = -42;
      Respawn_Z = 41;
    } else if (Objects.equals(BlockDetector.getEpreuve(), "Biscuit_Game")) {
      Respawn_X = -294;
      Respawn_Y = -40;
      Respawn_Z = 150;
    } else if (Objects.equals(BlockDetector.getEpreuve(), "Tire_a_la_Corde")) {
      Respawn_X = 50;
      Respawn_Y = -17;
      Respawn_Z = 74;
    }
    else if (Objects.equals(BlockDetector.getEpreuve(), "Arc_en_Ciel")) {
      Respawn_X = -215;
      Respawn_Y = -41;
      Respawn_Z = -131;
    }
    else if (Objects.equals(BlockDetector.getEpreuve(), "Brise_Glace")) {
      Respawn_X = 44;
      Respawn_Y = -23;
      Respawn_Z = -30;
    }
    else if (Objects.equals(BlockDetector.getEpreuve(), "Carrousel")) {
      Respawn_X = -127;
      Respawn_Y = -45;
      Respawn_Z = 12;
    }
    else if (Objects.equals(BlockDetector.getEpreuve(), "Billes")) {
      Respawn_X = 146;
      Respawn_Y = -43;
      Respawn_Z = -63;
    }
    else if (Objects.equals(BlockDetector.getEpreuve(), "Discotheque")) {
      Respawn_X = 139;
      Respawn_Y = -43;
      Respawn_Z = 34;
    }
    else if (Objects.equals(BlockDetector.getEpreuve(), "Jack_a_dit")) {
      Respawn_X = -254;
      Respawn_Y = -41;
      Respawn_Z = 37;
    } else if (Objects.equals(BlockDetector.getEpreuve(), "Bataille_Navale")) {
      Respawn_X = -151;
      Respawn_Y = -43;
      Respawn_Z = -97;
    } else if (Objects.equals(BlockDetector.getEpreuve(), "Croque_Carotte")) {
      Respawn_X = 42;
      Respawn_Y = -31;
      Respawn_Z = -134;
    } else if (Objects.equals(BlockDetector.getEpreuve(), "Puissance_4")) {
      Respawn_X = -190;
      Respawn_Y = -42;
      Respawn_Z = 97;
    } else if (Objects.equals(BlockDetector.getEpreuve(), "Morpion")) {
      Respawn_X = -334;
      Respawn_Y = -41;
      Respawn_Z = -78;
    } else if (Objects.equals(BlockDetector.getEpreuve(), "Squid_Game")) {
      Respawn_X = -256;
      Respawn_Y = -40;
      Respawn_Z = -52;
    } else if (Objects.equals(BlockDetector.getEpreuve(), "Roulette_Russe")) {
      Respawn_X = -20;
      Respawn_Y = -60;
      Respawn_Z = 141;
    } else if (Objects.equals(BlockDetector.getEpreuve(), "Tic_Tac_Explosif")) {
      Respawn_X = -84;
      Respawn_Y = -60;
      Respawn_Z = -169;
    } else if (Objects.equals(BlockDetector.getEpreuve(), "Bras_d_Argent")) {
      Respawn_X = -267;
      Respawn_Y = -44;
      Respawn_Z = -278;
    } else if (Objects.equals(BlockDetector.getEpreuve(), "Loup_Touche_Touche_Explosif")) {
      Respawn_X = 28;
      Respawn_Y = -22;
      Respawn_Z = -253;
    } else if (Objects.equals(BlockDetector.getEpreuve(), "Balle_aux_Prisonniers")) {
      Respawn_X = 210;
      Respawn_Y = -47;
      Respawn_Z = -195;
    } else if (Objects.equals(BlockDetector.getEpreuve(), "Chaises_Musicales")) {
      Respawn_X = 139;
      Respawn_Y = -43;
      Respawn_Z = 36;
    } else if (Objects.equals(BlockDetector.getEpreuve(), "Poule_Renard_Vipere")) {
      Respawn_X = -69;
      Respawn_Y = -40;
      Respawn_Z = -243;
    } else {
      Respawn_X = -41;
      Respawn_Y = -46;
      Respawn_Z = -3;
    }
    Bukkit.getScheduler().runTaskLater(
      Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("SquidGame")), () -> {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false, false));
        player.teleport(new Location(Bukkit.getWorld("world"), Respawn_X, Respawn_Y, Respawn_Z));
      }, 1L);
  }
}
