package fr.salut.squidgame.component.ListenerManager.MiniGames.LTTE;

import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.commands.games.LTTECommandExecutor;
import fr.salut.squidgame.component.commands.games.LTTECommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import fr.skytasul.glowingentities.GlowingEntities;

import java.util.*;


public class LTTEManager implements Listener {
  private static final SquidGame plugin = SquidGame.getInstance();
  private static final List<UUID> playersWithTNT = new ArrayList<>();
  private static final Random random = new Random();

  public static void startGame() {
    LTTEState gameState = plugin.getLTTEState();

    if (gameState != LTTEState.ON) return; // Vérifie si le jeu est actif

    if (playersWithTNT.isEmpty()) {
      // Filtre les joueurs pour exclure ceux des équipes "mort" et "garde"
      List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
      onlinePlayers.removeIf(player -> {
        Team team = player.getScoreboard().getEntryTeam(player.getName());
        return team == null || !team.getName().equalsIgnoreCase("joueur");
      });

      if (Bukkit.getOnlinePlayers().isEmpty()) {
        plugin.getLogger().warning("Aucun joueur en ligne pour démarrer LTTE !");
        return;
      }

      int tntCount = (LTTECommandExecutor.getBombProbability() > 1)
          ? (int) LTTECommandExecutor.getBombProbability()
          : Math.max((int) Math.floor(onlinePlayers.size() * (1 - LTTECommandExecutor.getBombProbability())), 1);

      for (int i = 0; i < tntCount && !onlinePlayers.isEmpty(); i++) {
        Player selectedPlayer = onlinePlayers.remove(random.nextInt(onlinePlayers.size()));
        playersWithTNT.add(selectedPlayer.getUniqueId());
        selectedPlayer.sendTitle(ChatColor.RED + "Vous avez une TNT !", "Touchez un autre joueur pour la lui donner !", 10, 70, 20);
        selectedPlayer.sendMessage(ChatColor.RED + "Vous avez une TNT ! Touchez un autre joueur pour la lui donner !");
      }

      for (Player onlinePlayer : onlinePlayers) {
        if (!playersWithTNT.contains(onlinePlayer.getUniqueId())) {
          onlinePlayer.sendMessage(ChatColor.YELLOW + "Vous n'avez pas reçu de TNT. Restez vigilant !");
          onlinePlayer.sendTitle(ChatColor.BLUE + "Vous n'avez pas de TNT !", "Fuyez les loups !", 10, 70, 20);
        }
      }

      Set<UUID> viewers = new HashSet<>(playersWithTNT);
      for (Player player : Bukkit.getOnlinePlayers()) {
        Team team = player.getScoreboard().getEntryTeam(player.getName());
        if (team != null && (team.getName().equalsIgnoreCase("garde") || team.getName().equalsIgnoreCase("mort"))) {
          viewers.add(player.getUniqueId());
        }
      }

      // Appliquer les effets de glowing uniquement aux joueurs ayant la TNT
      for (UUID glowingUUID : playersWithTNT) {
        Player glowingPlayer = Bukkit.getPlayer(glowingUUID);
        if (glowingPlayer == null || !glowingPlayer.isOnline()) continue;

        for (UUID viewerUUID : viewers) {
          Player viewer = Bukkit.getPlayer(viewerUUID);
          if (viewer == null || !viewer.isOnline()) continue;
          try {
            plugin.getGlowingEntities().setGlowing(glowingPlayer, viewer, ChatColor.RED);
          } catch (ReflectiveOperationException e) {
            plugin.getLogger().warning("Erreur lors du setGlowing : " + e.getMessage());
            e.printStackTrace();
          }
        }
      }

      if (!playersWithTNT.isEmpty()) {
        String tntPlayers = playersWithTNT.stream()
            .map(Bukkit::getPlayer)
            .filter(Objects::nonNull)
            .map(Player::getName)
            .reduce((p1, p2) -> p1 + ", " + p2)
            .orElse("Aucun joueur");
        Bukkit.broadcastMessage(ChatColor.GOLD + "Joueurs avec une TNT : " + ChatColor.RED + tntPlayers);
      }


      startTNTActionBarTask();

      // Démarre le compte à rebours
      startTNTCountdown();
    }
  }

