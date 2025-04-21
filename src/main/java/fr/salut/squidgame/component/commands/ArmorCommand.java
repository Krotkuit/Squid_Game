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

    @Subcommand("blue")
    void armorPlayerBlue(Player sender){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            giveLeatherArmor(target, Color.BLUE, null);
        }
    }

    @Subcommand("player blue")
    void armorPlayerBlue(Player sender, @Named("target") Player target){
        giveLeatherArmor(target, Color.BLUE, null);
    }

    @Subcommand("team blue")
    void armorTeamBlue(Player sender, @Named("target") Team team){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            if (team.hasEntity(target)){
                giveLeatherArmor(target, Color.BLUE, null);
            }
        }
    }

    @Subcommand("green")
    void armorPlayerGreen(Player sender){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            giveLeatherArmor(target, Color.GREEN, null);
        }
    }

    @Subcommand("player green")
    void armorPlayerGreen(Player sender, @Named("target") Player target){
        giveLeatherArmor(target, Color.GREEN, null);
    }

    @Subcommand("team green")
    void armorTeamGreen(Player sender, @Named("target") Team team){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            if (team.hasEntity(target)){
                giveLeatherArmor(target, Color.GREEN, null);
            }
        }
    }

    @Subcommand("yellow")
    void armorPlayerYellow(Player sender){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            giveLeatherArmor(target, Color.YELLOW, null);
        }
    }

    @Subcommand("player yellow")
    void armorPlayerYellow(Player sender, @Named("target") Player target){
        giveLeatherArmor(target, Color.YELLOW, null);
    }

    @Subcommand("team yellow")
    void armorTeamYellow(Player sender, @Named("target") Team team){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            if (team.hasEntity(target)){
                giveLeatherArmor(target, Color.YELLOW, null);
            }
        }
    }

    @Subcommand("orange")
    void armorPlayerOrange(Player sender){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            giveLeatherArmor(target, Color.ORANGE, null);
        }
    }

    @Subcommand("player orange")
    void armorPlayerOrange(Player sender, @Named("target") Player target){
        giveLeatherArmor(target, Color.ORANGE, null);
    }

    @Subcommand("team orange")
    void armorTeamOrange(Player sender, @Named("target") Team team){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            if (team.hasEntity(target)){
                giveLeatherArmor(target, Color.ORANGE, null);
            }
        }
    }

    @Subcommand("white")
    void armorPlayerWhite(Player sender){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            giveLeatherArmor(target, Color.WHITE, null);
        }
    }

    @Subcommand("player white")
    void armorPlayerWhite(Player sender, @Named("target") Player target){
        giveLeatherArmor(target, Color.WHITE, null);
    }

    @Subcommand("team white")
    void armorTeamWhite(Player sender, @Named("target") Team team){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            if (team.hasEntity(target)){
                giveLeatherArmor(target, Color.WHITE, null);
            }
        }
    }

    @Subcommand("black")
    void armorPlayerBlack(Player sender){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            giveLeatherArmor(target, Color.BLACK, null);
        }
    }

    @Subcommand("player black")
    void armorPlayerBlack(Player sender, @Named("target") Player target){
        giveLeatherArmor(target, Color.BLACK, null);
    }

    @Subcommand("team black")
    void armorTeamBlack(Player sender, @Named("target") Team team){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            if (team.hasEntity(target)){
                giveLeatherArmor(target, Color.BLACK, null);
            }
        }
    }

    @Subcommand("purple")
    void armorPlayerPurple(Player sender){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            giveLeatherArmor(target, Color.PURPLE, null);
        }
    }

    @Subcommand("player purple")
    void armorPlayerPurple(Player sender, @Named("target") Player target){
        giveLeatherArmor(target, Color.PURPLE, null);
    }

    @Subcommand("team purple")
    void armorTeamPurple(Player sender, @Named("target") Team team){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            if (team.hasEntity(target)){
                giveLeatherArmor(target, Color.PURPLE, null);
            }
        }
    }

    @Subcommand("fuchsia")
    void armorPlayerFuchsia(Player sender){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            giveLeatherArmor(target, Color.FUCHSIA, null);
        }
    }

    @Subcommand("player fuchsia")
    void armorPlayerFuchsia(Player sender, @Named("target") Player target){
        giveLeatherArmor(target, Color.FUCHSIA, null);
    }

    @Subcommand("team fuchsia")
    void armorTeamFuchsia(Player sender, @Named("target") Team team){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            if (team.hasEntity(target)){
                giveLeatherArmor(target, Color.FUCHSIA, null);
            }
        }
    }

    @Subcommand("aqua")
    void armorPlayerAqua(Player sender){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            giveLeatherArmor(target, Color.AQUA, null);
        }
    }

    @Subcommand("player aqua")
    void armorPlayerAqua(Player sender, @Named("target") Player target){
        giveLeatherArmor(target, Color.AQUA, null);
    }

    @Subcommand("team aqua")
    void armorTeamAqua(Player sender, @Named("target") Team team){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            if (team.hasEntity(target)){
                giveLeatherArmor(target, Color.AQUA, null);
            }
        }
    }

    @Subcommand("lime")
    void armorPlayerLime(Player sender){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            giveLeatherArmor(target, Color.LIME, null);
        }
    }

    @Subcommand("player lime")
    void armorPlayerLime(Player sender, @Named("target") Player target){
        giveLeatherArmor(target, Color.LIME, null);
    }

    @Subcommand("team lime")
    void armorTeamLime(Player sender, @Named("target") Team team){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            if (team.hasEntity(target)){
                giveLeatherArmor(target, Color.LIME, null);
            }
        }
    }


    @Subcommand("red")
    void armorPlayerRed(Player sender){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            giveLeatherArmor(target, Color.RED, null);
        }
    }

    @Subcommand("player red")
    void armorPlayerRed(Player sender, @Named("target") Player target){
        giveLeatherArmor(target, Color.RED, null);
    }

    @Subcommand("team red")
    void armorTeamRed(Player sender, @Named("target") Team team){
        for (Player target : SquidGame.getInstance().getServer().getOnlinePlayers()){
            if (team.hasEntity(target)){
                giveLeatherArmor(target, Color.RED, null);
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
