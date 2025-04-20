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

    if (player.getScoreboardTags().contains("md") && !team.getName().equalsIgnoreCase("garde") && !team.getName().equalsIgnoreCase("mort")) {
      //player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Vous avez bougé !"));

      player.getScoreboardTags().remove("md");

      Bukkit.getScheduler().runTaskLater(plugin, () -> {
        player.setHealth(0);

        // Joue un son de flèche tirée à tous les autres joueurs
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
          // Joue le son de la flèche en direction du joueur tué
          otherPlayer.playSound(player.getLocation(), "item.crossbow.shoot", 1.0f, 1.0f);
        }
      }, 100);
    }
  }
}