  @EventHandler
  public void onPlayerInteract(EntityDamageByEntityEvent event) {
    LTTEState state = plugin.getLTTEState();
    if (state == LTTEState.OFF || state == LTTEState.STOP) return;

    if (!(event.getDamager() instanceof Player giver)) return;
    if (!(event.getEntity() instanceof Player receiver)) return;

    UUID giverUUID = giver.getUniqueId();
    UUID receiverUUID = receiver.getUniqueId();

    if (playersWithTNT.contains(giverUUID) && !playersWithTNT.contains(receiverUUID)) {
      playersWithTNT.add(receiverUUID);


      for (UUID uuid : playersWithTNT) {
        Player tntPlayer = Bukkit.getPlayer(uuid);
        if (tntPlayer == null) continue; // Le joueur n'est pas connecté

        try {
          SquidGame.getInstance().getGlowingEntities().setGlowing(receiver, tntPlayer, ChatColor.RED);
          SquidGame.getInstance().getGlowingEntities().setGlowing(tntPlayer, receiver, ChatColor.RED);
        } catch (ReflectiveOperationException e) {
          e.printStackTrace();
        }

        try {
          SquidGame.getInstance().getGlowingEntities().unsetGlowing(giver, tntPlayer);
          SquidGame.getInstance().getGlowingEntities().unsetGlowing(tntPlayer, giver);
        } catch (ReflectiveOperationException e) {
          e.printStackTrace();
        }
      }

      playersWithTNT.remove(giverUUID);
      giver.sendTitle(ChatColor.BLUE + "Vous n'avez plus de TNT !", "Fuyez les loups !", 10, 40, 20);
      receiver.sendTitle(ChatColor.RED + "Vous avez une TNT !", "Touchez un autre joueur pour la lui donner !", 10, 40, 20);
    }
  }




  private static void startTNTCountdown() {
    int totalTicks = LTTECommandExecutor.getBombTimer() * 20; // Temps total en ticks
    new BukkitRunnable() {
      int remainingTicks = totalTicks;

      @Override
      public void run() {
        try {
          LTTEState state = plugin.getLTTEState();
          if (state != LTTEState.ON || playersWithTNT.isEmpty()) {
            cancel();
            return;
          }

          // Bip toutes les minutes restantes
          if (remainingTicks % 1200 == 0 && remainingTicks > 200) {
            int minutesLeft = remainingTicks / 1200;
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Il reste " + minutesLeft + " minute(s) !");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:block.note_block.pling master @a ~ ~ ~ 1 0.5 1");
          }

          // Bip toutes les secondes pendant les 10 dernières secondes
          if (remainingTicks <= 200 && remainingTicks > 60 && remainingTicks % 20 == 0) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Bip !");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:block.note_block.pling master @a ~ ~ ~ 1 1 1");
          }

          // Bip 6 fois par seconde pendant les 3 dernières secondes
          if (remainingTicks <= 60 && remainingTicks > 20 && remainingTicks % 3 == 0) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:block.note_block.pling master @a ~ ~ ~ 1 1 1");
          }

