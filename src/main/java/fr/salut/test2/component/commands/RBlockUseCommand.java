package fr.salut.test2.component.commands;

import fr.salut.test2.RBlock;
import fr.salut.test2.RBlocks;
import fr.salut.test2.manager.player.PlayersManager;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class RBlockUseCommand implements CommandExecutor, TabCompleter {

  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

    if (!(commandSender instanceof Player)) return false;
    Player player = ((Player) commandSender);

    String rBlockTitle = strings[0];
    HashMap<String, RBlock> rBlockList = RBlocks.getRBlockList();
    if (!rBlockList.containsKey(rBlockTitle)) {
      player.sendMessage(Color.RED+"Unknown RBlock title, couldn't execute the command.");
      return false;
    }

    RBlock rBlock = rBlockList.get(rBlockTitle);
    rBlock.toggle();

    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if (!commandSender.isOp()) return List.of();
    return RBlocks.getRBlockList().keySet().stream().toList();
  }
}
