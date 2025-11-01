package fr.salut.squidgame.component.ListenerManager.MiniGames.TAC;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.ListenerManager.intance.TeamManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.*;

public class TACManager implements Listener {

    public static double team1Click = 0;
    public static double team2Click = 0;

    public static Team team1;
    public static Team team2;

    private static final Vector west = new Vector(-1, 0, 0).normalize().multiply(0.1);
    private static final Vector east = new Vector(1, 0, 0).normalize().multiply(0.1);
    private static final Vector upeast = new Vector(1, 1, 0).normalize().multiply(0.1);
    private static final Vector upwest = new Vector(-1, 1, 0).normalize().multiply(0.1);
    private static final Vector down = new Vector(0, -0.5, 0).normalize().multiply(0.1);
    private static final Vector endWest = new Vector(-2, 0, 0).normalize().multiply(0.1);
    private static final Vector endEast = new Vector(2, 0, 0).normalize().multiply(0.1);

    public static List<Player> playersTeam1 = new ArrayList<>();
    public static List<Player> playersTeam2 = new ArrayList<>();

    static BukkitRunnable gameTask;

    public static void startTAC(){

        playersTeam1.addAll(TeamManager.getTeamOnlinePlayers(team1));
        playersTeam2.addAll(TeamManager.getTeamOnlinePlayers(team2));

        for (Player player : playersTeam1) {
            player.setGravity(false);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, PotionEffect.INFINITE_DURATION, 255, true));
        }
        for (Player player : playersTeam2){
            player.setGravity(false);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, PotionEffect.INFINITE_DURATION, 255, true));
        }

        gameTask = new BukkitRunnable() {
            @Override
            public void run() {

                if (SquidGame.getInstance().getTacState().equals(TACState.OFF)) cancel();
                if (SquidGame.getInstance().getTacState().equals(TACState.STOP)) return;

                if (team1Click > 50 || team2Click > 50){
                    for (Player player : TeamManager.getTeamOnlinePlayers(team1)){
                        if (team1Click<team2Click) pushForward(player, team1);
                        if (team2Click<team1Click) pushBackward(player, team1);
                    }
                    for (Player player : TeamManager.getTeamOnlinePlayers(team2)){
                        if (team1Click<team2Click) pushBackward(player, team2);
                        if (team2Click<team1Click) pushForward(player, team2);
                    }
                    // System.out.println("team1 : " + team1Click + " | team2 : " + team2Click);
                }

            }
        }; gameTask.runTaskTimer(SquidGame.getInstance(), 0, 20);
    }

    public static void stopTAC(Team looser, Team winner){
        System.out.println("stop");
        gameTask.cancel();
        for (Player teamPlayer : TeamManager.getTeamOnlinePlayers(looser)){
            teamPlayer.setGravity(true);
            teamPlayer.removePotionEffect(PotionEffectType.SLOW);
        }
        for (Player teamPlayer : TeamManager.getTeamOnlinePlayers(winner)){
            teamPlayer.setGravity(true);
            teamPlayer.removePotionEffect(PotionEffectType.SLOW);
            if (winner.equals(team1)) teamPlayer.setVelocity(endWest);
            if (winner.equals(team2)) teamPlayer.setVelocity(endEast);
        }
        SquidGame.getInstance().setTacState(TACState.OFF);
    }

    private static void pushForward(Player player, Team team) {
        if (new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - 0.7, player.getLocation().getZ()).getBlock().getType().isAir()){
            if (playersTeam1.contains(player)) player.teleport(new Location(player.getWorld(), player.getLocation().getX() + 0.35, player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
            if (playersTeam2.contains(player)) player.teleport(new Location(player.getWorld(), player.getLocation().getX() - 0.35, player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
            if (team.equals(team1)) playersTeam1.remove(player);
            if (team.equals(team2)) playersTeam2.remove(player);

            player.setVelocity(down);
        } else {
            if (team.equals(team1)){
                player.setVelocity(east);
            }
            if (team.equals(team2)) {
                player.setVelocity(west);
            }
        }
        if (playersTeam1.isEmpty() && team==team1){
            stopTAC(team1, team2);
            return;
        }
        if (playersTeam2.isEmpty() && team==team2){
            stopTAC(team2, team1);
        }
    }

    private static void pushBackward(Player player, Team team) {
        if (new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - 0.7, player.getLocation().getZ()).getBlock().getType().isAir()){
            if (team.equals(team1)) player.setVelocity(upwest);
            if (team.equals(team2)) player.setVelocity(upeast);
            return;
        }
        if (team.equals(team1)){ // west
            if (!playersTeam1.contains(player)) playersTeam1.add(player);
            player.setVelocity(west);
        }
        if (team.equals(team2)) { // east
            if (!playersTeam2.contains(player)) playersTeam2.add(player);
            player.setVelocity(east);
        }
    }

    private void showClick(){
        for (Player player : TeamManager.getTeamOnlinePlayers(team1)) {
            if (team1Click < team2Click) player.sendActionBar("§6Nombre de click : §l§c" + team1Click + "§r§l | §r§6différnce : §l§c" + (team1Click-team2Click));
            if (team2Click < team1Click) player.sendActionBar("§6Nombre de click : §l§a" + team1Click + "§r§l | §r§6différnce : §l§a+" + (team1Click-team2Click));
        }
        for (Player player : TeamManager.getTeamOnlinePlayers(team2)) {
            if (team1Click < team2Click) player.sendActionBar("§6Nombre de click : §l§a" + team2Click + "§r§l | §r§6différnce : §l§a+" + (team2Click-team1Click));
            if (team2Click < team1Click) player.sendActionBar("§6Nombre de click : §l§c" + team2Click + "§r§l | §r§6différnce : §l§c" + (team2Click-team1Click));
        }
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event){
        if (!SquidGame.getInstance().getTacState().equals(TACState.ON)) return;
        Player player = event.getPlayer();

        if (!playersTeam1.contains(player) && !playersTeam2.contains(player)) return;

        Team team = player.getScoreboard().getPlayerTeam(player);
        if (team!=null){
            if (team.equals(team1)) team1Click ++;
            if (team.equals(team2)) team2Click ++;
            showClick();
        }
    }

    @EventHandler
    public void onPLayerMove(PlayerMoveEvent event){
        if (!SquidGame.getInstance().getTacState().equals(TACState.ON)) return;
        Player player = event.getPlayer();
        if (!playersTeam1.contains(player) && !playersTeam2.contains(player)) return;
        player.setGravity(!new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - 0.7, player.getLocation().getZ()).getBlock().getType().isAir());
    }

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event){
        if (!SquidGame.getInstance().getTacState().equals(TACState.ON)) return;
        Player player = event.getPlayer();
        if (!playersTeam1.contains(player) && !playersTeam2.contains(player)) return;
        event.setCancelled(true);
    }
}