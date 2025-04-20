package fr.salut.squidgame.component.ListenerManager.compteur;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

public class MAJ_compteur {

  public MAJ_compteur() {
    // mise à jour du compteur
    World overworld = Bukkit.getWorld("world");
    overworld.getBlockAt(-41,-60,-38).setType(Material.REDSTONE_BLOCK);
  }
}
