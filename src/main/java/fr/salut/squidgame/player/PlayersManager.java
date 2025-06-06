package fr.salut.squidgame.player;

import fr.salut.squidgame.component.player.TPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class PlayersManager {

  @Getter
  public static Set<TPlayer> tPlayers = new HashSet<>();

    public static void addTPlayer(TPlayer tPlayer) {
    tPlayers.add(tPlayer);
  }

  public static TPlayer getTPlayer(Player player) {
    return tPlayers.stream().filter(tPlayer -> tPlayer.getPlayer().equals(player)).findFirst().orElse(null);
  }

  public static TPlayer getPlayer(String name) {
    return tPlayers.stream().filter(tPlayer -> tPlayer.getPlayer().getName().equals(name)).findFirst().orElse(null);
  }

  public static boolean contains(Player player) {
    return tPlayers.stream().anyMatch(tPlayer -> tPlayer.getPlayer().equals(player));
  }

  public static boolean contains(String name) {
    return tPlayers.stream().anyMatch(tPlayer -> tPlayer.getPlayer().getName().equals(name));
  }

}
