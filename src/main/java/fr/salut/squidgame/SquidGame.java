package fr.salut.squidgame;

import fr.salut.squidgame.component.ListenerManager.*;
import fr.salut.squidgame.component.ListenerManager.GameZone.GameZoneManager;
import fr.salut.squidgame.component.ListenerManager.LifeMode.LifeListener;
import fr.salut.squidgame.component.ListenerManager.MiniGames.BaP.BaPManager;
import fr.salut.squidgame.component.ListenerManager.MiniGames.LTTE.LTTEManager;
import fr.salut.squidgame.component.ListenerManager.MiniGames.LTTE.LTTEState;
import fr.salut.squidgame.component.ListenerManager.MiniGames.PRV.PRVListener;
import fr.salut.squidgame.component.ListenerManager.MiniGames.PRV.PRVState;
import fr.salut.squidgame.component.ListenerManager.MiniGames.CarrouselZoneCounter;
import fr.salut.squidgame.component.ListenerManager.MiniGames.TAC.TACState;
import fr.salut.squidgame.component.ListenerManager.armor.ArmorProtectionListener;
import fr.salut.squidgame.component.ListenerManager.intance.TeamManager;
import fr.salut.squidgame.component.commands.*;
import fr.salut.squidgame.component.commands.games.*;
import fr.salut.squidgame.extern.MVC.MVCFix;
import fr.salut.squidgame.menu.BookManager;
import fr.salut.squidgame.menu.BookMenuListener;
import fr.skytasul.glowingentities.GlowingEntities;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class SquidGame extends JavaPlugin{

    @Getter static SquidGame instance;

    @Setter @Getter
    private TACState tacState = TACState.OFF;
    @Setter @Getter
    private PRVState prvState = PRVState.OFF;
    private LTTEState ltteState = LTTEState.OFF;
    @Getter
    private final List<Player> playersWithTNT = new ArrayList<>();

    @Getter private GlowingEntities glowingEntities;

    @Getter
    BookManager booksManager;

    @Getter
    GameZoneManager gameZoneManager;

    @Override
    public void onEnable() {
        instance = this;
        // Manager
        new CommandManager();
        booksManager = new BookManager(this);
        booksManager.loadBooks(false);
        //new PlayerNumberManager();

        TeamManager.Team_Instance();

        glowingEntities = new GlowingEntities(this);

        // Listener
        registerEvents(
            new JoinListener(),
            new NoMoveTagListener(),
            //new PlayerRightListener(),
            new ArmorProtectionListener(),
            new MoveDetectListener(this),
            new DeathListener(),
            new PRVListener(),
            new LTTEManager(),
            new BaPManager(),
            //new TAC(),
            new CarrouselZoneCounter(this),
            gameZoneManager = new GameZoneManager(this),
            new GunListener(),
            new LifeListener(),
            new TchatCommad(), // contient un listener
            new BookMenuListener(),

            // [ extern folder ] //
            new MVCFix(this)
        );

        loadCommands();

        // Load les commandes aussi
        CommandManager.getHandler().register(
                new BaPCommand(),
                new LTTECommand(this),
                new LTTECommandExecutor(),
                new MoveDetectionCmd(),
                new PRVCommand(),
                //new NickNameCommands(),
                new MenuCommand(),
                new ArmorCommand(),
                new CMCommand(),
                new TACCommand(),
                new CarrouselCommand(),
                new BGCommand(),
                new LifeModeCommand(),
                new TchatCommad(),
                new ConfigsCommand()
        );

        getLogger().info("Le plugin est activé !");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        glowingEntities.disable();

        getLogger().info("Le plugin est désactivé !");
    }

    private void loadCommands() {
        getCommand("lifemode").setExecutor(new LifeModeCommand());
        getCommand("lifemode").setTabCompleter(new LifeModeCommand());
        getCommand("carrousel").setExecutor(new CarrouselCommand());
        getCommand("bg").setExecutor(new BGCommand());
        getCommand("setepreuve").setExecutor(new EpreuveCommand());
        getCommand("setepreuve").setTabCompleter(new EpreuveCommand());
        getCommand("getepreuve").setExecutor(new EpreuveCommand());
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
