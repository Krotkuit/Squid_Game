package fr.salut.squidgame.component.commands.debug;

import fr.salut.squidgame.utils.chronometer.Chronometer;
import fr.salut.squidgame.utils.chronometer.ChronometerType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;

@Command("debug")
public class ChronomterDebugCommand {

    @Subcommand("chronometer bossbar")
    public void debugChronometerBossBar(CommandSender sender) {
        if (sender instanceof Player player)
            Chronometer.startChronometer(player, "debug", 20, ChronometerType.BOSSBAR, "%time%", ChronometerType.ACTION_BAR, "debug fini");
    }

    @Subcommand("chronometer bossbar removeall")
    public void debugChronometerBossBarRemoveAll(CommandSender sender) {
        if (sender instanceof Player player)
            Chronometer.stopAllChronometer(player, ChronometerType.ACTION_BAR, "%null%");
    }
}
