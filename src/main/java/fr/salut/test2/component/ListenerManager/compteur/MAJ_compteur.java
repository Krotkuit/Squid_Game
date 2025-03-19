package fr.salut.test2.component.ListenerManager.compteur;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

public class MAJ_compteur {

  public static void MAJ_compteur() {
    // mise à jour du compteur
    World overworld = Bukkit.getWorld("world");
    overworld.getBlockAt(-41,-60,-38).setType(Material.REDSTONE_BLOCK);
  }
}
