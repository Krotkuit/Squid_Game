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
        if (!(arrow.getShooter() instanceof Player player)) return;

        Team team = player.getScoreboard().getEntryTeam(player.getName());
        if (team==null){return;}
        if (!team.getName().equalsIgnoreCase("garde")){return;}

        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon.getType().equals(Material.CROSSBOW)){

            LivingEntity target = (LivingEntity) event.getEntity();
            target.setHealth(0);
        }
    }
}
