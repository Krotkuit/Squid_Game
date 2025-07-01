package fr.salut.squidgame.component.ListenerManager.MiniGames.LTTE;

import fr.salut.squidgame.SquidGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;


public class LTTEManager implements Listener {
    private static final SquidGame plugin = SquidGame.getInstance();
    private static final List<Player> playersWithTNT = new ArrayList<>();
    private static final Random random = new Random();

    public static void startGame() {
        LTTEState gameState = plugin.getLTTEState();

        if (gameState != LTTEState.ON) return; // Vérifie si le jeu est actif

        if (playersWithTNT.isEmpty()) {
            // Filtre les joueurs pour exclure ceux des équipes "mort" et "garde"
            List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
            onlinePlayers.removeIf(player -> {
                Team team = player.getScoreboard().getEntryTeam(player.getName());
                return team != null && (team.getName().equalsIgnoreCase("mort") || team.getName().equalsIgnoreCase("garde"));
            });

            int tntCount = Math.max((int) Math.ceil(onlinePlayers.size() * 0.15), 1);

            for (int i = 0; i < tntCount && !onlinePlayers.isEmpty(); i++) {
                Player selectedPlayer = onlinePlayers.remove(random.nextInt(onlinePlayers.size()));
                playersWithTNT.add(selectedPlayer);
                selectedPlayer.sendTitle(ChatColor.RED + "Vous avez une TNT !", "Touchez un autre joueur pour la lui donner !", 10, 70, 20);
                selectedPlayer.sendMessage(ChatColor.RED + "Vous avez une TNT ! Touchez un autre joueur pour la lui donner !");
            }

            for (Player onlinePlayer : onlinePlayers) {
                if (!playersWithTNT.contains(onlinePlayer)) {
                    onlinePlayer.sendMessage(ChatColor.YELLOW + "Vous n'avez pas reçu de TNT. Restez vigilant !");
                    onlinePlayer.sendTitle(ChatColor.BLUE + "Vous n'avez pas de TNT !", "Fuyez les loups !", 10, 70, 20);
                }
            }

            if (!playersWithTNT.isEmpty()) {
                String tntPlayers = playersWithTNT.stream()
                    .map(Player::getName)
                    .reduce((p1, p2) -> p1 + ", " + p2)
                    .orElse("Aucun joueur");
                Bukkit.broadcastMessage(ChatColor.GOLD + "Joueurs avec une TNT : " + ChatColor.RED + tntPlayers);
            }


            startTNTActionBarTask();

            // Démarre le compte à rebours de 20 secondes
            startTNTCountdown();
        }
    }

    @EventHandler
    public void onPlayerInteract(EntityDamageByEntityEvent event) {
        LTTEState state = plugin.getLTTEState();

        if (state == LTTEState.OFF || state == LTTEState.STOP) return;

        if (!(event.getDamager() instanceof Player giver)) return;

        if (!(event.getEntity() instanceof Player receiver)) return;

        if (playersWithTNT.contains(giver) && !playersWithTNT.contains(receiver)) {
            // Transfert de la TNT
            playersWithTNT.remove(giver);
            playersWithTNT.add(receiver);

            giver.sendMessage(ChatColor.GREEN + "Vous avez donné la TNT à " + receiver.getName() + " !");
            giver.sendTitle(ChatColor.BLUE + "Vous n'avez plus de TNT !", "Fuyez les loups !", 10, 40, 20);
            receiver.sendMessage(ChatColor.RED + "Vous avez reçu une TNT ! Touchez un autre joueur pour la lui donner !");
            receiver.sendTitle(ChatColor.RED + "Vous avez une TNT !", "Touchez un autre joueur pour la lui donner !", 10, 40, 20);
        }
    }

    private static void startTNTCountdown() {
        int totalTicks = 3000; // Temps total en ticks (2 minutes 30 secondes)

        new BukkitRunnable() {
            int remainingTicks = totalTicks;

            @Override
            public void run() {
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
                    for (Player player : new ArrayList<>(playersWithTNT)) {
                        player.sendMessage(ChatColor.RED + "BOOM ! Vous avez explosé !");
                        player.getWorld().playSound(player.getLocation(), "minecraft:entity.tnt.primed", 1.0F, 1.0F);
                        player.setHealth(0);
                        player.getWorld().createExplosion(player.getLocation(), 4.0F, false, false);
                        playersWithTNT.remove(player);
                    }
                    cancel();
                    return;
                }

                remainingTicks--;
            }
        }.runTaskTimer(plugin, 0, 1); // Exécute toutes les 1 tick
    }
    private static void startTNTCountdown1() {
        int delayInSeconds = random.nextInt(95 - 35 + 1) + 35; // Génère un nombre entre 35 et 95
        SquidGame.getInstance().getLogger().info(ChatColor.RED + "" + delayInSeconds + " secondes");
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

                for (Player player : new ArrayList<>(playersWithTNT)) {
                    player.sendMessage(ChatColor.RED + "BOOM ! Vous avez explosé !");
                    player.getWorld().playSound(player.getLocation(), "minecraft:entity.tnt.primed", 1.0F, 1.0F);
                    player.setHealth(0);
                    player.getWorld().createExplosion(player.getLocation(), 4.0F, false, false);
                    playersWithTNT.remove(player);
                }

            }
        }.runTaskLater(plugin, delayInTicks);
    }

    public Collection<Player> getPlayersWithTNT() {
        return playersWithTNT;
    }

    public static void clearPlayersWithTNT() {
        for (Player player : new ArrayList<>(playersWithTNT)) {
            player.sendMessage(ChatColor.GREEN + "Vous n'avez plus de TNT !");
        }
        playersWithTNT.clear();
        SquidGame.getInstance().getLogger().info("La liste des loups explosifs a été réinitialisée.");
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

                for (Player player : playersWithTNT) {
                    player.sendActionBar(ChatColor.RED + "Vous avez une TNT !");
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 0, true, false, false));
                }
            }
        }.runTaskTimer(plugin, 0, 20); // Exécute toutes les secondes (20 ticks)
    }
}