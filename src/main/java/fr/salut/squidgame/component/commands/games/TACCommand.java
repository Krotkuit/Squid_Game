package fr.salut.squidgame.component.commands.games;

import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.ListenerManager.MiniGames.TAC.TAC;
import fr.salut.squidgame.component.ListenerManager.MiniGames.TAC.TACState;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Subcommand;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Command("tac")
public class TACCommand {

    SquidGame plugin = SquidGame.getInstance();

    @Getter
    static final List<UUID> teamCorde1 = new ArrayList<>();
    @Getter
    static final List<UUID> teamCorde2 = new ArrayList<>();

    @DefaultFor("~")
    public void bapDefault(Player sender){
        sender.sendMessage("§cÉtat invalide. Utilisez ON, OFF, STOP, select.");
    }

    @Subcommand("ON")
    public void tacON(CommandSender sender){
        plugin.setTacState(TACState.ON);
        sender.sendMessage("§aTAC activé : jeux activé.");
        TAC.startDetection();
        TAC.CPS();
    }

    @Subcommand("OFF")
    public void tacOFF(CommandSender sender){
        plugin.setTacState(TACState.OFF);
        sender.sendMessage("§cTAC désactivé : jeux réinitailisé et désactivé.");
        TAC.setClickTeam1(0);
        TAC.setClickTeam2(0);
        TAC.getIgnoredClicker().clear();
        teamCorde1.clear();
        teamCorde2.clear();
    }

    @Subcommand("STOP")
    public void tacSTOP(CommandSender sender){
        plugin.setTacState(TACState.STOP);
        sender.sendMessage("§eTAC en mode STOP : jeux mis en pause.");
    }
    @Subcommand("RESET")
    public void tacRESET(CommandSender sender){
        plugin.setTacState(TACState.RESET);
        TAC.setClickTeam1(0);
        TAC.setClickTeam2(0);
        TAC.getIgnoredClicker().clear();
        sender.sendMessage("§eTAC reset");
    }


    @Subcommand("select")
    public void tacSelectTeam(CommandSender sender, @Named("team1") String team1, @Named("team2") String team2){
        Team t1 = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(team1);
        Team t2 = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(team2);

        if (t1==null || t2==null){
            sender.sendMessage("§eLes teams n'existent pas !");
        }

        for (Player player : Bukkit.getOnlinePlayers()){
            if (t1.hasEntry(player.getName())){
                player.addScoreboardTag("Corde1");
                teamCorde1.add(player.getUniqueId());
                continue;
            }
            if (t2.hasEntry(player.getName())){
                player.addScoreboardTag("Corde2");
                teamCorde2.add(player.getUniqueId());
            }
        }
    }
}
