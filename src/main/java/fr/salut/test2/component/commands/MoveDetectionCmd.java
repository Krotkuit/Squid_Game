package fr.salut.test2.component.commands;

import fr.salut.test2.component.ListenerManager.MoveDetectListener;
import fr.salut.test2.manager.player.PlayersManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MoveDetectionCmd implements CommandExecutor, TabCompleter {

  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    //if (!commandSender.isOp()) {
      //commandSender.sendMessage("§cVous devez être op pour utiliser cette commande");
      //return false;
    // }

    if (strings.length != 1) {
      commandSender.sendMessage("§cMerci d'utiliser la syntaxe: /movedetection <true|false>");
      return false;
    }

    try {
      boolean enable = Boolean.parseBoolean(strings[0]);
      commandSender.sendMessage("§aDétection de mouvements activée: " + (enable ? "Oui" : "Non"));
      MoveDetectListener.setEnabled(enable);
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
