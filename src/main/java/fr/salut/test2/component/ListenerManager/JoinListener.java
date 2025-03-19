package fr.salut.test2.component.ListenerManager;

import fr.salut.test2.component.ListenerManager.armor.GiveArmorPlayer;
import fr.salut.test2.component.ListenerManager.compteur.MAJ_compteur;
import fr.salut.test2.component.player.TPlayer;
import fr.salut.test2.manager.player.PlayersManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.scoreboard.Team;

@Getter
public class JoinListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    Team team = Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(player.getName());
    if (team != null && team.getName().equalsIgnoreCase("garde")) return;
    //PlayerRightListener.giveRight(player);
    player.teleport(new Location(Bukkit.getWorld("world"), -36, -55, 31));
    // Vérifier si c'est la première connexion du joueur
    if (!player.hasPlayedBefore()) {
      // Donner un armure en cuir teintée au joueur
      GiveArmorPlayer.giveUnbreakableArmor(player);

      // Forcer le joueur en mode Adventure
      player.setGameMode(GameMode.ADVENTURE);

      // Ajouter le tag "joueur"
      player.addScoreboardTag("joueur");

      // Ajouter le joueur dans l'équipe "joueur"
      team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("joueur");

      // Ajouter le joueur dans l'équipe "joueur"
      team.addEntry(player.getName());
    }
    // mise à jour du compteur
    MAJ_compteur.MAJ_compteur();

    Team team_p = player.getScoreboard().getEntryTeam(player.getName());
    if (team_p == null) return;
    if (team_p.getName().equalsIgnoreCase("mort")){
      player.teleport(new Location(Bukkit.getWorld("world"), -41, -46, -3));
    }
  }
  @EventHandler
  public void onPlayerDownloadTexturePack(PlayerResourcePackStatusEvent event) {
    Player player = event.getPlayer();
    if (!player.hasPlayedBefore()) {
      TitleUtils.sendTitle(
        player,
        "&k&fBienvenue &k&fdans", // Titre
        "&k&d&l■&k&a&lSquid &k&d&lGame&k&a&l■", // Sous-titre
        10, 180, 20
      );
      player.playSound(player, Sound.MUSIC_DISC_13, 1, 1);
    }
  }

}

