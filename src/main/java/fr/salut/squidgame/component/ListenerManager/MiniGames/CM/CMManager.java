package fr.salut.squidgame.component.ListenerManager.MiniGames.CM;

import fr.salut.squidgame.SquidGame;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class CMManager {
    static int[] zone = {-22, -23, 21, -66};
    private static final List<Location> chairsLoc = new ArrayList<>();

    @Setter
    private static double chairProp = 0.95; // Valeur par défaut

  public static void putChairs(){
        int chairs = 0;

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getMainScoreboard();
        Team team = scoreboard.getTeam("joueur");

        for (Player player : SquidGame.getInstance().getServer().getOnlinePlayers()) {
            if (team.hasEntry(player.getName())) {
                chairs += 1;
            }
        }


        // Calcul du nombre de chaises
        if (chairProp > 0 && chairProp < 1) {
            chairs = (int) Math.floor(chairs * chairProp);
        } else if (chairProp >= 1) {
            chairs = Math.max(0, chairs - (int) chairProp);
        }


        while (chairs > 0){
            generateRandomPos();
            chairs -= 1;
        }

        for (Location loc : chairsLoc){
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();

            String worldName = loc.getWorld().getName();
            String dimension = "minecraft:" + worldName.toLowerCase();
            String cmd = "execute in " + dimension + " run setblock " + x + " " + y + " " + z + " cfm:stripped_oak_chair";
            Bukkit.getLogger().info("Placement d'une chaise dans le monde '" + worldName + "' aux coordonnées : x=" + x + ", y=" + y + ", z=" + z);
            Bukkit.getLogger().info("Commande exécutée : " + cmd);

            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
    }

    public static void removeChair() {
        Iterator<Location> iterator = chairsLoc.iterator();
        while (iterator.hasNext()) {
            Location loc = iterator.next();
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            String worldName = loc.getWorld().getName();
            String dimension = "minecraft:" + worldName.toLowerCase();
            String cmd = "execute in " + dimension + " run setblock " + x + " " + y + " " + z + " air";
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
            iterator.remove(); // Supprime l'élément en toute sécurité
        }
    }

    private static void generateRandomPos() {
        int minX = Math.min(zone[0], zone[2]);
        int maxX = Math.max(zone[0], zone[2]);
        int minZ = Math.min(zone[1], zone[3]);
        int maxZ = Math.max(zone[1], zone[3]);

        if (minX >= maxX || minZ >= maxZ) {
            throw new IllegalArgumentException("Les limites de la zone sont incorrectes : bound doit être supérieur à origin.");
        }

        int x = new Random().nextInt(minX, maxX);
        int y = 62; // Coordonnée Y fixe
        int z = new Random().nextInt(minZ, maxZ);

        Location location = new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), x, y, z);
        if (chairsLoc.contains(location)) {
            generateRandomPos();
        } else {
            chairsLoc.add(location);
        }
    }
}
