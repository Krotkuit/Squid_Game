package fr.salut.squidgame.component.ListenerManager;

import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class TagListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getPlayer().getScoreboardTags().contains("no_move")) {

            if (event.getFrom().distanceSquared(event.getTo()) > 0) event.setTo(event.getFrom().setDirection(event.getTo().getDirection()));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (!player.getScoreboardTags().contains("no_drop"))
            return;

        ItemStack protectedItem = null;
        if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
            protectedItem = event.getCurrentItem();
        } else if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
            protectedItem = event.getCursor();
        }
        if (protectedItem == null)
            return;

        if (event.getClickedInventory() != null) {
            InventoryType invType = event.getClickedInventory().getType();
            if (invType != InventoryType.PLAYER &&
                    invType != InventoryType.CREATIVE &&
                    invType != InventoryType.CRAFTING) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getSlotType() == InventoryType.SlotType.CRAFTING) {
            event.setCancelled(true);
            return;
        }

        if (event.getClick() == ClickType.DROP || event.getClick() == ClickType.CONTROL_DROP) {
            event.setCancelled(true);
            return;
        }

        if (event.isShiftClick()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    void onItemDrop(PlayerDropItemEvent event) {
        if (!event.getPlayer().getScoreboardTags().contains("no_drop"))
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemFrameInteract(PlayerInteractEntityEvent event) {
        if (!event.getPlayer().getScoreboardTags().contains("no_drop"))
            return;
        if (!(event.getRightClicked() instanceof ItemFrame))
            return;
        event.setCancelled(true);
    }
}
