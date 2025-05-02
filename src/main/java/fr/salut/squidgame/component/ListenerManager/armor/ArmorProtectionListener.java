package fr.salut.squidgame.component.ListenerManager.armor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.scoreboard.Team;

public class ArmorProtectionListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Vérifie si c'est un joueur
        if (event.getWhoClicked() instanceof Player) {

            Player player = (Player) event.getWhoClicked();
            Team team = player.getScoreboard().getEntryTeam(player.getName());
            if (team != null && team.getName().equalsIgnoreCase("garde")) return;

            // On ignore si ce n'est pas un clic sur un slot d'armure
            if (event.getSlotType() != org.bukkit.event.inventory.InventoryType.SlotType.ARMOR) {
                return;
            }

            // Si le joueur essaie de retirer son armure, on annule l'événement
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                return; // Ignore si l'item est nul ou si c'est un espace vide
            }

            event.setCancelled(true); // Empêche l'action (retirer l'armure)
        }
    }
}

