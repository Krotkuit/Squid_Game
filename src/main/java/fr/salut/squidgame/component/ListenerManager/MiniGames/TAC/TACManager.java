package fr.salut.squidgame.component.ListenerManager.MiniGames.TAC;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.ListenerManager.intance.TeamManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
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
    private static final World TACW = Bukkit.getWorld("worlds/squidgame/tac");

    private static final List<Location> team1Locs = List.of(
            new Location(TACW, -22.5, 151, -126.5, -90, 0),
            new Location(TACW, -21.5, 151, -128.5, -90, 0),
            new Location(TACW, -20.5, 151, -126.5, -90, 0),
            new Location(TACW, -19.5, 151, -128.5, -90, 0),
            new Location(TACW, -18.5, 151, -126.5, -90, 0),
            new Location(TACW, -17.5, 151, -128.5, -90, 0),
            new Location(TACW, -16.5, 151, -126.5, -90, 0),
            new Location(TACW, -15.5, 151, -128.5, -90, 0),
            new Location(TACW, -14.5, 151, -126.5, -90, 0),
            new Location(TACW, -13.5, 151, -128.5, -90, 0)
    );

    private static final List<Location> team2Locs = List.of(
            new Location(TACW, 21.5, 151, -128.5, 90, 0),
            new Location(TACW, 20.5, 151, -126.5, 90, 0),
            new Location(TACW, 19.5, 151, -128.5, 90, 0),
            new Location(TACW, 18.5, 151, -126.5, 90, 0),
            new Location(TACW, 17.5, 151, -128.5, 90, 0),
            new Location(TACW, 16.5, 151, -126.5, 90, 0),
            new Location(TACW, 15.5, 151, -128.5, 90, 0),
            new Location(TACW, 14.5, 151, -126.5, 90, 0),
            new Location(TACW, 13.5, 151, -128.5, 90, 0),
            new Location(TACW, 12.5, 151, -126.5, 90, 0)
    );

    static BukkitRunnable gameTask;

    public static void startTAC(){

        if (playersTeam1.isEmpty() || playersTeam2.isEmpty()){
            SquidGame.getInstance().getLogger().warning("Teams not loaded, pls use ");
            return;
        }

        TACW.setDifficulty(Difficulty.EASY);

        for (Player player : playersTeam1) {
            player.setGravity(false);
            player.setFoodLevel(2);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, PotionEffect.INFINITE_DURATION, 255, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, PotionEffect.INFINITE_DURATION, 150, true, false));
        }
        for (Player player : playersTeam2){
            player.setGravity(false);
            player.setFoodLevel(2);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, PotionEffect.INFINITE_DURATION, 255, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, PotionEffect.INFINITE_DURATION, 150, true, false));
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


    public static void placePlayers() {

        if (team1 == null || team2 == null){
            SquidGame.getInstance().getLogger().warning("Team 1 or Team 2 isn't set yet !");
            return;
        }

        playersTeam1.addAll(TeamManager.getTeamOnlinePlayers(team1));
        playersTeam2.addAll(TeamManager.getTeamOnlinePlayers(team2));

        // Créer des copies modifiables et INVERSER l'ordre
        List<Location> availableTeam1Locs = new ArrayList<>(team1Locs);
        List<Location> availableTeam2Locs = new ArrayList<>(team2Locs);

        Collections.reverse(availableTeam1Locs);
        Collections.reverse(availableTeam2Locs);

        // Team 1 : assigner les joueurs aux positions les plus proches
        assignPlayersToClosestLocations(playersTeam1, availableTeam1Locs);

        // Team 2 : assigner les joueurs aux positions les plus proches
        assignPlayersToClosestLocations(playersTeam2, availableTeam2Locs);
    }

    private static void assignPlayersToClosestLocations(List<Player> players, List<Location> locations) {
        List<Player> unassignedPlayers = new ArrayList<>(players);

        // Parcourir chaque position de 1 à 10
        for (Location location : locations) {
            if (unassignedPlayers.isEmpty()) break;

            Player closestPlayer = null;
            double minDistance = Double.MAX_VALUE;

            // Trouver le joueur le plus proche de cette position
            for (Player player : unassignedPlayers) {
                double distance = player.getLocation().distance(location);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPlayer = player;
                }
            }

            // Téléporter le joueur le plus proche et le retirer de la liste
            if (closestPlayer != null) {
                closestPlayer.teleport(location);
                unassignedPlayers.remove(closestPlayer);
            }
        }

        // Si plus de 10 joueurs, répartir les restants sur les positions déjà utilisées
        if (!unassignedPlayers.isEmpty()) {
            int index = 0;
            for (Player player : unassignedPlayers) {
                player.teleport(locations.get(index % locations.size()));
                index++;
            }
        }
    }

    public static void stopTAC(Team looser, Team winner){
        System.out.println("stop");
        gameTask.cancel();
        for (Player teamPlayer : TeamManager.getTeamOnlinePlayers(looser)){
            teamPlayer.setGravity(true);
            teamPlayer.setFoodLevel(20);
            teamPlayer.removePotionEffect(PotionEffectType.SLOW);
            teamPlayer.removePotionEffect(PotionEffectType.JUMP);
        }
        for (Player teamPlayer : TeamManager.getTeamOnlinePlayers(winner)){
            teamPlayer.setGravity(true);
            teamPlayer.setFoodLevel(20);
            teamPlayer.removePotionEffect(PotionEffectType.SLOW);
            teamPlayer.removePotionEffect(PotionEffectType.JUMP);
            if (winner.equals(team1)) teamPlayer.setVelocity(endWest);
            if (winner.equals(team2)) teamPlayer.setVelocity(endEast);
        }
        TACW.setDifficulty(Difficulty.PEACEFUL);
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
    public void onPlayerClick(PlayerAnimationEvent event){
        if (!SquidGame.getInstance().getTacState().equals(TACState.ON)) return;

        Player player = event.getPlayer();

        if (!playersTeam1.contains(player) && !playersTeam2.contains(player)) return;

        // Vérifier si le joueur regarde un bloc (raytrace)
        Block targetBlock = player.getTargetBlockExact(3); // 3 blocs de portée
        if (targetBlock == null || targetBlock.getType().isAir()) return;
        if (targetBlock.getType() != Material.valueOf("COPYCATS_COPYCAT_SHAFT")) return;

        Team team = TeamManager.getTeam(player);
        if (team != null){
            if (team.equals(team1)) team1Click++;
            if (team.equals(team2)) team2Click++;
            showClick();
        }
    }

    @EventHandler
    public void onFoodRegen(FoodLevelChangeEvent event){
        if (!SquidGame.getInstance().getTacState().equals(TACState.ON)) return;

        // Vérifier que c'est un joueur
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        // Vérifier que le joueur est dans une des équipes
        if (!playersTeam1.contains(player) && !playersTeam2.contains(player)) return;

        event.setFoodLevel(2);
        event.setCancelled(true);
    }

    @EventHandler
    public void onPLayerMove(PlayerMoveEvent event){
        if (!SquidGame.getInstance().getTacState().equals(TACState.ON)) return;
        Player player = event.getPlayer();
        if (!playersTeam1.contains(player) && !playersTeam2.contains(player)) return;
        player.setGravity(!new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - 0.7, player.getLocation().getZ()).getBlock().getType().isAir());
    }
}