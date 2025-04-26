package fr.salut.squidgame.component.ListenerManager.MiniGames.TAC;

import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.commands.games.TACCommand;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TAC implements Listener {

    SquidGame plugin = SquidGame.getInstance();

    private final List<UUID> teamCorde1 = TACCommand.getTeamCorde1();
    private final List<UUID> teamCorde2 = TACCommand.getTeamCorde2();

    @Getter
    static List<UUID> ignoredClicker = new ArrayList<>();
    @Setter
    static int clickTeam1 = 0;
    @Setter
    static int clickTeam2 = 0;

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e){

        Player player = e.getPlayer();
        if (plugin.getTacState().equals(TACState.ON)){

            if (!e.getAction().equals(Action.LEFT_CLICK_BLOCK))return;
            if (ignoredClicker.contains(player.getUniqueId())) return;
            if (player.getScoreboardTags().contains("Corde1")){
                clickTeam1++;
            }
            if (player.getScoreboardTags().contains("Corde2")){
                clickTeam2++;
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        Player player = e.getPlayer();
        if (plugin.getTacState().equals(TACState.ON)){

            if (ignoredClicker.contains(player.getUniqueId())){
                if (e.getFrom()!=e.getTo()){
                    e.setCancelled(true);
                }
            }
        }
    }

    public void startDetection(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (SquidGame.getInstance().getTacState().equals(TACState.OFF)){
                    cancel();
                }
                if (clickTeam1 > clickTeam2) {
                    for (Player player : plugin.getServer().getOnlinePlayers()) {
                        if (player.getScoreboardTags().contains("Corde1") && !ignoredClicker.contains(player.getUniqueId())) {
                            plugin.teleportPlayer(player, player.getX() + 0.1, player.getY(), player.getZ());
                        }
                        if (player.getScoreboardTags().contains("Corde2") && !ignoredClicker.contains(player.getUniqueId())) {
                            plugin.teleportPlayer(player, player.getX() - 0.1, player.getY(), player.getZ());
                        }
                        Block block = Bukkit.getWorld("world").getBlockAt((int) player.getX(), (int)player.getY()-1, (int)player.getZ());
                        if (block.getType().equals(Material.AIR)){
                            if (!ignoredClicker.contains(player.getUniqueId())){
                                SquidGame.getInstance().teleportPlayer(player, player.getX(), player.getY()-2, player.getZ());
                                player.setGravity(false);
                                if (player.getScoreboardTags().contains("Corde1")){
                                    TACCommand.getTeamCorde1().remove(player.getUniqueId());
                                }
                                if (player.getScoreboardTags().contains("Corde2")){
                                    TACCommand.getTeamCorde2().remove(player.getUniqueId());
                                }
                                ignoredClicker.add(player.getUniqueId());
                            }
                            if (player.getScoreboardTags().contains("Corde1")){
                                for (UUID uuid : ignoredClicker){
                                    Player playerInTeam = Bukkit.getPlayer(uuid);
                                    if (playerInTeam==null)return;
                                    if (playerInTeam.getScoreboardTags().contains("Corde1")){
                                        SquidGame.getInstance().teleportPlayer(playerInTeam, player.getX(), player.getY()-2, player.getZ());
                                    }
                                }
                            }

                            else if (player.getScoreboardTags().contains("Corde2")){
                                for (UUID uuid : ignoredClicker){
                                    Player playerInTeam = Bukkit.getPlayer(uuid);
                                    if (playerInTeam==null)return;
                                    if (playerInTeam.getScoreboardTags().contains("Corde2")){
                                        SquidGame.getInstance().teleportPlayer(playerInTeam, player.getX(), player.getY()-2, player.getZ());
                                    }
                                }
                            }
                        }
                    }
                }
                if (TACCommand.getTeamCorde1().isEmpty()){
                    for (UUID uuid : teamCorde1){
                        Player player = Bukkit.getPlayer(uuid);
                        if (player!=null){
                            player.setGravity(true);
                            player.setHealth(1);
                        }
                    }
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tac OFF");
                    cancel();
                } else if (TACCommand.getTeamCorde2().isEmpty()) {
                    for (UUID uuid : teamCorde2){
                        Player player = Bukkit.getPlayer(uuid);
                        if (player!=null){
                            player.setGravity(true);
                            player.setHealth(1);
                        }
                    }
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tac OFF");
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20*3);// toutes les seconds ( 20 ticks = 0.33... s)
    }
}