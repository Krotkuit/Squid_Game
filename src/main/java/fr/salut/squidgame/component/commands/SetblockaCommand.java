package fr.salut.squidgame.component.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;

public class SetblockaCommand {

    @Command("setblocka")
    public void onSetBlock(Player sender, double x, double y, double z, String blockName, String worldName) {

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            if (sender != null){
                sender.sendMessage("§cLe monde " + worldName + " n'a pas été trouvé !");
            }
            return;
        }

        Material material = Material.getMaterial(blockName.toUpperCase());
        if (material == null) {
            if (sender != null){
                sender.sendMessage("§cBloc inconnu : " + blockName);
            }
            return;
        }

        Location loc = new Location(world, x, y, z);
        Block block = world.getBlockAt(loc);
        block.setType(material);
    }
}
