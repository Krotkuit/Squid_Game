package fr.salut.squidgame.menu;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Getter
public enum Books {
    LOBBY(new Location(Bukkit.getWorld("world"),-18, -31, 10)),
    SOLEIL_GAME(new Location(Bukkit.getWorld("world"),-17, -31, 9)),
    CARROUSEL(new Location(Bukkit.getWorld("world"),-15, -31, 9)),
    BaP(new Location(Bukkit.getWorld("world"),-11, -31, 9)),
    BRAS_DARGENT(new Location(Bukkit.getWorld("world"),-11, -31, 11)),
    LTTE(new Location(Bukkit.getWorld("world"),-9, -31, 9)),
    PRV(new Location(Bukkit.getWorld("world"),-10, -31, 10)),
    CM(new Location(Bukkit.getWorld("world"),-9, -31, 11)),
    SQUID_GAME(new Location(Bukkit.getWorld("world"),-13, -31, 9)),
    CROQUE_CAROTTE(new Location(Bukkit.getWorld("world"),-12, -31, 10)),
    ARC_EN_CIEL(new Location(Bukkit.getWorld("world"),-14, -31, 10)),
    PUISSANCE_4(new Location(Bukkit.getWorld("world"),-15, -31, 11)),
    BILLES(new Location(Bukkit.getWorld("world"),-13, -31, 11)),
    FIND_THE_BUTTON(new Location(Bukkit.getWorld("world"),-8, -31, 10)),
    CORDE_A_SAUTER(new Location(Bukkit.getWorld("world"),-7, -31, 9)),
    SQUID_GAME_AERIEN(new Location(Bukkit.getWorld("world"),-7, -31, 11)),
    ;

    private final Location loc;

    Books(Location loc){
        this.loc = loc;
    }

}
