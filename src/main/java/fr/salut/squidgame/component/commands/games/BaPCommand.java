package fr.salut.squidgame.component.commands.games;

import fr.salut.squidgame.component.ListenerManager.MiniGames.BaP.BaPManager;
import fr.salut.squidgame.component.ListenerManager.MiniGames.BaP.BaPState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;

@Command("bap")
public class BaPCommand {
    private static BaPState bapState = BaPState.OFF; // État initial

    public static void setBaPState(BaPState newState) {
        if (newState == null) {
            throw new IllegalArgumentException("L'état BaPState ne peut pas être null.");
        }
        bapState = newState;
    }

    @DefaultFor("~")
    public void bapDefault(CommandSender sender){
        sender.sendMessage("§cÉtat invalide. Utilisez ON, OFF ou STOP.");
    }

    @Subcommand("ON")
    public void bapON(CommandSender sender){
        bapState = BaPState.ON;
        sender.sendMessage("§aBaP activé : le jeu est maintenant actif.");
    }

    @Subcommand("OFF")
    public void bapOFF(CommandSender sender){
        bapState = BaPState.OFF;
        BaPManager.getPlayersInPrison().clear();

        // Supprime les snowballs uniquement pour les joueurs des équipes bleu_marine et vert_profond
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Team team = onlinePlayer.getScoreboard().getEntryTeam(onlinePlayer.getName());
            if (team == null) continue;

            String teamName = team.getName().toLowerCase();
            if (teamName.equals("bleu_marine") || teamName.equals("vert_profond")) {
                onlinePlayer.getInventory().remove(Material.SNOWBALL);
                BaPManager.applyGlowingToPlayersWithSnowball();
            }
        }

        sender.sendMessage("§cBaP désactivé : le jeu est maintenant inactif.");
    }

    @Subcommand("STOP")
    public void bapSTOP(CommandSender sender){
        bapState = BaPState.STOP;
        BaPManager manager = new BaPManager();
        manager.notifyGuardsOfPrisoners();
        sender.sendMessage("§eBaP en pause : le jeu est temporairement suspendu.");
    }

    public static BaPState getBaPState() {
        return bapState;
    }
}
