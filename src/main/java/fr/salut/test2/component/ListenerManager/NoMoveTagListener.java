package fr.salut.test2.component.ListenerManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class NoMoveTagListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getPlayer().getScoreboardTags().contains("no_move")) {

            if (event.getFrom().distanceSquared(event.getTo()) > 0) event.setTo(event.getFrom().setDirection(event.getTo().getDirection()));
        }
    }
}
