package fr.salut.squidgame.component.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Optional;

import java.util.List;

@Command("tpa")
public class TpaCommand {

    @Command("tpa")
    public void onTpa(Player sender,
                      @Optional String selector,
                      String worldName,
                      double x, double y, double z,
                      @Optional float yaw, @Optional float pitch) {

        World world = Bukkit.getWorld(worldName);

        if (world == null){
            sender.sendMessage("§cMonde " + worldName + " inconnu !");
            return;
        }

        Location targetLoc = new Location(
               world , x, y, z, yaw, pitch
        );

        if (selector != null) {
            List<Entity> entities = Bukkit.selectEntities(sender, selector);
            for (Entity entity : entities) {
                entity.teleport(targetLoc);
            }
        } else {
            sender.teleport(targetLoc);
        }
    }
}