          // Bip 5 fois pendant la dernière seconde
          if (remainingTicks <= 20) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:block.note_block.pling master @a ~ ~ ~ 1 2 1");
          }


          // Explosion à la fin du timer
          if (remainingTicks <= 0) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stopsound @a");
            for (UUID uuid : new ArrayList<>(playersWithTNT)) {
              Player player = Bukkit.getPlayer(uuid);
              if (player == null || !player.isOnline()) continue;
              try {
                player.sendMessage(ChatColor.RED + "BOOM ! Vous avez explosé !");
                player.getWorld().playSound(player.getLocation(), "minecraft:entity.tnt.primed", 1.0F, 1.0F);
                player.setHealth(0);
                player.getWorld().createExplosion(player.getLocation(), 4.0F, false, false);
                player.setGlowing(false);
                playersWithTNT.remove(player);
              } catch (Exception e) {
                plugin.getLogger().warning("Erreur lors de l'explosion du joueur " + player.getName() + " : " + e.getMessage());
                e.printStackTrace();
              }
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ltte off");
            cancel();
            return;
          }

          remainingTicks--;
        } catch (Exception e) {
          plugin.getLogger().warning("Erreur dans la tâche ActionBar : " + e.getMessage());
          e.printStackTrace();
        }
      }
    }.runTaskTimer(plugin, 0, 1); // Exécute toutes les 1 tick
  }
  private static void startTNTCountdown1() {
    int delayInSeconds = random.nextInt(95 - 35 + 1) + 35; // Génère un nombre entre 35 et 95
    plugin.getLogger().info(ChatColor.RED + "" + delayInSeconds + " secondes");
    int delayInTicks = delayInSeconds * 20;
    new BukkitRunnable() {
      @Override
      public void run() {
        LTTEState state = plugin.getLTTEState();
        if (state != LTTEState.ON || playersWithTNT.isEmpty()) {
          cancel();
          return;
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stopsound @a");

        for (UUID uuid : new ArrayList<>(playersWithTNT)) {
          Player player = Bukkit.getPlayer(uuid);
          if (player == null || !player.isOnline()) continue;
          player.sendMessage(ChatColor.RED + "BOOM ! Vous avez explosé !");
          player.getWorld().playSound(player.getLocation(), "minecraft:entity.tnt.primed", 1.0F, 1.0F);
          player.setHealth(0);
          player.getWorld().createExplosion(player.getLocation(), 4.0F, false, false);
          playersWithTNT.remove(player);
        }

      }
    }.runTaskLater(plugin, delayInTicks);
  }

  public Collection<UUID> getPlayersWithTNT() {
    return playersWithTNT;
  }

  public static void clearPlayersWithTNT() {
    for (UUID uuid : new ArrayList<>(playersWithTNT)) {
      Player player = Bukkit.getPlayer(uuid);
      if (player != null && player.isOnline()) {
        player.sendMessage(ChatColor.GREEN + "Vous n'avez plus de TNT !");
      }
    }
    for (UUID targetUUID : new ArrayList<>(playersWithTNT)) {
      Player target = Bukkit.getPlayer(targetUUID);
      if (target == null || !target.isOnline()) continue;

      for (UUID viewerUUID : new ArrayList<>(playersWithTNT)) {
        Player viewer = Bukkit.getPlayer(viewerUUID);
        if (viewer == null || !viewer.isOnline()) continue;
        try {
          plugin.getGlowingEntities().unsetGlowing(target, viewer);
          target.setGlowing(false);
          plugin.getLogger().info("joueur :" + target.getName() + " n'est plus visible par " + viewer.getName());
        } catch (ReflectiveOperationException e) {
          plugin.getLogger().warning("Erreur lors du unsetGlowing entre " + target.getName() + " et " + viewer.getName() + " : " + e.getMessage());
          e.printStackTrace();
        }
      }
    }

    playersWithTNT.clear();
    plugin.getLogger().info("La liste des loups explosifs a été réinitialisée.");
  }
  private static void startTNTActionBarTask() {
    new BukkitRunnable() {
      @Override
      public void run() {
        LTTEState state = plugin.getLTTEState();
        if (state != LTTEState.ON || playersWithTNT.isEmpty()) {
          cancel();
          return;
        }

        for (UUID uuid : new ArrayList<>(playersWithTNT)) {
          Player player = Bukkit.getPlayer(uuid);
          if (player == null || !player.isOnline()) continue;
          player.sendActionBar(ChatColor.RED + "Vous avez une TNT !");
          player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2, 1, true, false, false));
        }
      }
    }.runTaskTimer(plugin, 0, 1); // Exécute toutes les secondes (20 ticks)
  }
}