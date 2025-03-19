package fr.salut.test2;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;

public class RBlocks {
    static World world = Bukkit.getWorlds().get(0);
    private static HashMap<String, RBlock> RBlockList = new HashMap<>();

    static {
        RBlock Lobby = new RBlock("Lobby", new Location(world, -24, -38, -22));
    }

    public static void registerRBlock(RBlock rBlock) {
        RBlockList.put(rBlock.title, rBlock);
    }

    public static HashMap<String, RBlock> getRBlockList() {
        return RBlockList;
    }
}
