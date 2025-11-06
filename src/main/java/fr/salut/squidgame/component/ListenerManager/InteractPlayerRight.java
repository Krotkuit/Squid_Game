package fr.salut.squidgame.component.ListenerManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.List;

public class InteractPlayerRight implements Listener {

  @EventHandler
  private void onBlockInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    Team team = Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(player.getName());

    if (team != null && team.getName().equalsIgnoreCase("garde")) return;

    World world = Bukkit.getWorld(player.getUniqueId());
    if (world != null && !world.getName().equalsIgnoreCase("worlds/SquidGame/Dortoir")) return;

    List<Material> materialsInterdits = Arrays.asList(
        Material.LEVER,
        Material.SHULKER_BOX
    );

    if (event.getClickedBlock() != null && materialsInterdits.contains(event.getClickedBlock().getType())) {
      event.setCancelled(true);
      player.sendActionBar("§cTu n'es pas autoriser à utiliser ça !");
    }
  }
}
