package fr.salut.test2;

import fr.salut.test2.component.ListenerManager.*;
import fr.salut.test2.component.ListenerManager.armor.ArmorProtectionListener;
import fr.salut.test2.component.ListenerManager.intance.TeamManager;
import fr.salut.test2.component.commands.MoveDetectionCmd;
import fr.salut.test2.component.commands.RBlockUseCommand;
import fr.salut.test2.component.commands.TestCmd;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {
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
    }

}
