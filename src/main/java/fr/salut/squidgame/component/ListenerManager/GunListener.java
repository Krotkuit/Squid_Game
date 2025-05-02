package fr.salut.squidgame.component.ListenerManager;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

public class GunListener implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player shooter)) return;
        if (!(event.getEntity() instanceof Player target)) return;

        Team shooterTeam = shooter.getScoreboard().getEntryTeam(shooter.getName());
        if (shooterTeam==null) return;
        if (!shooterTeam.getName().equalsIgnoreCase("garde")) return;

        Team targetTeam = target.getScoreboard().getEntryTeam(target.getName());
        if (targetTeam != null && targetTeam.getName().equalsIgnoreCase("garde")) return;

        ItemStack weapon = shooter.getInventory().getItemInMainHand();
        if (weapon.getType().equals(Material.CROSSBOW)){
            target.setHealth(0);
        }
    }
}
