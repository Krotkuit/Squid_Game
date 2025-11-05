package fr.salut.squidgame.component.ListenerManager.compteur;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

public class MAJ_compteur {

  public MAJ_compteur() {
    // mise à jour du compteur
    World overworld = Bukkit.getWorld("worlds/SquidGame/Dortoir");
    if (overworld == null) return;
    overworld.getBlockAt(37, 82, -43).setType(Material.REDSTONE_BLOCK);
  }
}
