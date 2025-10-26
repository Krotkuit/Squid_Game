package fr.salut.squidgame.component.ListenerManager.MiniGames.Splatoon;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ClaimedZone {

  private final Set<Block> blocks = new HashSet<>();
  @Setter
  private String currentOwner = null; // équipe actuelle
  @Setter
  private String claimingTeam = null; // équipe en train de claim
  @Setter
  private double progress = 0; // % de progression
  private final Map<String, Set<UUID>> playersInZone = new HashMap<>();
  @Setter
  private BukkitRunnable task;

  public ClaimedZone(Set<Block> blocks) {
    this.blocks.addAll(blocks);
  }

  public Set<Block> getBlocks() { return blocks; }

  public void addPlayer(String team, UUID uuid) {
    playersInZone.putIfAbsent(team, new HashSet<>());
    playersInZone.get(team).add(uuid);
  }

  public boolean isEmpty() {
    return playersInZone.values().stream().allMatch(Set::isEmpty);
  }

  public Map<String, Set<UUID>> getPlayersInZone() { return playersInZone; }

  public String getCurrentOwner() { return currentOwner; }

  public String getClaimingTeam() { return claimingTeam; }

  public double getProgress() { return progress; }

  public void resetProgress() { this.progress = 0; }

  public BukkitRunnable getTask() { return task; }

  public void removePlayer(String team, UUID playerId) {
    Set<UUID> playerSet = playersInZone.get(team);
    if (playerSet != null) {
      playerSet.remove(playerId);
      if (playerSet.isEmpty()) {
        playersInZone.remove(team); // supprime l'équipe si plus de joueur
      }
    }
  }


}
