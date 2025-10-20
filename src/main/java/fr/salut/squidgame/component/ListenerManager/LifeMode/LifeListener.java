package fr.salut.squidgame.component.ListenerManager.LifeMode;

import fr.salut.squidgame.SquidGame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LifeListener implements Listener {

  private static LifeListener instance;

  private final Map<UUID, Integer> playerLives = new HashMap<>();
  private boolean lifeModeEnabled = false;
  public static int defaultLives = 2;

  public LifeListener() {
    instance = this; // Initialise l'instance statique
  }

  public static LifeListener getInstance() {
    return instance;
  }

  public void setLifeModeEnabled(boolean enabled) {
    this.lifeModeEnabled = enabled;

    if (!enabled) {
      // Met à jour les vies de tous les joueurs à 1
      for (UUID uuid : playerLives.keySet()) {
        Player player = SquidGame.getInstance().getServer().getPlayer(uuid);
        playerLives.put(uuid, 1);
        if (player != null && player.isOnline()) {
          updatePlayerXP(player, 1); // Met à jour la barre d'XP
        }
      }
      SquidGame.getInstance().getLogger().info("Mode de vie désactivé : tous les joueurs ont maintenant 1 vie.");
    } else {
      SquidGame.getInstance().getLogger().info("Mode de vie activé.");
    }
  }

  public void setDefaultLives(int lives) {
    this.defaultLives = lives;
  }

  public Map<UUID, Integer> getPlayerLives() {
    return playerLives;
  }

  @EventHandler
  public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();

    if (!playerLives.containsKey(uuid)) {
      int lives = lifeModeEnabled ? defaultLives : 1; // Si le mode de vie est désactivé, définir les vies à 1
      playerLives.put(uuid, lives);
    }
    updatePlayerXP(player, playerLives.get(uuid));
  }


  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    int lives = playerLives.getOrDefault(uuid, defaultLives); // Utilise une valeur par défaut
    if (playerLives.containsKey(uuid)) {
      lives = lives - 1;
      if (lives > 0) {
        playerLives.put(uuid, lives);
        updatePlayerXP(player, lives); // Met à jour le niveau d'XP
      } else {
        playerLives.remove(uuid);
        updatePlayerXP(player, 0);
      }
    }
  }

  @EventHandler
  public void onPlayerTeleport(org.bukkit.event.player.PlayerTeleportEvent event) {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    int lives = playerLives.getOrDefault(uuid, defaultLives); // Utilise une valeur par défaut
    if (playerLives.containsKey(uuid)) {
      playerLives.put(uuid, lives);
      updatePlayerXP(player, lives); // Met à jour le niveau d'XP
    }
  }

  @EventHandler
  public void onPlayerExpChange(PlayerExpChangeEvent event) {
    event.setAmount(0);
  }

  public void addPlayerWithDefaultLives(Player player) {
    UUID uuid = player.getUniqueId();
    int lives = lifeModeEnabled ? defaultLives : 1; // Si le mode de vie est désactivé, définir les vies à 1
    if (!playerLives.containsKey(uuid)) {
      playerLives.put(uuid, lives);
      updatePlayerXP(player, lives); // Met à jour la barre d'XP
    }
  }

  public void updatePlayerXP(Player player, int lives) {
    player.setExp(0.99f); // Remplie la barre d'XP
    player.setLevel(lives); // Met à jour le niveau d'XP au nombre de vies
  }

  public boolean isLifeModeEnabled() {
    return lifeModeEnabled;
  }
}