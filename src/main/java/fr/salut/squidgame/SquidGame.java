package fr.salut.squidgame;

import dev.xernas.menulib.MenuLib;
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
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class SquidGame extends JavaPlugin implements Listener {

    @Getter static SquidGame instance;

    @Setter
    @Getter
    private PRVState prvState = PRVState.OFF;
    private LTTEState ltteState = LTTEState.OFF;
    @Getter
    private final List<Player> playersWithTNT = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        MenuLib.init(this);
        // Manager
        new CommandManager();
        new PlayerNumberManager();

        TeamManager.Team_Instance();

        // Listener
        registerEvents(
                new BlockDetector(),
                new JoinListener(),
                new NoMoveTagListener(),
                //new PlayerRightListener(),
                new ArmorProtectionListener(),
                new MoveDetectListener(this),
                new DeathListener(),
                new PRVListener(),
                new LTTEManager(),
                new BaPManager()
        );

        loadCommands();

        // Load les commandes aussi
        CommandManager.getHandler().register(
                new BaPCommand(),
                new LTTECommand(this),
                new MoveDetectionCmd(),
                new PRVCommand(),
                new NickNameCommands(),
                new MenuCommand(),
                new ArmorCommand()
        );

        // import custom mob
        getLogger().info("Le plugin est activé !");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Le plugin est désactivé !");
    }

    private void loadCommands() {
        getCommand("test").setExecutor(new TestCmd());
        getCommand("rblocktoggle").setExecutor(new RBlockUseCommand());
    }

    public void teleportPlayer(Player player, double x, double y, double z) {
        player.teleport(new Location(player.getWorld(), x, y, z));
    }

    public LTTEState getLTTEState() {
        return ltteState;
    }

    public void setLTTEState(LTTEState state) {
        this.ltteState = state;
    }

    public static void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            instance.getServer().getPluginManager().registerEvents(listener, instance);
        }
    }
}
