package fr.salut.squidgame;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;

public class RBlocks {
    static World world = Bukkit.getWorlds().get(0);
    private static HashMap<String, RBlock> RBlockList = new HashMap<>();

    static {
        RBlock Lobby = new RBlock("Lobby", new Location(world, -24, -38, -22));
        RBlock Epreuve_123Soleil = new RBlock("Epreuve_123Soleil", new Location(world, -34, -35,  -20));
        RBlock Epreuve_Biscuit_Team = new RBlock("Epreuve_Biscuit_Team", new Location(world, -34, -35,  -18));
        RBlock Epreuve_Biscuit_Game = new RBlock("Epreuve_Biscuit_Game", new Location(world, -34, -35,  -16));
        RBlock Epreuve_Tire_a_la_Corde = new RBlock("Epreuve_Tire_a_la_Corde", new Location(world, 36, -35,  -20));
        RBlock Epreuve_Arc_en_Ciel = new RBlock("Epreuve_Arc_en_Ciel", new Location(world, -36, -35,  -18));
        RBlock Epreuve_Brise_Glace = new RBlock("Epreuve_Brise_Glace", new Location(world, 36, -35,  -16));
        RBlock Epreuve_Carrousel = new RBlock("Epreuve_Carrousel", new Location(world, -38, -35,  -20));
        RBlock Epreuve_Billes = new RBlock("Epreuve_Billes", new Location(world, 38, -35,  -18));
        RBlock Epreuve_Discotheque = new RBlock("Epreuve_Discotheque", new Location(world, 38, -35,  -16));
        RBlock Epreuve_Jack_a_dit = new RBlock("Epreuve_Jack_a_dit", new Location(world, -40, -35,  -20));
        RBlock Epreuve_Bataille_Navale = new RBlock("Epreuve_Bataille_Navale", new Location(world, -40, -35,  -18));
        RBlock Epreuve_Croque_Carotte = new RBlock("Epreuve_Croque_Carotte", new Location(world, -40, -35,  -16));
        RBlock Epreuve_Puissance_4 = new RBlock("Epreuve_Puissance_4", new Location(world, -42, -35,  -20));
        RBlock Epreuve_Morpion = new RBlock("Epreuve_Morpion", new Location(world, -42, -35,  -18));
        RBlock Epreuve_Squid_Game = new RBlock("Epreuve_Squid_Game", new Location(world, -42, -35,  -16));
        RBlock Epreuve_Roulette_Russe = new RBlock("Epreuve_Roulette_Russe", new Location(world, -44, -35,  -20));
        RBlock Epreuve_Tic_Tac_Explosif = new RBlock("Epreuve_Tic_Tac_Explosif", new Location(world, -44, -35,  -18));
    }

    public static void registerRBlock(RBlock rBlock) {
        RBlockList.put(rBlock.title, rBlock);
    }

    public static HashMap<String, RBlock> getRBlockList() {
        return RBlockList;
    }
}
