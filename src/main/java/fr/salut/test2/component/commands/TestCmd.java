package fr.salut.test2.component.commands;

import fr.salut.test2.component.ListenerManager.MoveDetectListener;
import fr.salut.test2.manager.player.PlayersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TestCmd implements CommandExecutor, TabCompleter {

  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

    Player player = (Player) commandSender;

    try {
      boolean enable = Boolean.parseBoolean(strings[0]);
      PlayersManager.getTPlayer(player).setDead(enable);
    }
    catch (IllegalArgumentException e) {
      commandSender.sendMessage("§cMerci d'entrer un booléen (true|false)");
      return false;
    }

    return false;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if (!commandSender.isOp()) return List.of();
    return List.of("true", "false");
  }
}
