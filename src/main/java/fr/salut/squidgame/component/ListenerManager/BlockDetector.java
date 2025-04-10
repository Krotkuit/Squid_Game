package fr.salut.squidgame.component.ListenerManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;


public class BlockDetector implements Listener {
  private static String epreuve = "Lobby";
  @EventHandler
  public void onCommand(PlayerCommandPreprocessEvent event) {
    String command = event.getMessage(); // Ex: "/setblock 100 64 -200 minecraft:stone"
    if (!command.startsWith("/setblock")) return;
    String[] args = command.split(" ");
    if (args.length < 5) return;
    try {
      int x = Integer.parseInt(args[1]);
      int y = Integer.parseInt(args[2]);
      int z = Integer.parseInt(args[3]);
      //Block targetBlock = Bukkit.getWorld("world").getBlockAt(x, y, z);
      //if (targetBlock.getType() == Material.AIR) return;

      String blockType = String.valueOf(args[4]);
      // case when blocktype contains value
      if (!blockType.toUpperCase().endsWith("REDSTONE_BLOCK")) return;

      blockType = "REDSTONE_BLOCK";
      Material material = Material.getMaterial(blockType);

      if (material != Material.REDSTONE_BLOCK) return;
      if (x == -24 &&
        y == -38 &&
        z == -22) {
        epreuve = "Lobby";
      }
      else if (x == -34 &&
        y == -35 &&
        z == -20) {
          epreuve = "123Soleil";
      } else if (
        x == -34 &&
        y == -35 &&
        z == -18) {
          epreuve = "Biscuit_Team";
      } else if (
        x == -34 &&
        y == -35 &&
        z == -16) {
          epreuve = "Biscuit_Game";
      } else if (
        x == -36 &&
        y == -35 &&
        z == -20) {
          epreuve = "Tire_a_la_Corde";
      } else if (
        x == -36 &&
        y == -35 &&
        z == -18) {
          epreuve = "Arc_en_Ciel";
      } else if (
        x == -36 &&
        y == -35 &&
        z == -16) {
          epreuve = "Brise_Glace";
      } else if (
        x == -38 &&
        y == -35 &&
        z == -20) {
          epreuve = "Carrousel";
      } else if (
        x == -38 &&
        y == -35 &&
        z == -18) {
          epreuve = "Billes";
      } else if (
        x == -38 &&
        y == -35 &&
        z == -16) {
          epreuve = "Discotheque";
      } else if (
        x == -40 &&
        y == -35 &&
        z == -20) {
          epreuve = "Jack_a_dit";
      } else if (
        x == -40 &&
        y == -35 &&
        z == -18) {
          epreuve = "Bataille_Navale";
      } else if (
        x == -40 &&
        y == -35 &&
        z == -16) {
          epreuve = "Croque_Carotte";
      } else if (
        x == -42 &&
        y == -35 &&
        z == -20) {
          epreuve = "Puissance_4";
      } else if (
        x == -42 &&
        y == -35 &&
        z == -18) {
          epreuve = "Morpion";
      } else if (
        x == -42 &&
        y == -35 &&
        z == -16) {
          epreuve = "Squid_Game";
      } else if (
        x == -44 &&
        y == -35 &&
        z == -20) {
          epreuve = "Roulette_Russe";
      } else if (
        x == -44 &&
        y == -35 &&
        z == -18) {
        epreuve = "Tic_Tac_Explosif";
        Bukkit.broadcastMessage(epreuve);
      }
      System.out.println("Epreuve actuelle: " + epreuve);
    } catch (NumberFormatException e) {
      Bukkit.broadcastMessage("Erreur : Coordonnées invalides !");
    }
  }
  //@EventHandler
  public void onBlockRedstone(BlockPlaceEvent event) {
    Block block = event.getBlock();
    // Vérifie si c'est un bloc de redstone aux coordonnées -34, -35, -19
    if (block.getType() != Material.REDSTONE_BLOCK) return;
    if (
      block.getX() == -32 &&
      block.getY() == -35 &&
      block.getZ() == -18) {
      epreuve = "123Soleil";
      Bukkit.broadcastMessage(epreuve);
    }
    else if (
      block.getX() == -34 &&
        block.getY() == -35 &&
        block.getZ() == -19) {
      epreuve = "Biscuit_Team";
      Bukkit.broadcastMessage(epreuve);
    }
    else if (
      block.getX() == -34 &&
        block.getY() == -35 &&
        block.getZ() == -17) {
      epreuve = "Biscuit_Game";
      Bukkit.broadcastMessage(epreuve);
    }
  }
  public static String getEpreuve() {
    return epreuve;
  }
}

