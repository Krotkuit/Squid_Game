package fr.salut.squidgame.component.commands;

import fr.salut.squidgame.component.ListenerManager.LifeMode.LifeListener;
import org.bukkit.entity.Entity;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.UUID;

@Command("lifemode")
@CommandPermission("spg.admin.lifemode")
public class LifeModeCommand {

  private final LifeListener lifeListener;

  public LifeModeCommand() {
    this.lifeListener = LifeListener.getInstance();
  }

  // --------- /lifemode mode on/off ---------
  public enum Mode {
    ON, OFF
  }

  @Subcommand("mode")
  public void setMode(CommandSender sender, Mode mode) {
    if (mode == null) {
      sender.sendMessage(ChatColor.RED + "Usage: /lifemode mode <ON|OFF>");
      return;
    }
    boolean enable = mode == Mode.ON;
    lifeListener.setLifeModeEnabled(enable);
    sender.sendMessage(ChatColor.GREEN + "Life mode " + (enable ? "enabled." : "disabled."));
  }

  // --------- /lifemode lifenumber <number> ---------
  @Subcommand("lifenumber")
  public void setLifeNumber(CommandSender sender, Integer number) {
    if (number == null) {
      sender.sendMessage(ChatColor.RED + "Usage: /lifemode lifenumber <number>");
      return;
    }
    lifeListener.setDefaultLives(number);
    sender.sendMessage(ChatColor.GREEN + "Default lives set to " + number + ".");
  }

  // --------- /lifemode player revive ---------
  @Subcommand("player revive")
  public void revivePlayers(CommandSender sender) {
    if (!lifeListener.isLifeModeEnabled()) {
      sender.sendMessage(ChatColor.RED + "Le mode de vie est désactivé. Impossible d'exécuter la commande /lifemode player revive.");
      return;
    }

    for (Player player : Bukkit.getOnlinePlayers()) {
      Team team = player.getScoreboard().getEntryTeam(player.getName());
      if (team != null && team.getName().equalsIgnoreCase("garde")) continue;

      Integer lives = lifeListener.getPlayerLives().get(player.getUniqueId());
      if (lives != null && lives > 0) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join joueur " + player.getName());
        sender.sendMessage(ChatColor.GREEN + "Player " + player.getName() + " revived.");
      }
    }
  }

  // --------- /lifemode player reset <number> ---------
  @Subcommand("player reset")
  public void resetPlayers(CommandSender sender, Integer number) {
    if (number == null) {
      sender.sendMessage(ChatColor.RED + "Usage: /lifemode player reset <number>");
      return;
    }
    if (!lifeListener.isLifeModeEnabled()) {
      sender.sendMessage(ChatColor.RED + "Le mode de vie est désactivé. Impossible d'exécuter la commande /lifemode player reset <number>.");
      return;
    }

    for (Player player : Bukkit.getOnlinePlayers()) {
      UUID uuid = player.getUniqueId();
      lifeListener.getPlayerLives().put(uuid, number);
      lifeListener.updatePlayerXP(player, number); // Met à jour l'XP
    }

    sender.sendMessage(ChatColor.GREEN + "All players' lives have been reset to " + number + ".");
  }

  @Subcommand("lifeadd")
  public void addLifePlayers(CommandSender sender, @Named("targets") String selector, Integer number) {
    if (number == null) {
      sender.sendMessage(ChatColor.RED + "Usage: /lifemode lifeadd <number>");
      return;
    }
    if (!lifeListener.isLifeModeEnabled()) {
      sender.sendMessage(ChatColor.RED + "Le mode de vie est désactivé. Impossible d'exécuter la commande /lifemode lifeadd <number>.");
      return;
    }

    List<Entity> entities = Bukkit.selectEntities(sender, selector);
    for (Entity entity : entities) {
      Player player = (Player) entity;
      UUID uuid = player.getUniqueId();
      Integer currentLives = lifeListener.getPlayerLives().getOrDefault(uuid, 0);
      int newLives = currentLives + number;
      lifeListener.getPlayerLives().put(uuid, newLives);
      lifeListener.updatePlayerXP(player, newLives); // Met à jour l'XP
      sender.sendMessage(ChatColor.GREEN + "Added " + number + " lives to " + player.getName() + ". New total: " + newLives + ".");
    }
  }

  @Subcommand("getlifenumber")
  public void getLifeNumber(CommandSender sender, @Named("targets") String selector) {
    if (!lifeListener.isLifeModeEnabled()) {
      sender.sendMessage(ChatColor.RED + "Le mode de vie est désactivé. Impossible d'exécuter la commande /lifemode getlifenumber <player>.");
      return;
    }

    List<Entity> entities = Bukkit.selectEntities(sender, selector);
    if (entities.isEmpty()) {
      sender.sendMessage(ChatColor.RED + "No players found for the given selector.");
      return;
    }
    // Récupère les joueurs de la liste 'entities' qui sont bien des Player
    List<Player> sortedPlayers = entities.stream()
        .filter(entity -> entity instanceof Player)
        .map(entity -> (Player) entity)
        .filter(player -> {
          Team team = player.getScoreboard().getEntryTeam(player.getName());
          // Exclure les joueurs dans l'équipe "garde"
          return team == null || !team.getName().equalsIgnoreCase("garde");
        })
        .sorted((p1, p2) -> {
          // Récupère les vies, avec valeur par défaut pour éviter le null
          Integer lives1 = lifeListener.getPlayerLives().getOrDefault(p1.getUniqueId(), 0);
          Integer lives2 = lifeListener.getPlayerLives().getOrDefault(p2.getUniqueId(), 0);

          // Tri par nombre de vies (ordre décroissant)
          int livesCompare = Integer.compare(lives2, lives1);
          if (livesCompare != 0) return livesCompare;

          // Si égalité, tri alphabétique croissant du pseudo
          return p1.getName().compareToIgnoreCase(p2.getName());
        })
        .toList();

// Envoie les messages triés
    for (Player player : sortedPlayers) {
      Integer lives = lifeListener.getPlayerLives().get(player.getUniqueId());
      if (lives == null) {
        sender.sendMessage(ChatColor.RED + "Player " + player.getName() + " has no recorded lives.");
      } else if (lives == LifeListener.defaultLives) {
        sender.sendMessage(ChatColor.GREEN + "Player " + player.getName() + " has " + lives + " lives.");
      } else if (lives > 0) {
        sender.sendMessage(ChatColor.YELLOW + "Player " + player.getName() + " has " + lives + " lives.");
      } else {
        sender.sendMessage(ChatColor.RED + "Player " + player.getName() + " has " + lives + " lives left.");
      }
    }

  }

}
