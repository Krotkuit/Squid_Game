package fr.salut.squidgame.component.ListenerManager.MiniGames.TTB;

import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.ListenerManager.intance.TeamManager;
import fr.salut.squidgame.component.commands.EpreuveCommand;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class TTBManager implements Listener {

    @Getter
    public static List<Team> teams = new ArrayList<>();

    @Getter
    public static Map<Team, UUID> bombers = new HashMap<>();

    private static final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    private static final List<String> allowedTeams = List.of(
        "noir", "gris_fonce", "gris_clair", "blanc", "marron", "rose", "magenta",
        "indigo", "bleu_marine", "bleu_roi", "bleu_ciel", "cyan", "vert_profond", "vert_citron",
        "jaune", "or", "orange", "saumon", "bordeaux", "rouge"
    );
    private final Map<Team, BukkitRunnable> activeTask = new HashMap<>();

    private final Sound exploseSound = Sound.ENTITY_GENERIC_EXPLODE;
    private final Sound tickSound = Sound.BLOCK_NOTE_BLOCK_HAT;

    public static void addTeams () {
        teams.clear();

        for (Team team : scoreboard.getTeams()){

            if (allowedTeams.contains(team.getName()) && TeamManager.hasTeamOnlinePlayers(team)){
                teams.add(team);
            }
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
            if (bombers.containsKey(team)) bombers.replace(team, target.getUniqueId());
            else bombers.put(team, target.getUniqueId());
        }
    }

    public static void stopGame(){
        for (UUID uuid : bombers.values()){
            Bukkit.getPlayer(uuid).getInventory().remove(Material.TNT);
        }
        bombers.clear();
    }

    @EventHandler
    public void onServerChronometerEnd(Chronometer.ServerChronometerEndEvent event){
        if (!EpreuveCommand.getEpreuve().equals("TTB")) return;
        if (allowedTeams.contains(event.getGroup())){
            Team team = scoreboard.getTeam(event.getGroup());
            Team joueur = scoreboard.getTeam("joueur");
            if (team!=null){
                Player bomber = Bukkit.getPlayer(bombers.get(team));
                activeTask.get(team).cancel();
                activeTask.remove(team);

                if (bomber != null){
                    team.removeEntry(bomber.getName());
                    joueur.addEntry(bomber.getName());

                    bomber.getInventory().setHelmet(new ItemStack(Material.AIR));
                    bomber.getWorld().playSound(bomber.getLocation(), exploseSound, 10, 1);
                    bomber.getWorld().createExplosion(bomber.getLocation(), 0, false, false);
                    bomber.setHealth(0);
                }

                if (TeamManager.getTeamOnlinePlayers(team).size() > 5 - TTBCommand.maxDead){
                    UUID newBomber = TeamManager.getTeamOnlinePlayers(team).get(TTBCommand.random.nextInt(TeamManager.getTeamOnlinePlayers(team).size())).getUniqueId();
                    giveBombTo(Bukkit.getPlayer(newBomber), null, team);
                    for (Player player : TeamManager.getTeamOnlinePlayers(team)) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stopsound @a[team=" + team.getName() + "]");
                        player.sendTitle("§6Next Round", "§c" + Bukkit.getPlayer(newBomber).getName() + " à la bombe !");
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound squidgame:ttb_musique record @a[team=" + team.getName() + "]");
                    Chronometer.startServerChronometer(null, team, team.getName(), TTBCommand.random.nextInt(90, 150), ChronometerType.ACTION_BAR, "%null%", ChronometerType.ACTION_BAR, "§cBOUM !");
                } else {
                    bombers.remove(team);
                    for (Player player : TeamManager.getTeamOnlinePlayers(team)) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stopsound @a[team=" + team.getName() + "]");
                        player.sendMessage("§aVeuillez attendre que toutes les équipes aient fini ");
                    }
                    Team gardeTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("garde");
                    for (Player garde : TeamManager.getTeamOnlinePlayers(gardeTeam)){
                        garde.sendMessage("§eEquipe "+ team.getName() +"§a à fini");
                        if (bombers.isEmpty()) garde.sendMessage("§a Toutes les équipes ont fini !");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onServerChronometerTick(Chronometer.ChronometerTimeChangeEvent event){
        if (!EpreuveCommand.getEpreuve().equals("TTB")) return;
        if (allowedTeams.contains(event.getGroup())){
            Team team = scoreboard.getTeam(event.getGroup());
            if (team!=null){
                if (event.getTime() > 45){
                    for (Player player : TeamManager.getTeamOnlinePlayers(team)) {
                        player.playSound(player, tickSound, 8, 1);
                    }
                }
                if (event.getTime() == 45 ) tickSoundFast(team);
                if (event.getTime() == 5) tickSoundVeryFast(team);

                Player bomber = Bukkit.getPlayer(bombers.get(team));
                if (bomber != null) bomber.sendActionBar("§cVous avez la bombe !");
            }
        }
    }

    @EventHandler
    public void onPlayerHitPlayer(EntityDamageByEntityEvent e) {
        if (!EpreuveCommand.getEpreuve().equals("TTB")) return;
        if (e.getDamager() instanceof Player player) {
            if (e.getEntity() instanceof Player target) {
                Team team = scoreboard.getPlayerTeam(player);
                Team targetTeam = scoreboard.getPlayerTeam(target);

                if (targetTeam == null || team == null) return;
                if (team.getName().equals(targetTeam.getName())){
                    if (bombers.get(team).equals(player.getUniqueId())){
                        giveBombTo(target, player, team);
                    }
                }
            }
        }
    }

    private void tickSoundFast(Team team){
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : TeamManager.getTeamOnlinePlayers(team)) player.playSound(player, tickSound, 8, 1);
            }
        };
        task.runTaskTimer(SquidGame.getInstance(), 0, 10);

        activeTask.put(team, task);
    }

    private void tickSoundVeryFast(Team team){

        activeTask.get(team).cancel();

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : TeamManager.getTeamOnlinePlayers(team)) player.playSound(player, tickSound, 8, 1);
            }
        };
        task.runTaskTimer(SquidGame.getInstance(), 0, 5);

        activeTask.replace(team, task);
    }}
