package fr.salut.squidgame.component.ListenerManager.armor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveArmorPlayer {
  public static void giveUnbreakableArmor(Player player) {
    // Crée les pièces d'armure en côte de maille
    ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
    ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
    ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);

    // Applique les propriétés d'incassabilité
    ItemMeta chestplateMeta = chestplate.getItemMeta();
    ItemMeta leggingsMeta = leggings.getItemMeta();
    ItemMeta bootsMeta = boots.getItemMeta();

    if (chestplateMeta != null) {
      // chestplateMeta.setColor(Color.fromRGB(0, 170, 170));  // #00AAAA en RGB (commenté)
      chestplateMeta.setUnbreakable(true); // Armure incassable
      chestplateMeta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE); // Cache le tag incassable
      chestplate.setItemMeta(chestplateMeta);
    }
    if (leggingsMeta != null) {
      // leggingsMeta.setColor(Color.fromRGB(0, 170, 170));  // #00AAAA en RGB (commenté)
      leggingsMeta.setUnbreakable(true); // Armure incassable
      leggingsMeta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE); // Cache le tag incassable
      leggings.setItemMeta(leggingsMeta);
    }
    if (bootsMeta != null) {
      // bootsMeta.setColor(Color.fromRGB(0, 170, 170));  // #00AAAA en RGB (commenté)
      bootsMeta.setUnbreakable(true); // Armure incassable
      bootsMeta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE); // Cache le tag incassable
      boots.setItemMeta(bootsMeta);
    }

    // Donne l'armure au joueur (sans casque)
    player.getInventory().setChestplate(chestplate);
    player.getInventory().setLeggings(leggings);
    player.getInventory().setBoots(boots);
  }
}