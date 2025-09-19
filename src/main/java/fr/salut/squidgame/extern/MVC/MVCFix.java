package fr.salut.squidgame.extern.MVC;

import fr.salut.squidgame.SquidGame;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MVCFix implements Listener {

    private final Map<UUID, GameMode> previousGamemodes = new HashMap<>();

    private final SquidGame plugin;

    public MVCFix(SquidGame plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World targetWorld = player.getWorld();

        previousGamemodes.put(player.getUniqueId(), player.getGameMode());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                if (player.getWorld().equals(targetWorld)) {
                    GameMode previous = previousGamemodes.get(player.getUniqueId());
                    if (previous != null) {
                        player.setGameMode(previous);
                    }
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 2L, 2L);

    }

}
