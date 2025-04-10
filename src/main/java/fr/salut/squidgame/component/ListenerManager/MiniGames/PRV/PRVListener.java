package fr.salut.squidgame.component.ListenerManager.MiniGames.PRV;

import fr.salut.squidgame.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.Set;

public class PRVListener implements Listener {

    private static Main plugin;
    private static final Set<Player> teleportedPlayers = new HashSet<>();

    public PRVListener(Main plugin) {
        this.plugin = plugin;
    }

    private final Location poulePrisonCenter = new Location(Bukkit.getWorld("world"), -55.5, -60, -228.5);
    private final Location renardPrisonCenter = new Location(Bukkit.getWorld("world"), -99.5, -60, -215.5);
    private final Location viperePrisonCenter = new Location(Bukkit.getWorld("world"), -88.5, -60, -259.5);
    private final double prisonRadiusSquared = 25;

    @EventHandler
    public void onPlayerInteract(EntityDamageByEntityEvent event) {
        PRVState state = plugin.getPrvState();

        if (state == PRVState.OFF || state == PRVState.STOP) return;

        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();
        if (!(event.getEntity() instanceof Player)) return;

        Player target = (Player) event.getEntity();

        if (isPlayerInTeam(player, "poule") && isPlayerInTeam(target, "vipere")) {
            plugin.teleportPlayer(target, poulePrisonCenter.getX(), poulePrisonCenter.getY(), poulePrisonCenter.getZ());
            teleportedPlayers.add(target);
            player.sendMessage("Vous avez envoyé " + target.getName() + " dans votre prison !");
        } else if (isPlayerInTeam(player, "vipere") && isPlayerInTeam(target, "renard")) {
            plugin.teleportPlayer(target, viperePrisonCenter.getX(), viperePrisonCenter.getY(), viperePrisonCenter.getZ());
            teleportedPlayers.add(target);
            player.sendMessage("Vous avez envoyé " + target.getName() + " dans votre prison !");
        } else if (isPlayerInTeam(player, "renard") && isPlayerInTeam(target, "poule")) {
            plugin.teleportPlayer(target, renardPrisonCenter.getX(), renardPrisonCenter.getY(), renardPrisonCenter.getZ());
            teleportedPlayers.add(target);
            player.sendMessage("Vous avez envoyé " + target.getName() + " dans votre prison !");
        } else {
            player.sendMessage("Vous ne pouvez pas interagir avec ce joueur.");
        }
    }

    private static final Set<Player> playersWhoFreedPrison = new HashSet<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        PRVState state = plugin.getPrvState();

        if (state == PRVState.OFF) return;

        Player player = event.getPlayer();
        Location to = event.getTo();

        if (to == null || to.getWorld() == null) return;

        // Vérifie si le joueur est emprisonné
        if (teleportedPlayers.contains(player)) {
            if (isPlayerInTeam(player, "vipere")) {
                if (!isInPrisonZone(player, poulePrisonCenter)) {
                    player.teleport(poulePrisonCenter);
                    player.sendMessage("§cVous ne pouvez pas quitter §fla prison des §fPoules §f!");
                }
            } else if (isPlayerInTeam(player, "poule")) {
                if (!isInPrisonZone(player, renardPrisonCenter)) {
                    player.teleport(renardPrisonCenter);
                    player.sendMessage("§cVous ne pouvez pas quitter §fla prison des §6Renards §f!");
                }
            } else if (isPlayerInTeam(player, "renard")) {
                if (!isInPrisonZone(player, viperePrisonCenter)) {
                    player.teleport(viperePrisonCenter);
                    player.sendMessage("§cVous ne pouvez pas quitter §fla prison des §aVipères §f!");
                }
            }
            return; // Empêche toute autre logique pour les joueurs emprisonnés
        }

        // Vérifie si le joueur est libre (non emprisonné)
        if (!teleportedPlayers.contains(player)) {
            if (isPlayerInTeam(player, "vipere") && isInPrisonZone(player, poulePrisonCenter)) {
                if (!playersWhoFreedPrison.contains(player)) {
                    releasePrisoners("vipere", player);
                    playersWhoFreedPrison.add(player);
                }
            } else if (isPlayerInTeam(player, "poule") && isInPrisonZone(player, renardPrisonCenter)) {
                if (!playersWhoFreedPrison.contains(player)) {
                    releasePrisoners("poule", player);
                    playersWhoFreedPrison.add(player);
                }
            } else if (isPlayerInTeam(player, "renard") && isInPrisonZone(player, viperePrisonCenter)) {
                if (!playersWhoFreedPrison.contains(player)) {
                    releasePrisoners("renard", player);
                    playersWhoFreedPrison.add(player);
                }
            } else {
                // Si le joueur quitte la zone de la prison, on le retire de la liste
                playersWhoFreedPrison.remove(player);
            }
        }
    }

    // Méthode pour libérer tous les joueurs emprisonnés d'une équipe
    private void releasePrisoners(String team, Player liberator) {
        PRVState state = plugin.getPrvState();

        if (state == PRVState.OFF || state == PRVState.STOP) return;
        final boolean[] hasFreedPlayers = {false}; // Utilisation d'un tableau pour rendre la variable mutable

        // Parcourt les joueurs emprisonnés
        teleportedPlayers.removeIf(player -> {
            if (isPlayerInTeam(player, team)) {
                player.sendMessage("Vous avez été §blibéré §fde la prison !");
                hasFreedPlayers[0] = true; // Indique qu'un joueur a été libéré
                return true; // Retire le joueur de la liste des emprisonnés
            }
            return false;
        });

        // Envoie un message au libérateur uniquement si des joueurs ont été libérés
        if (hasFreedPlayers[0]) {
            liberator.sendMessage("Vous avez §blibéré §ftous les joueurs emprisonnés de votre équipe !");
        }
    }

    private boolean isPlayerInTeam(Player player, String team) {
        Team playerTeam = player.getScoreboard().getEntryTeam(player.getName());
        return playerTeam != null && playerTeam.getName().equalsIgnoreCase(team);
    }

    private boolean isInPrisonZone(Player player, Location prisonCenter) {
        Location playerLocation = player.getLocation();
        if (prisonCenter.getWorld() == null || playerLocation.getWorld() == null) {
            return false;
        }
        if (!playerLocation.getWorld().equals(prisonCenter.getWorld())) {
            return false;
        }
        double distanceSquared = playerLocation.distanceSquared(prisonCenter);
        return distanceSquared <= prisonRadiusSquared;
    }

    public static void resetPrisoners() {
        teleportedPlayers.clear();
        playersWhoFreedPrison.clear();
        plugin.getLogger().info("Les listes de prisonniers ont été réinitialisées.");
    }
}