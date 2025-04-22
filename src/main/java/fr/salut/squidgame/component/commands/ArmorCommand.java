package fr.salut.squidgame.component.commands;

import fr.salut.squidgame.SquidGame;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Subcommand;

@Command("armor")
public class ArmorCommand {

    @DefaultFor("~")
    void defaultArmor(Player sender){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            giveMailArmor(target);
        }
    }

    @Subcommand("hexa")
    void armorPlayer(Player sender, @Named("hexa") String hexa){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            giveLeatherArmor(target, null, hexa);
        }
        // tu peux rajouter un message si tu veux
    }

    @Subcommand("red")
    void armorPlayer(Player sender){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            giveLeatherArmor(target, Color.RED, null);
        }
        // tu peux rajouter un message si tu veux
    }

    //donne armure en mail
    @Subcommand("player mail")
    void armorPlayer(Player sender, @Named("target") Player target){
        giveMailArmor(target);
        // tu peux rajouter un message si tu veux
    }

    @Subcommand("player hexa")
    void armorPlayer(Player sender, @Named("target") Player target, @Named("hexa") String hexa){
        giveLeatherArmor(target, null, hexa);
        // tu peux rajouter un message si tu veux
    }

    @Subcommand("player red")
    void armorPlayerRed(Player sender, @Named("target") Player target){
        giveLeatherArmor(target, Color.RED, null);
        // tu peux rajouter un message si tu veux
    }

    private void giveLeatherArmor(Player player, Color color, String hexaColor){
        ItemStack boot = new ItemStack(Material.LEATHER_BOOTS);
        ItemStack pant = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);

        LeatherArmorMeta bootMeta = (LeatherArmorMeta) boot.getItemMeta();
        LeatherArmorMeta pantMeta = (LeatherArmorMeta) pant.getItemMeta();
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();

        if (color==null && hexaColor!=null){
            if (hexaColor.startsWith("#")){
                hexaColor = hexaColor.replace("#", "");
            }
            color.setRed(getRGB(hexaColor)[0]);
            color.setGreen(getRGB(hexaColor)[1]);
            color.setBlue(getRGB(hexaColor)[2]);
        }

        bootMeta.setColor(color);
        pantMeta.setColor(color);
        chestplateMeta.setColor(color);

        bootMeta.setUnbreakable(true); // Armure incassable
        bootMeta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE); // Cache le tag incassable
        pantMeta.setUnbreakable(true); // Armure incassable
        pantMeta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE); // Cache le tag incassable
        chestplateMeta.setUnbreakable(true); // Armure incassable
        chestplateMeta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE); // Cache le tag incassable

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
