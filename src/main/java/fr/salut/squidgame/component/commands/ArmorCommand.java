package fr.salut.squidgame.component.commands;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.List;

@Command("armor")
@CommandPermission("sqg.admins.commands.armor")
public class ArmorCommand {

    @DefaultFor("~")
    public void giveArmor(CommandSender sender, @Named("targets") String selector, @Optional String color) {
        List<Entity> entities = Bukkit.selectEntities(sender, selector);

        for (Entity entity : entities) {
            if (entity instanceof Player player){
                if (color==null){
                    giveMailArmor(player);
                } else {
                    giveLeatherArmor(player, color);
                }
            }
        }
    }


    private void giveLeatherArmor(Player player, String hexaColor){
        Color color = null;
        ItemStack boot = new ItemStack(Material.LEATHER_BOOTS);
        ItemStack pant = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);

        LeatherArmorMeta bootMeta = (LeatherArmorMeta) boot.getItemMeta();
        LeatherArmorMeta pantMeta = (LeatherArmorMeta) pant.getItemMeta();
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();

        if (hexaColor!=null){
            if (hexaColor.startsWith("#")){
                hexaColor = hexaColor.replace("#", "");
            }
            int r = getRGB(hexaColor)[0];
            int g = getRGB(hexaColor)[1];
            int b = getRGB(hexaColor)[2];

            color = Color.fromRGB(r, g, b);
        }

        bootMeta.setColor(color);
        pantMeta.setColor(color);
        chestplateMeta.setColor(color);

        bootMeta.setUnbreakable(true); // Armure incassable
        bootMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE); // Cache le tag incassable
        pantMeta.setUnbreakable(true); // Armure incassable
        pantMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE); // Cache le tag incassable
        chestplateMeta.setUnbreakable(true); // Armure incassable
        chestplateMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE); // Cache le tag incassable

        boot.setItemMeta(bootMeta);
        pant.setItemMeta(pantMeta);
        chestplate.setItemMeta(chestplateMeta);

        player.getEquipment().setBoots(boot);
        player.getEquipment().setLeggings(pant);
        player.getEquipment().setChestplate(chestplate);
    }

    private void giveMailArmor(Player player){
        ItemStack boot = new ItemStack(Material.CHAINMAIL_BOOTS);
        ItemStack pant = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);

        ArmorMeta bootMeta = (ArmorMeta) boot.getItemMeta();
        ArmorMeta pantMeta = (ArmorMeta) pant.getItemMeta();
        ArmorMeta chestplateMeta = (ArmorMeta) chestplate.getItemMeta();


        bootMeta.setUnbreakable(true); // Armure incassable
        bootMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE); // Cache le tag incassable
        pantMeta.setUnbreakable(true); // Armure incassable
        pantMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE); // Cache le tag incassable
        chestplateMeta.setUnbreakable(true); // Armure incassable
        chestplateMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE); // Cache le tag incassable

        boot.setItemMeta(bootMeta);
        pant.setItemMeta(pantMeta);
        chestplate.setItemMeta(chestplateMeta);

        player.getEquipment().setBoots(boot);
        player.getEquipment().setLeggings(pant);
        player.getEquipment().setChestplate(chestplate);
    }

    private int[] getRGB(final String rgb) {
        final int[] ret = new int[3];
        for (int i = 0; i < 3; i++)
        {
            ret[i] = Integer.parseInt(rgb.substring(i * 2, i * 2 + 2), 16);
        }
        return ret;
    }
}
