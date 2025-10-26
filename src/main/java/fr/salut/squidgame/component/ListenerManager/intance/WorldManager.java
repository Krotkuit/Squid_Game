package fr.salut.squidgame.component.ListenerManager.intance;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;

public class WorldManager {

  private static final String SQUID_GAME_WORLDS_FOLDER = "worlds/SquidGame";

  public void applyRuleToAllWorlds() {
    File folder = new File(SQUID_GAME_WORLDS_FOLDER);

    if (!folder.exists() || !folder.isDirectory()) {
      Bukkit.getLogger().warning("[SquidGame] Le dossier " + SQUID_GAME_WORLDS_FOLDER + " n'existe pas ou n'est pas un dossier.");
      return;
    }

    File[] worldDirs = folder.listFiles(File::isDirectory);
    if (worldDirs == null) {
      Bukkit.getLogger().warning("[SquidGame] Aucun monde trouvé dans le dossier " + SQUID_GAME_WORLDS_FOLDER + ".");
      return;
    }

    for (File worldDir : worldDirs) {
      String worldName = worldDir.toString();

      // Charger ou récupérer le monde
      World world = Bukkit.getWorld(worldName);
      if (world == null) {
        Bukkit.getLogger().info("[SquidGame] Impossible de charger le monde : " + worldName);
      } else {
        //world.setDifficulty(Difficulty.PEACEFUL);
        Bukkit.getLogger().info("world is charged ? : " + (Bukkit.getWorld(worldName.toLowerCase()) != null));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"execute in minecraft:" + worldName.toLowerCase() + " run difficulty peaceful");
        Bukkit.getLogger().info("MONDE : " + world);
        Bukkit.getLogger().info("NOM DU MONDE : " + world.getName());
        Bukkit.getLogger().info("DIFFICULTY DU MONDE : " + world.getDifficulty());
        Bukkit.getLogger().info("monde : " + worldName.toLowerCase());
        Bukkit.getLogger().info("[SquidGame] Règle appliquée au monde : " + worldName);
      }
    }
  }
}
