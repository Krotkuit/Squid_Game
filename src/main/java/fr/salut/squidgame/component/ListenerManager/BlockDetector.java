package fr.salut.squidgame.component.ListenerManager;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Objects;


public class BlockDetector implements Listener {
  @Getter
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
      if (x == 6 && y == 65 && z == 6) {
        epreuve = "Lobby";
      } else if (x == 6 && y == 65 && z == 9) {
        epreuve = "123Soleil";
      } else if (x == 6 && y == 65 && z == 12) {
        epreuve = "ArcEnCiel";
      } else if (x == 0 && y == 0 && z == 0) {
        epreuve = "BAP";
      } else if (x == 6 && y == 65 && z == 18) {
        epreuve = "CacheCache";
      } else if (x == 6 && y == 65 && z == 15) {
        epreuve = "Carrousel";
      } else if (x == 6 && y == 65 && z == 21) {
        epreuve = "CordeASauter";
      } else if (x == 0 && y == 0 && z == 0) {
        epreuve = "LTTE";
      } else if (x == 0 && y == 0 && z == 0) {
        epreuve = "PRV";
      } else if (x == 0 && y == 0 && z == 0) {
        epreuve = "Puissance4";
      } else if (x == 6 && y == 65 && z == 24) {
        epreuve = "SquidGameAerien";
      } else if (x == 9 && y == 65 && z == 24) {
        epreuve = "Salle_Grise";
      } else if ((x == 15 || x == 18 || x == 21) && y == 65 && z == 6) {
        epreuve = "Escaliers";
      }
      /*if (x == -24 && y == -38 && z == -22) {
        epreuve = "Lobby";
      } else if (x == -34 && y == -35 && z == -20) {
          epreuve = "123Soleil";
      } else if (x == -34 && y == -35 && z == -18) {
          epreuve = "Biscuit_Team";
      } else if (x == -34 && y == -35 && z == -16) {
          epreuve = "Biscuit_Game";
      } else if ( x == -36 && y == -35 && z == -20) {
          epreuve = "Tire_a_la_Corde";
      } else if ( x == -36 && y == -35 && z == -18) {
          epreuve = "Arc_en_Ciel";
      } else if (x == -36 && y == -35 && z == -16) {
          epreuve = "Brise_Glace";
      } else if (x == -38 && y == -35 && z == -20) {
          epreuve = "Carrousel";
      } else if (x == -38 && y == -35 && z == -18) {
          epreuve = "Billes";
      } else if (x == -38 && y == -35 && z == -16) {
          epreuve = "Discotheque";
      } else if (x == -40 && y == -35 && z == -20) {
          epreuve = "Jack_a_dit";
      } else if (x == -40 && y == -35 && z == -18) {
          epreuve = "Bataille_Navale";
      } else if (x == -40 && y == -35 && z == -16) {
          epreuve = "Croque_Carotte";
      } else if (x == -42 && y == -35 && z == -20) {
          epreuve = "Puissance_4";
      } else if (x == -42 && y == -35 && z == -18) {
          epreuve = "Morpion";
      } else if (x == -42 && y == -35 && z == -16) {
          epreuve = "Squid_Game";
      } else if (x == -44 && y == -35 && z == -20) {
          epreuve = "Roulette_Russe";
      } else if (x == -44 && y == -35 && z == -18) {
        epreuve = "Tic_Tac_Explosif";
      } else if (x == -44 && y == -35 && z == -16) {
        epreuve = "Bras_d_Argent";
      } else if (x == -46 && y == -35 && z == -20) {
        epreuve = "Loup_Touche_Touche_Explosif";
      } else if (x == -46 && y == -35 && z == -18) {
        epreuve = "Balle_aux_Prisonniers";
      } else if (x == -46 && y == -35 && z == -16) {
        epreuve = "Chaises_Musicales";
      } else if (x == -48 && y == -35 && z == -20) {
        epreuve = "Poule_Renard_Vipere";
      } else if (x == -48 && y == -35 && z == -18) {
        epreuve = "Find_the_Button";
      } else if (x == -34 && y == -38 && z == -13) {
        epreuve = "Salle_Blanche";
      } else if (x == -40 && y == -35 && z == -13) {
        epreuve = "Salle_Grise";
      } else if (x == -32 && y == -35 && (z == -20 || z == -18 || z == -16)) {
        epreuve = "Escaliers";
      }*/

      System.out.println("Epreuve actuelle: " + epreuve);
    } catch (NumberFormatException e) {
      Bukkit.broadcastMessage("Erreur : Coordonnées invalides !");
      e.printStackTrace();
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
}

