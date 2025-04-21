package fr.salut.squidgame.menu;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Getter
public enum Books {

    SOLEIL_GAME(new Location(Bukkit.getWorld("world"),-18, -41, 8)),
    LOBBY(new Location(Bukkit.getWorld("world"),-19, -41, 9)),
    CARROUSEL(new Location(Bukkit.getWorld("world"),-16, -41, 8)),
    SQUID_GAME(new Location(Bukkit.getWorld("world"),-14, -41, 8)),
    BRAS_DARGENT(new Location(Bukkit.getWorld("world"),-12, -41, 10)),
    ;

    private final Location loc;

    Books(Location loc){
        this.loc = loc;
    }

}
