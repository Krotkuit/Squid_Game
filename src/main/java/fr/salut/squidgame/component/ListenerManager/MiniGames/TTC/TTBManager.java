package fr.salut.squidgame.component.ListenerManager.MiniGames.TTC;

import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.commands.games.TTBCommand;
import fr.salut.squidgame.utils.chronometer.Chronometer;
import fr.salut.squidgame.utils.chronometer.ChronometerType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;

public class TTBManager implements Listener {

    @Getter
    public static List<Team> teams = new ArrayList<>();

    @Getter
    public static Map<Team, UUID> bombers = new HashMap<>();

    private static final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    private static final List<String> allowedTeams = List.of("dark_red", "rouge", "orange", "jaune", "vert_clair", "vert", "vert_profond",
            "bleu", "cyan", "bleu_marine", "bleu_profond", "violet", "magenta", "rose", "blanc", "light_gray", "gris", "dark_gray", "black", "marron");

    private final Sound exploseSound = Sound.ENTITY_GENERIC_EXPLODE;
    private final Sound tickSound = Sound.BLOCK_NOTE_BLOCK_HAT;

    public static void addTeams () {
        teams.clear();

        for (Team team : scoreboard.getTeams()){
            if (allowedTeams.contains(team.getName()) && !team.getEntries().isEmpty()) teams.add(team);
        }
    }

    public static void giveBombTo(Player target, Player bomber, Team team){
        if (target != null){
            target.sendActionBar("§cVous avez la bombes !");
            target.getInventory().setHelmet(new ItemStack(Material.TNT));
            if (bomber != null){
                bomber.sendActionBar("Vous n'avez plus la bombe");
                bomber.getInventory().setHelmet(new ItemStack(Material.AIR));
            }
            bombers.replace(team, target.getUniqueId());
        }
    }

    @EventHandler
    public void onServerChronometerEnd(Chronometer.ServerChronometerEndEvent event){
        if (allowedTeams.contains(event.getGroup())){
            Team team = scoreboard.getTeam(event.getGroup());
            if (team!=null){
                Player bomber = Bukkit.getPlayer(bombers.get(team));

                if (bomber != null){
                    team.removeEntry(bomber.getName());

                    bomber.getInventory().setHelmet(new ItemStack(Material.AIR));
                    bomber.getWorld().playSound(bomber.getLocation(), exploseSound, 10, 1);
                    bomber.getWorld().createExplosion(bomber.getLocation(), 0, false, false);
                    bomber.setHealth(0);
                }

                if (team.getPlayers().size() > 5 - TTBCommand.maxDead){
                    UUID newBomber = team.getPlayers().stream().toList().get(TTBCommand.random.nextInt(team.getPlayers().size())).getUniqueId();
                    giveBombTo(Bukkit.getPlayer(newBomber), null, team);
                    Chronometer.startServerChronometer(null, team, team.getName(), TTBCommand.random.nextInt(90, 150), ChronometerType.ACTION_BAR, "%null%", ChronometerType.ACTION_BAR, "§cBOUM !");
                }
            }
        }
    }

    @EventHandler
    public void onServerChronometerTick(Chronometer.ChronometerTimeChangeEvent event){
        if (allowedTeams.contains(event.getGroup())){
            Team team = scoreboard.getTeam(event.getGroup());
            if (team!=null){
                for (Player player : SquidGame.getInstance().getServer().getOnlinePlayers()) {
                    if (team.hasPlayer(player)) player.playSound(player, tickSound, 8, 1);
                }
                Player bomber = Bukkit.getPlayer(bombers.get(team));
                if (bomber != null) bomber.sendActionBar("§cVous avez la bombe !");;
            }
        }
    }

    @EventHandler
    public void onPlayerGiveBomb(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        Vector direction = player.getLocation().getDirection().normalize();

        RayTraceResult result = player.getWorld().rayTraceEntities(
                player.getEyeLocation(),
                direction,
                1000,
                entity -> entity instanceof Player && !entity.equals(player)
        );

        if (result != null && result.getHitEntity() instanceof Player target) {
            Team team = scoreboard.getPlayerTeam(player);
            Team targetTeam = scoreboard.getPlayerTeam(target);

            if (targetTeam == null || team == null) return;
            if (team.equals(targetTeam)){
                if (bombers.get(team).equals(player.getUniqueId())){
                    giveBombTo(target, player, team);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerHitPlayer(EntityDamageByEntityEvent e) {

        if (e.getDamager() instanceof Player player) {
            if (e.getEntity() instanceof Player target) {
                Team team = scoreboard.getPlayerTeam(player);
                Team targetTeam = scoreboard.getPlayerTeam(target);

                if (targetTeam == null || team == null) return;
                if (team.equals(targetTeam)){
                    if (bombers.get(team).equals(player.getUniqueId())){
                        giveBombTo(target, player, team);
                    }
                }
            }
        }
    }
}
