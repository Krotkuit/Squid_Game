package fr.salut.test2.component.ListenerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerRightListener {
    public void onPlayerDropItem(PlayerDropItemEvent event) {

        Player player = event.getPlayer();

        if (player.hasPermission("disabledrops")) {

            event.setCancelled(true);

        }

    }
}
