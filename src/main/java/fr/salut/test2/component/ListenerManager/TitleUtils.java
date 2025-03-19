package fr.salut.test2.component.ListenerManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TitleUtils {

    // Méthode pour envoyer un titre et un sous-titre au joueur
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        title = ChatColor.translateAlternateColorCodes('&', title);  // Remplace les codes & par des couleurs
        subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);

        // Envoi du titre et sous-titre coloré
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }
}