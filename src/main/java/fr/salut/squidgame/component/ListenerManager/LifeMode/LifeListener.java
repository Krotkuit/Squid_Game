package fr.salut.squidgame.component.ListenerManager.LifeMode;

import fr.salut.squidgame.SquidGame;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Map;

public class LifeListener implements Listener {

  private static LifeListener instance;

  private final Map<Player, Integer> playerLives = new HashMap<>();
  private boolean lifeModeEnabled = false;
  private int defaultLives = 2;

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
      for (Player player : playerLives.keySet()) {
        playerLives.put(player, 1);
        updatePlayerXP(player, 1); // Met à jour la barre d'XP
      }
      SquidGame.getInstance().getLogger().info("Mode de vie désactivé : tous les joueurs ont maintenant 1 vie.");
    } else {
      SquidGame.getInstance().getLogger().info("Mode de vie activé.");
    }
  }

  public void setDefaultLives(int lives) {
    this.defaultLives = lives;
  }

  public Map<Player, Integer> getPlayerLives() {
    return playerLives;
  }

  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent event) {
    Player player = event.getPlayer();
    int lives = playerLives.getOrDefault(player, defaultLives); // Utilise une valeur par défaut
    if (playerLives.containsKey(player)) {
      lives = lives - 1;
      if (lives > 0) {
        playerLives.put(player, lives);
        updatePlayerXP(player, lives); // Met à jour le niveau d'XP
      } else {
        playerLives.remove(player);
        updatePlayerXP(player, 0);
      }
    }
  }

  @EventHandler
  public void onPlayerExpChange(PlayerExpChangeEvent event) {
    event.setAmount(0);
  }

  public void addPlayerWithDefaultLives(Player player) {
    int lives = lifeModeEnabled ? defaultLives : 1; // Si le mode de vie est désactivé, définir les vies à 1
    if (!playerLives.containsKey(player)) {
      playerLives.put(player, lives);
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