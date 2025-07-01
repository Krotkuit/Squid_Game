package fr.salut.squidgame.component.ListenerManager;

import lombok.Setter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

public class MoveDetectListener implements Listener {

  @Setter
  private static boolean enabled = false;

  private final JavaPlugin plugin;

  // Le constructeur reçoit le plugin pour initialiser correctement l'instance
  public MoveDetectListener(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    Player player = event.getPlayer();

    if (!enabled) return;

    Team team = player.getScoreboard().getEntryTeam(player.getName());

    if (player.getScoreboardTags().contains("md")
        && !team.getName().equalsIgnoreCase("garde")
        && !team.getName().equalsIgnoreCase("mort")) {

      // Vérifie si le joueur a bougé de manière significative
      double deltaX = Math.abs(event.getFrom().getX() - event.getTo().getX());
      double deltaY = Math.abs(event.getFrom().getY() - event.getTo().getY());
      double deltaZ = Math.abs(event.getFrom().getZ() - event.getTo().getZ());

      // Vérifie si la caméra a bougé de manière significative
      float deltaYaw = Math.abs(event.getFrom().getYaw() - event.getTo().getYaw());
      float deltaPitch = Math.abs(event.getFrom().getPitch() - event.getTo().getPitch());

      double movementTolerance = 0.01; // Seuil de tolérance pour le mouvement
      float cameraTolerance = 2.0f;   // Seuil de tolérance pour la caméra (en degrés)

      if (deltaX > movementTolerance || deltaY > movementTolerance || deltaZ > movementTolerance
          || deltaYaw > cameraTolerance || deltaPitch > cameraTolerance) {
        player.getScoreboardTags().remove("md");

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
          player.setHealth(0);

          // Joue un son de flèche tirée à tous les autres joueurs
          for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            otherPlayer.playSound(player.getLocation(), "item.crossbow.shoot", 1.0f, 1.0f);
          }
        }, 100);
      }
    }
  }
}
