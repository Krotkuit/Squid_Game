package fr.salut.squidgame.component.commands.games;

import fr.salut.squidgame.component.ListenerManager.MiniGames.TTC.TTBManager;
import fr.salut.squidgame.utils.chronometer.Chronometer;
import fr.salut.squidgame.utils.chronometer.ChronometerType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Team;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;

import java.util.Random;
import java.util.UUID;

@Command("ttc")
public class TTBCommand {

    public static double maxDead = 1;
    public static Random random = new Random();

    @Subcommand("set")
    public void tTCSet(CommandSender sender, double score){
        if (score <= 0) {
            sender.sendMessage("§cLa valeur doit être positive.");
            return;
        }

        maxDead = score;
    }

    @Subcommand("start")
    public void tTTStart(){

        TTBManager.addTeams();

        for (Team team : TTBManager.getTeams()){

            UUID bomber = team.getPlayers().stream().toList().get(random.nextInt(team.getPlayers().size())).getUniqueId();

            TTBManager.giveBombTo(Bukkit.getPlayer(bomber), null, team);

            Chronometer.startServerChronometer(null, team, team.getName(), random.nextInt(90, 150), ChronometerType.ACTION_BAR, "%null%", ChronometerType.ACTION_BAR, "§cBOUM !");
        }
    }
}
