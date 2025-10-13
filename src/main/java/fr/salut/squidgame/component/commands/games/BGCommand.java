package fr.salut.squidgame.component.commands.games;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class BGCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    // Vérifier si le sender est un joueur
    if (sender instanceof Player) {
      Player player = (Player) sender;
      if (!player.isOp()) {
        player.sendMessage("§cVous devez être opérateur pour exécuter cette commande.");
        return true;
      }
    }


    World world = Bukkit.getWorld("worlds/SquidGame/BriseGlace");
    if (world == null) {
      sender.sendMessage("§cLe monde 'worlds/SquidGame/BriseGlace' n'existe pas.");
      return true;
    }


    int startX = 23, y = 95, z = 18;
    int RightSafe = 16, LeftSafe = 17;

    for (int i = 0; i < 20; i++) {
      int x = startX + (i * 4);
      int random = ThreadLocalRandom.current().nextInt(2);
      // Placer le bloc de commande
      Location commandBlockLocation = new Location(world, x, y, z);
      Block commandBlock = world.getBlockAt(commandBlockLocation);
      commandBlock.setType(Material.REPEATING_COMMAND_BLOCK);

      String blockCommand;
      Location redWoolLocation;
      Location greenWoolLocation;

      if (random == 0) {
        blockCommand = "/execute positioned ~-13 ~-30 ~-20 if entity @a[dx=0,dy=0,dz=0] run fill ~ ~-1 ~1 ~1 ~-1 ~ air destroy";
        // Placer les blocs de laine rouge et verte
        redWoolLocation = new Location(world, x, y - 1, RightSafe);
        greenWoolLocation = new Location(world, x, y - 1, LeftSafe);
      }
      else {
        blockCommand = "/execute positioned ~-13 ~-30 ~-17 if entity @a[dx=0,dy=0,dz=0] run fill ~ ~-1 ~1 ~1 ~-1 ~ air destroy";
        // Placer les blocs de laine rouge et verte
        redWoolLocation = new Location(world, x, y - 1, LeftSafe);
        greenWoolLocation = new Location(world, x, y - 1, RightSafe);
      }


      // Construire la commande setblock
      //String setblockCommand = String.format(
      //    "setblock %d %d %d minecraft:repeating_command_block[facing=up]{Command:\"%s\", auto : 1}",
      //    x, y, z, blockCommand.replace("\"", "\\\"") // Échapper les guillemets
      //);

      // Placer le bloc command block répétitif
      commandBlock.setType(Material.REPEATING_COMMAND_BLOCK);

      // Obtenir le BlockState
      CommandBlock cb = (CommandBlock) commandBlock.getState();

      // Définir la commande
      cb.setCommand(blockCommand);

      // Mise à jour
      cb.update(true);

      // Pour l'automatisation, tu peux essayer d'utiliser la commande console pour activer le mode auto
      // Exemple : activer auto par commande console
      String setAutoCmd = String.format("data merge block %d %d %d {auto:1b}", x, y, z);
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), setAutoCmd);


      // Exécuter la commande en tant que console
      //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cb);

      world.getBlockAt(redWoolLocation).setType(Material.RED_WOOL);
      world.getBlockAt(greenWoolLocation).setType(Material.LIME_WOOL);
    }

    sender.sendMessage("§a20 blocs de commande ont été placés avec succès !");
    return true;
  }
}