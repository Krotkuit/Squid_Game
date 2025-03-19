package fr.salut.test2;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

public class RBlock {
    String title;
    Location location;

    RBlock(String title, Location location) {
        this.title = title;
        this.location = location;
        RBlocks.registerRBlock(this);
    }

    // Place un Block RedstoneBlock ou Air en fonction du block actuellement en place.
    public void toggle() {
        World world = this.location.getWorld();
        BlockData blockData = world.getBlockData(this.location);
        blockData = (blockData.getMaterial() == Material.REDSTONE_BLOCK) // variable = (condition)
                ? Material.AIR.createBlockData() // ? if
                : Material.REDSTONE_BLOCK.createBlockData(); // : else
        world.setBlockData(this.location, blockData);
    }
}
