package fr.salut.squidgame.component.commands.games;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.concurrent.ThreadLocalRandom;

@Command("bg")
@CommandPermission("sqg.admins.commands.bg")
public class BGCommand {

  @Subcommand("random")
  public void placeRandomCommandBlocks(CommandSender sender) {
    World world = Bukkit.getWorld("worlds/SquidGame/BriseGlace");
    if (world == null) {
      sender.sendMessage("§cLe monde 'worlds/SquidGame/BriseGlace' n'existe pas.");
      return;
    }

    int startX = 23, y = 95, z = 18;
    int RightSafe = 16, LeftSafe = 17;

    for (int i = 0; i < 20; i++) {
      int x = startX + (i * 4);
      int random = ThreadLocalRandom.current().nextInt(2);

      Location commandBlockLocation = new Location(world, x, y, z);
      Block commandBlock = world.getBlockAt(commandBlockLocation);
      commandBlock.setType(Material.REPEATING_COMMAND_BLOCK);

      String blockCommand;
      Location redWoolLocation;
      Location greenWoolLocation;

      if (random == 0) {
        blockCommand = "/execute positioned ~-13 ~-30 ~-20 if entity @a[team=joueur,dx=0,dy=0,dz=0] run fill ~ ~-1 ~1 ~1 ~-1 ~ air destroy";
        redWoolLocation = new Location(world, x, y - 1, RightSafe);
        greenWoolLocation = new Location(world, x, y - 1, LeftSafe);
      } else {
        blockCommand = "/execute positioned ~-13 ~-30 ~-17 if entity @a[team=joueur,dx=0,dy=0,dz=0] run fill ~ ~-1 ~1 ~1 ~-1 ~ air destroy";
        redWoolLocation = new Location(world, x, y - 1, LeftSafe);
        greenWoolLocation = new Location(world, x, y - 1, RightSafe);
      }

      // Configurer le command block
      CommandBlock cb = (CommandBlock) commandBlock.getState();
      cb.setCommand(blockCommand);
      cb.update(true);

      // Activer le mode auto
      String setAutoCmd = String.format("data merge block %d %d %d {auto:1b}", x, y, z);
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), setAutoCmd);

      // Placer les blocs de laine
      world.getBlockAt(redWoolLocation).setType(Material.RED_WOOL);
      world.getBlockAt(greenWoolLocation).setType(Material.LIME_WOOL);
    }

    sender.sendMessage("§a20 blocs de commande ont été placés avec succès !");
  }
}