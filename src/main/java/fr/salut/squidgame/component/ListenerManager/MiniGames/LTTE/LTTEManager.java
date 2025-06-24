package fr.salut.squidgame.component.ListenerManager.MiniGames.LTTE;

import fr.salut.squidgame.SquidGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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

            int tntCount = Math.max((int) Math.ceil(onlinePlayers.size() * 0.1), 1);

            for (int i = 0; i < tntCount && !onlinePlayers.isEmpty(); i++) {
                Player selectedPlayer = onlinePlayers.remove(random.nextInt(onlinePlayers.size()));
                playersWithTNT.add(selectedPlayer);
                selectedPlayer.sendTitle(ChatColor.RED + "Vous avez une TNT !", "Touchez un autre joueur pour la lui donner !", 10, 70, 20);
                selectedPlayer.sendMessage(ChatColor.RED + "Vous avez une TNT ! Touchez un autre joueur pour la lui donner !");
                selectedPlayer.setGlowing(true);
            }

            for (Player onlinePlayer : onlinePlayers) {
                if (!playersWithTNT.contains(onlinePlayer)) {
                    onlinePlayer.sendMessage(ChatColor.YELLOW + "Vous n'avez pas reçu de TNT. Restez vigilant !");
                    onlinePlayer.sendTitle(ChatColor.BLUE + "Vous n'avez pas de TNT !", "Fuyez les loups !", 10, 70, 20);
                    onlinePlayer.setGlowing(false);
                }
            }

            if (!playersWithTNT.isEmpty()) {
                String tntPlayers = playersWithTNT.stream()
                    .map(Player::getName)
                    .reduce((p1, p2) -> p1 + ", " + p2)
                    .orElse("Aucun joueur");
                Bukkit.broadcastMessage(ChatColor.GOLD + "Joueurs avec une TNT : " + ChatColor.RED + tntPlayers);
            }
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
            giver.setGlowing(false);
            receiver.sendMessage(ChatColor.RED + "Vous avez reçu une TNT ! Touchez un autre joueur pour la lui donner !");
            receiver.sendTitle(ChatColor.RED + "Vous avez une TNT !", "Touchez un autre joueur pour la lui donner !", 10, 40, 20);
            giver.setGlowing(true);
        }
    }

    private static void startTNTCountdown() {
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
                    player.setGlowing(false);
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
            player.setGlowing(false);
        }
        playersWithTNT.clear();
        SquidGame.getInstance().getLogger().info("La liste des loups explosifs a été réinitialisée.");
    }
}