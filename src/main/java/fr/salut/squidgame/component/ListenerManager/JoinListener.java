package fr.salut.squidgame.component.ListenerManager;

import fr.salut.squidgame.component.ListenerManager.NumberPlayer.PlayerNumberManager;
import fr.salut.squidgame.component.ListenerManager.armor.GiveArmorPlayer;
import fr.salut.squidgame.component.ListenerManager.compteur.MAJ_compteur;
import lombok.Getter;
import org.bukkit.*;
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
      // attribuer un nombre au joueur
      PlayerNumberManager.getInstance().assignNumber(player);
      int number = PlayerNumberManager.getInstance().getPlayerNumber(player);
      String idFormatted = String.format("%03d", number);
      player.sendMessage("Votre numéro est : " + number);

      // Modifier le pseudo dans le chat
      player.setDisplayName("Joueur #" + idFormatted);
      // Modifier le nom dans le tableau des scores (tab)
      player.setPlayerListName("Joueur #" + idFormatted);

      // Modifier le nom au-dessus de la tête
      player.setCustomName("Joueur #" + idFormatted);

      team.setSuffix(" #" + idFormatted + " ");
      team.addEntry(player.getName());

      player.setCustomNameVisible(true);

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
    new MAJ_compteur();

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