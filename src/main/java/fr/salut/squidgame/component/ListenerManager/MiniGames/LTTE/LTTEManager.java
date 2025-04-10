package fr.salut.squidgame.component.ListenerManager.MiniGames.LTTE;

import fr.salut.squidgame.Main;
import fr.salut.squidgame.component.ListenerManager.MiniGames.PRV.PRVState;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;


public class LTTEManager implements Listener {
    private static Main plugin;
    private static final List<Player> playersWithTNT = new ArrayList<>();
    private final Random random = new Random();

    public LTTEManager(Main plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onGameStart(PlayerJoinEvent event) {
        LTTEState gameState = plugin.getLTTEState();

        if (gameState != LTTEState.ON) return; // Vérifie si le jeu est actif

        if (playersWithTNT.isEmpty()) {
            // Filtre les joueurs pour exclure ceux des équipes "mort" et "garde"
            List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
            onlinePlayers.removeIf(player -> {
                Team team = player.getScoreboard().getEntryTeam(player.getName());
                return team != null && (team.getName().equalsIgnoreCase("mort") || team.getName().equalsIgnoreCase("garde"));
            });

            int tntCount = Math.max((int) Math.ceil(onlinePlayers.size() * 0.05), 1);

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
            // Démarre le compte à rebours de 20 secondes
            startTNTCountdown();
        }
    }

    @EventHandler
    public void onPlayerInteract(EntityDamageByEntityEvent event) {
        LTTEState state = plugin.getLTTEState();

        if (state == LTTEState.OFF || state == LTTEState.STOP) return;

        if (!(event.getDamager() instanceof Player)) return;

        Player giver = (Player) event.getDamager();
        if (!(event.getEntity() instanceof Player)) return;

        Player receiver = (Player) event.getEntity();


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

    private void startTNTCountdown() {
        int delayInSeconds = random.nextInt(35 - 15 + 1) + 15; // Génère un nombre entre 15 et 35
        int delayInTicks = delayInSeconds * 20;
        new BukkitRunnable() {
            @Override
            public void run() {
                LTTEState state = plugin.getLTTEState();
                if (state != LTTEState.ON || playersWithTNT.isEmpty()) {
                    cancel();
                    return;
                }

                for (Player player : new ArrayList<>(playersWithTNT)) {
                    player.sendMessage(ChatColor.RED + "BOOM ! Vous avez explosé !");
                    player.getWorld().playSound(player.getLocation(), "minecraft:entity.tnt.primed", 1.0F, 1.0F);
                    player.setHealth(0);
                    player.getWorld().createExplosion(player.getLocation(), 4.0F, false, false);
                    playersWithTNT.remove(player);
                }
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("SquidGame"), delayInTicks);
    }

    public Collection<Player> getPlayersWithTNT() {
        return playersWithTNT;
    }

    public static void clearPlayersWithTNT() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.RED + "L'épreuve a été arrêtée", "", 10, 70, 20);
        }

        for (Player player : new ArrayList<>(playersWithTNT)) {
            player.sendMessage(ChatColor.GREEN + "Vous n'avez plus de TNT !");
        }
        playersWithTNT.clear();
        plugin.getLogger().info("La liste des loups explosifs a été réinitialisée.");
    }
}