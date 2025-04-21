package fr.salut.squidgame.component.commands;

import fr.salut.squidgame.SquidGame;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scoreboard.Team;
import revxrsal.commands.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Command("armor")
public class ArmorCommand {

    private static final Map<String, Color> COLORS = new HashMap<>();

    static {
        COLORS.put("white", Color.WHITE);
        COLORS.put("orange", Color.ORANGE);
        COLORS.put("magenta", Color.FUCHSIA);
        COLORS.put("light_blue", Color.AQUA);
        COLORS.put("yellow", Color.YELLOW);
        COLORS.put("lime", Color.LIME);
        COLORS.put("pink", Color.FUCHSIA);
        COLORS.put("gray", Color.GRAY);
        COLORS.put("light_gray", Color.SILVER);
        COLORS.put("cyan", Color.TEAL);
        COLORS.put("purple", Color.PURPLE);
        COLORS.put("blue", Color.BLUE);
        COLORS.put("brown", Color.MAROON);
        COLORS.put("green", Color.GREEN);
        COLORS.put("red", Color.RED);
        COLORS.put("black", Color.BLACK);
    }

    @DefaultFor("~")
    void defaultArmor(Player sender){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            giveMailArmor(target);
        }
    }

    @Subcommand("hexa")
    void armorHexa(Player sender, @Named("hexa") String hexa){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            giveLeatherArmor(target, null, hexa);
        }
    }

    @Subcommand("player hexa")
    void armorPlayerHexa(Player sender, @Named("target") Player target, @Named("hexa") String hexa){
        giveLeatherArmor(target, null, hexa);
    }

    @Subcommand("team hexa")
    void armorTeamHexa(Player sender, @Named("team") Team team, @Named("hexa") String hexa){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            if (team.hasEntity(target)) {
                giveLeatherArmor(target, null, hexa);
            }
        }
    }

    @Subcommand("<color>")
    void armorColor(Player sender, @Named("color") String colorName){
        Color color = COLORS.get(colorName.toLowerCase());
        if (color == null) {
            sender.sendMessage("§cCouleur inconnue.");
            return;
        }
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            giveLeatherArmor(target, color, null);
        }
    }

    @Subcommand("player <target> <color>")
    void armorPlayerColor(Player sender, @Named("target") Player target, @Named("color") String colorName){
        Color color = COLORS.get(colorName.toLowerCase());
        if (color == null) {
            sender.sendMessage("§cCouleur inconnue.");
            return;
        }
        giveLeatherArmor(target, color, null);
    }

    @Subcommand("team <color>")
    void armorTeamColor(Player sender, @Named("team") Team team, @Named("color") String colorName){
        Color color = COLORS.get(colorName.toLowerCase());
        if (color == null) {
            sender.sendMessage("§cCouleur inconnue.");
            return;
        }
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            if (team.hasEntity(target)) {
                giveLeatherArmor(target, color, null);
            }
        }
    }

    private void giveLeatherArmor(Player player, Color color, String hexaColor){
        if (color == null && hexaColor != null){
            if (hexaColor.startsWith("#")){
                hexaColor = hexaColor.substring(1);
            }
            int[] rgb = getRGB(hexaColor);
            color = Color.fromRGB(rgb[0], rgb[1], rgb[2]);
        }

        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);

        applyColor(boots, color);
        applyColor(leggings, color);
        applyColor(chestplate, color);
        applyColor(helmet, color);

        player.getEquipment().setBoots(boots);
        player.getEquipment().setLeggings(leggings);
        player.getEquipment().setChestplate(chestplate);
        player.getEquipment().setHelmet(helmet);
    }

    private void applyColor(ItemStack item, Color color){
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
        item.setItemMeta(meta);
    }

    private void giveMailArmor(Player player){
        player.getEquipment().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
        player.getEquipment().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
        player.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        player.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
    }

    private int[] getRGB(final String hex) {
        final int[] ret = new int[3];
        for (int i = 0; i < 3; i++) {
            ret[i] = Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return ret;
    }
}
