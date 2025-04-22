package fr.salut.squidgame.component.ListenerManager.MiniGames.CM;

import fr.salut.squidgame.SquidGame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CMManager {
    static int[] zone = {161, 51, 118, 8 };
    private static final List<Location> chairsLoc = new ArrayList<>();

    public static void putChairs(){
        int chairs = 0;

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getMainScoreboard();
        Team team = scoreboard.getTeam("joueur");

        for (Player player : SquidGame.getInstance().getServer().getOnlinePlayers()){
            if (player.getScoreboard().getTeams().contains(team)){
                chairs += 1;
            }
        }

        chairs = (int) Math.floor(chairs*0.7);

        while (chairs > 0){
            generateRandomPos();
            chairs -= 1;
        }

        for (Location loc : chairsLoc){
            double x = loc.getX();
            double y = loc.getY();
            double z = loc.getZ();
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "setblock " + x + " " + y + " " + z + " cfm:stripped_oak_chair");
        }
    }

    public static void removeChair(){
        for (Location loc : chairsLoc){
            double x = loc.getX();
            double y = loc.getY();
            double z = loc.getZ();
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "setblock " + x + " " + y + " " + z + " air");
            chairsLoc.remove(loc);
        }
    }

    private static void generateRandomPos(){
        int x = new Random().nextInt(Math.min(zone[0], zone[2]), Math.max(zone[0], zone[2]));
        int y = -56;
        int z = new Random().nextInt(Math.min(zone[1], zone[3]), Math.max(zone[1], zone[3]));

        Location location = new Location(Bukkit.getWorld("world"), x, y, z);
        if (chairsLoc.contains(location)){
            generateRandomPos();
        } else {
            chairsLoc.add(location);
        }
    }
}
