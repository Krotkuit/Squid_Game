package fr.salut.squidgame;

import fr.salut.squidgame.component.ListenerManager.*;
import fr.salut.squidgame.component.ListenerManager.MiniGames.BaP.BaPManager;
import fr.salut.squidgame.component.ListenerManager.MiniGames.LTTE.LTTEManager;
import fr.salut.squidgame.component.ListenerManager.MiniGames.LTTE.LTTEState;
import fr.salut.squidgame.component.ListenerManager.MiniGames.PRV.PRVListener;
import fr.salut.squidgame.component.ListenerManager.MiniGames.PRV.PRVState;
import fr.salut.squidgame.component.ListenerManager.NumberPlayer.PlayerNumberManager;
import fr.salut.squidgame.component.ListenerManager.armor.ArmorProtectionListener;
import fr.salut.squidgame.component.ListenerManager.intance.TeamManager;
import fr.salut.squidgame.component.commands.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin implements Listener {

    private PRVState prvState = PRVState.OFF;
    private LTTEState ltteState = LTTEState.OFF;
    private final PlayerNumberManager playerNumberManager = new PlayerNumberManager();

    @Override
    public void onEnable() {
        TeamManager.Team_Instance();

        getServer().getPluginManager().registerEvents(new BlockDetector(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new NoMoveTagListener(), this);
        //getServer().getPluginManager().registerEvents(new PlayerRightListener(), this);
        getServer().getPluginManager().registerEvents(new ArmorProtectionListener(), this);
        getServer().getPluginManager().registerEvents(new MoveDetectListener(this), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new PRVListener(this), this);
        getServer().getPluginManager().registerEvents(new LTTEManager(this), this);
        getServer().getPluginManager().registerEvents(new BaPManager(this), this);
        loadCommands();

        // import custom mob
        getLogger().info("Le plugin est activé !");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadCommands() {
        getCommand("movedetection").setExecutor(new MoveDetectionCmd());
        getCommand("test").setExecutor(new TestCmd());
        getCommand("rblocktoggle").setExecutor(new RBlockUseCommand());
        getCommand("prv").setExecutor(new PRVCommand(this));
        getCommand("ltte").setExecutor(new LTTECommand(this));
        getCommand("bap").setExecutor(new BaPCommand(this));
    }

    public void teleportPlayer(Player player, double x, double y, double z) {
        player.teleport(new Location(player.getWorld(), x, y, z));
    }

    public PRVState getPrvState() {
        return prvState;
    }

    public void setPrvState(PRVState state) {
        this.prvState = state;
    }

    public LTTEState getLTTEState() {
        return ltteState;
    }

    public void setLTTEState(LTTEState state) {
        this.ltteState = state;
    }
    public PlayerNumberManager getPlayerNumberManager() {
        return playerNumberManager;
    }

    private final List<Player> playersWithTNT = new ArrayList<>();

    public List<Player> getPlayersWithTNT() {
        return playersWithTNT;
    }
}
