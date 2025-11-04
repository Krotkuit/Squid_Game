package fr.salut.squidgame.component.commands.games;

import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.ListenerManager.MiniGames.TAC.TACManager;
import fr.salut.squidgame.component.ListenerManager.MiniGames.TAC.TACState;
import fr.salut.squidgame.component.ListenerManager.intance.TeamManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Command("tac")
@CommandPermission("spg.admins.commands.tac")
public class TACCommand {

    SquidGame plugin = SquidGame.getInstance();
    private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    @DefaultFor("~")
    public void bapDefault(Player sender){
        sender.sendMessage("§cÉtat invalide. Utilisez ON, OFF, STOP, select.");
    }

    @Subcommand("ON")
    @CommandPermission("spg.admins.commands.tac.ON")
    public void tacON(CommandSender sender){
        plugin.setTacState(TACState.ON);
        sender.sendMessage("§aTAC activé : jeux activé.");
        TACManager.startTAC();
    }

    @Subcommand("OFF")
    @CommandPermission("spg.admins.commands.tac.OFF")
    public void tacOFF(CommandSender sender){
        plugin.setTacState(TACState.OFF);
        sender.sendMessage("§cTAC désactivé : jeux réinitailisé et désactivé.");
        TACManager.team1Click = 0;
        TACManager.team2Click = 0;
    }

    @Subcommand("RESET")
    @CommandPermission("spg.admins.commands.tac.RESET")
    public void tacRESET(CommandSender sender){
        plugin.setTacState(TACState.RESET);
        for (Player player : TeamManager.getTeamOnlinePlayers(TACManager.team1)){
            player.removePotionEffect(PotionEffectType.SLOW);
            player.setGravity(true);
        }
        for (Player player : TeamManager.getTeamOnlinePlayers(TACManager.team2)){
            player.removePotionEffect(PotionEffectType.SLOW);
            player.setGravity(true);
        }
        TACManager.team1Click = 0;
        TACManager.team2Click = 0;
        TACManager.playersTeam1.clear();
        TACManager.playersTeam2.clear();
        sender.sendMessage("§eTAC reset");
    }


    @Subcommand("select")
    @CommandPermission("spg.admins.commands.tac.select")
    public void tacSelectTeam(CommandSender sender,@Named("team") String team1,@Named("team") String team2){
        if (team1==null || team2==null){
            sender.sendMessage("§eLes teams n'existent pas !");
        }

        TACManager.team1 = scoreboard.getTeam(team1);
        TACManager.team2 = scoreboard.getTeam(team2);
    }

    @Subcommand("place")
    @CommandPermission("spg.admins.commands.tac.place")
    public void tacPlaceTeams() {TACManager.placePlayers();}

    @Subcommand("debug setclickt1")
    @CommandPermission("spg.admins.commands.tac.debug.setclickt2")
    public void debugSetClickTeam1(double clicks) {
        TACManager.team1Click = clicks;
    }

    @Subcommand("debug setclickt2")
    @CommandPermission("spg.admins.commands.tac.debug.setclickt2")
    public void debugSetClickTeam2(double clicks) {
        TACManager.team2Click = clicks;
    }
}
