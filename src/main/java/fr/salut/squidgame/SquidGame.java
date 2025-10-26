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
import fr.salut.squidgame.component.ListenerManager.MiniGames.Splatoon.SplatoonManager;
import fr.salut.squidgame.component.ListenerManager.MiniGames.Splatoon.SplatoonState;
import fr.salut.squidgame.component.ListenerManager.MiniGames.Splatoon.ZoneManager;
import fr.salut.squidgame.component.ListenerManager.MiniGames.TAC.TACState;
import fr.salut.squidgame.component.ListenerManager.MiniGames.TTC.TTBManager;
import fr.salut.squidgame.component.ListenerManager.armor.ArmorProtectionListener;
import fr.salut.squidgame.component.ListenerManager.intance.TeamManager;
import fr.salut.squidgame.component.ListenerManager.intance.WorldManager;
import fr.salut.squidgame.component.commands.*;
import fr.salut.squidgame.component.commands.games.*;
import fr.salut.squidgame.utils.MVC.MVCFix;
import fr.salut.squidgame.menu.BookManager;
import fr.salut.squidgame.menu.BookMenuListener;
import fr.skytasul.glowingentities.GlowingEntities;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class SquidGame extends JavaPlugin{

    @Getter static SquidGame instance;

    @Setter @Getter
    private TACState tacState = TACState.OFF;
    @Setter @Getter
    private PRVState prvState = PRVState.OFF;
    private LTTEState ltteState = LTTEState.OFF;
    @Setter @Getter
    private SplatoonState splatoonState = SplatoonState.OFF;
    @Getter
    private final List<Player> playersWithTNT = new ArrayList<>();

    @Getter private GlowingEntities glowingEntities;

    @Getter
    BookManager booksManager;

    @Getter
    GameZoneManager gameZoneManager;

    @Getter
    ZoneManager splatoonZoneManager;

    @Override
    public void onEnable() {
        instance = this;
        // Manager
        new CommandManager();
        booksManager = new BookManager(this);
        booksManager.loadBooks(false);
        //new PlayerNumberManager();

        TeamManager.Team_Instance();
        //new WorldManager().applyRuleToAllWorlds();
        splatoonZoneManager = new ZoneManager(this);
        glowingEntities = new GlowingEntities(this);
        gameZoneManager = new GameZoneManager(this);

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
            new GunListener(),
            new LifeListener(),
            new TchatCommad(), // contient un listener
            new BookMenuListener(),
            new TTBManager(),
            new SplatoonManager(this),

            // [ extern folder ] //
            new MVCFix(this)
        );

        loadCommands();

        // Load les commandes aussi
        CommandManager.getHandler().register(
                new BaPCommand(),
                new LTTECommand(this),
                new MoveDetectionCmd(),
                new PRVCommand(),
                //new NickNameCommands(),
                new MenuCommand(),
                new ArmorCommand(),
                new CMCommand(),
                new TACCommand(),
                new CarrouselCommand(),
                new BGCommand(),
                new SplatoonCommand(),
                new LifeModeCommand(),
                new TchatCommad(),
                new ConfigsCommand(),
                new TTBCommand()

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
