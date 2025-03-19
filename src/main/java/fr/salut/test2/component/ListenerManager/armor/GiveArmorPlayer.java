package fr.salut.test2.component.ListenerManager.armor;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class GiveArmorPlayer {
  public static void giveUnbreakableArmor(Player player) {
    // Crée les pièces d'armure en cuir
    ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
    ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
    ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

    // Applique la couleur #00AAAA (cyan clair) à chaque pièce
    LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
    LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
    LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();

    // Applique la teinture et rend les objets incassables
    if (chestplateMeta != null) {
      chestplateMeta.setColor(Color.fromRGB(0, 170, 170));  // #00AAAA en RGB
      chestplateMeta.setUnbreakable(true); // Armure incassable
      chestplateMeta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE); // Cache le tag incassable
      chestplate.setItemMeta(chestplateMeta);
    }
    if (leggingsMeta != null) {
      leggingsMeta.setColor(Color.fromRGB(0, 170, 170));  // #00AAAA en RGB
      leggingsMeta.setUnbreakable(true); // Armure incassable
      leggingsMeta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE); // Cache le tag incassable
      leggings.setItemMeta(leggingsMeta);
    }
    if (bootsMeta != null) {
      bootsMeta.setColor(Color.fromRGB(0, 170, 170));  // #00AAAA en RGB
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
