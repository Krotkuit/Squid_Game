package fr.salut.squidgame.component.commands.games;

import fr.salut.squidgame.component.ListenerManager.MiniGames.TTB.TTBManager;
import fr.salut.squidgame.component.ListenerManager.intance.TeamManager;
import fr.salut.squidgame.utils.chronometer.Chronometer;
import fr.salut.squidgame.utils.chronometer.ChronometerType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;

import java.util.Random;
import java.util.UUID;

@Command("ttb")
public class TTBCommand {

    public static double maxDead = 1;
    public static Random random = new Random();

    @Subcommand("set")
    public void tTCBet(CommandSender sender, double score){
        if (score <= 0) {
            sender.sendMessage("§cLa valeur doit être positive.");
            return;
        }

        maxDead = score;
    }

    @Subcommand("start")
    public void tTBStart(){

        TTBManager.addTeams();

        for (Team team : TTBManager.getTeams()){

            UUID bomber = TeamManager.getTeamOnlinePlayers(team).get(random.nextInt(TeamManager.getTeamOnlinePlayers(team).size())).getUniqueId();

            TTBManager.giveBombTo(Bukkit.getPlayer(bomber), null, team);
            for (Player player : TeamManager.getTeamOnlinePlayers(team)) player.sendTitle("§6Début du jeu", "§c" + Bukkit.getPlayer(bomber).getName() + " à la bombe !");
            Chronometer.startServerChronometer(null, team, team.getName(), random.nextInt(90, 150), ChronometerType.ACTION_BAR, "%null%", ChronometerType.ACTION_BAR, "§cBOUM !");
        }
    }

    @Subcommand("stop")
    public void tTBStop() {
        TTBManager.stopGame();
        Chronometer.stopAllServerChronometer(null, ChronometerType.ACTION_BAR, "%null%");
    }
}
