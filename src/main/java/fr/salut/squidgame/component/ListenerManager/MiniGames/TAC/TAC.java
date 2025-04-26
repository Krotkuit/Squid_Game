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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TAC implements Listener {

    SquidGame plugin = SquidGame.getInstance();

    private static final List<UUID> teamCorde1 = TACCommand.getTeamCorde1();
    private static final List<UUID> teamCorde2 = TACCommand.getTeamCorde2();
    private static final HashMap<UUID, Integer> clickCounts = new HashMap<>();

    @Getter
    static List<UUID> ignoredClicker = new ArrayList<>();
    @Setter
    static int clickTeam1 = 0;
    @Setter
    static int clickTeam2 = 0;

    public static void CPS() {
        // Reset CPS chaque seconde
        new BukkitRunnable() {
            @Override
            public void run() {
                if (SquidGame.getInstance().getTacState().equals(TACState.OFF)){
                    cancel();
                }
                clickCounts.clear();
            }
        }.runTaskTimer(SquidGame.getInstance(), 0L, 20L); // 20 ticks = 1 seconde
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e){

        Player player = e.getPlayer();
        if (plugin.getTacState().equals(TACState.ON)){

            if (!e.getAction().isLeftClick())return;
            if (ignoredClicker.contains(player.getUniqueId())) return;

            clickCounts.put(player.getUniqueId(), clickCounts.getOrDefault(player.getUniqueId(), 0) + 1);
            int cps = clickCounts.get(player.getUniqueId());

            if (cps > 15) {
                Bukkit.broadcastMessage("⚠️ " + player.getName() + " a " + cps + " CPS !");
            }

            if (cps<20){
                if (player.getScoreboardTags().contains("Corde1")){
                    clickTeam1++;
                    System.out.println(clickTeam1);
                }
                if (player.getScoreboardTags().contains("Corde2")){
                    clickTeam2++;
                    System.out.println(clickTeam2);
                }
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

    public static void startDetection(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (SquidGame.getInstance().getTacState().equals(TACState.OFF)){
                    cancel();
                }
                if (clickTeam1 > clickTeam2) {
                    for (Player player : SquidGame.getInstance().getServer().getOnlinePlayers()) {
                        if (!player.getScoreboardTags().contains("Corde2")) continue;
                        if (!ignoredClicker.contains(player.getUniqueId())) {
                            player.getLocation().add(0.1, 0, 0);
                        }
                        Block block = player.getLocation().clone().add(0, -1, 0).getBlock();
                        if (block.getType().equals(Material.AIR)){
                            if (!ignoredClicker.contains(player.getUniqueId())){
                                player.setGravity(false);

                                TACCommand.getTeamCorde1().remove(player.getUniqueId());
                                for (UUID uuid : ignoredClicker){
                                    Player playerInTeam = Bukkit.getPlayer(uuid);
                                    if (playerInTeam==null)return;
                                    if (playerInTeam.getScoreboardTags().contains("Corde1")){
                                        playerInTeam.getLocation().add(0, -2, 0);
                                    }
                                }
                                player.getLocation().add(0, -2, 0);
                                ignoredClicker.add(player.getUniqueId());

                            }
                        }
                    }
                } else if (clickTeam1 < clickTeam2){
                    for (Player player : SquidGame.getInstance().getServer().getOnlinePlayers()) {
                        if (!player.getScoreboardTags().contains("Corde1")) continue;
                        if (!ignoredClicker.contains(player.getUniqueId())) {
                            player.getLocation().add(-0.1, 0, 0);
                        }
                        Block block = player.getLocation().clone().add(0, -1, 0).getBlock();
                        if (block.getType().equals(Material.AIR)){
                            if (!ignoredClicker.contains(player.getUniqueId())){
                                player.setGravity(false);

                                TACCommand.getTeamCorde2().remove(player.getUniqueId());
                                for (UUID uuid : ignoredClicker){
                                    Player playerInTeam = Bukkit.getPlayer(uuid);
                                    if (playerInTeam==null)return;
                                    if (playerInTeam.getScoreboardTags().contains("Corde2")){
                                        playerInTeam.getLocation().add(0, -2, 0);
                                    }
                                }
                                player.getLocation().add(0, -2, 0);
                                ignoredClicker.add(player.getUniqueId());
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
        }.runTaskTimer(SquidGame.getInstance(), 0, 20*3);// toutes les seconds ( 20 ticks = 0.33... s)
    }
}