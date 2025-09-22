package fr.salut.squidgame.component.ListenerManager.GameZone;

import fr.salut.squidgame.component.commands.EpreuveCommand;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;
import java.util.function.Supplier;

public class GameZoneManager implements Listener {

  public static final Map<String, Zone> gameZones = new HashMap<>();

  private final Location centerCarrousel = new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 0.5, 65, 41.5);
  private final Location centerCordeASauter = new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -56.5, -52, 0.5);

  public GameZoneManager() {
    // Définir les zones pour chaque épreuve
    Zone LobbyZone = new Zone();
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -71.7, -59, -16.7),
        new Location(Bukkit.getWorld("world"), -8.3, -43.79, 43.7)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -5.3, -59, 41.7),
        new Location(Bukkit.getWorld("world"), -74.7, -43.79, -14.7)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -70.7, -59, -17.7),
        new Location(Bukkit.getWorld("world"), -9.3, -43.79, 44.7)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -5.3, -59, -14.7),
        new Location(Bukkit.getWorld("world"), -74, -43.79, 41.7)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -7.3, -59, -15.7),
        new Location(Bukkit.getWorld("world"), -72.7, -43.79, 42.7)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -69.7, -59, -19.7),
        new Location(Bukkit.getWorld("world"), -56.3, -42.79, -17.3)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -56.7, -59, -18.7),
        new Location(Bukkit.getWorld("world"), -23.3, -43.79, -17.3)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -10.3, -59, -19.7),
        new Location(Bukkit.getWorld("world"), -23.7, -43.79, -17.3)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -10.3, -59, 46.7),
        new Location(Bukkit.getWorld("world"), -69.7, -43.79, 44.3)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -51.7, -59, -18.3),
        new Location(Bukkit.getWorld("world"), -49.3, -57.79, -28.7)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -49.3, -59, -26.3),
        new Location(Bukkit.getWorld("world"), -78.7, -57.79, -28.7)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -78.7, -59, -26.3),
        new Location(Bukkit.getWorld("world"), -76.3, -57.79, -34.7)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -66.3, -59, -32.3),
        new Location(Bukkit.getWorld("world"), -73.7, -57.79, -45.7)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -88.7, -59, -45.7),
        new Location(Bukkit.getWorld("world"), -81.3, -57.79, -32.3)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -81.7, -59, -34.7),
        new Location(Bukkit.getWorld("world"), -71.3, -57.79, -32.3)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -27.3, -59, -18.3),
        new Location(Bukkit.getWorld("world"), -30.7, -57.79, -23.7)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -22.3, -59, -21.3),
        new Location(Bukkit.getWorld("world"), -30.7, -57.79, -23.7)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -24.7, -59, -26.7),
        new Location(Bukkit.getWorld("world"), -22.3, -57.79, -21.3)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -17.3, -59, -26.3),
        new Location(Bukkit.getWorld("world"), -29.7, -55.79, -30.7)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -27.7, -55.79, -38.7),
        new Location(Bukkit.getWorld("world"), -19.3, -59, -30.3)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -18.3, -59, -32.3),
        new Location(Bukkit.getWorld("world"), -28.7, -55.79, -37.7)
    );
    LobbyZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -29.7, -59, -33.3),
        new Location(Bukkit.getWorld("world"), -17.3, -55.79, -36.7)
    );
    gameZones.put("Lobby", LobbyZone);

    Zone StairsZone = new Zone();
    StairsZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 216.3, -60, 25.7),
        new Location(Bukkit.getWorld("world"), 279.7, 12.201, -36.7)
    );
    gameZones.put("Escaliers", StairsZone);

    Zone zone123Soleil = new Zone();
    // [ Aire de jeu ] //
    zone123Soleil.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), 35.7, 1, 17.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), -33.7, 21, 203.7)
    );

    // [ Transition ascenseur / aire de jeu ] //
    zone123Soleil.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), 21.7, 1, 21),
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), -20.7, 4.21, 6.3)
    );
    zone123Soleil.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), -3.7, 1, 6.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), 4.7, 4.21, -2.7)
    );

    // [ Ascenseur ] //
    zone123Soleil.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), -3.7, 1, 5.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), 4.7, 4.201, -9.7)
    );
    zone123Soleil.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), -4.7, 1, 4.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), 5.7, 4.201, -9.7)
    );
    zone123Soleil.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), -7.7, 1, 3.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), 8.7, 4.201, -9.7)
    );
    zone123Soleil.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), -8.7, 1, 2.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), 9.7, 4.201, -8.7)
    );
    zone123Soleil.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), -9.7, 1, 1.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), 10.7, 4.201, -7.7)
    );
    zone123Soleil.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), -10.7, 1, 0.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), 11.7, 4.201, -6.7)
    );

    gameZones.put("123Soleil", zone123Soleil);

    Zone zoneArcenCiel = new Zone();
    // [ Aire de jeu ] //
    zoneArcenCiel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), 55.7, 64.5, 6.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), -54.7, 86.201, 90.7)
    );

    // [ Ascenseur ] //
    zoneArcenCiel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), -2.7, 65, 7.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), 3.7, 68.201, 4.3)
    );
    zoneArcenCiel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), -3.7, 65, 4.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), 4.7, 68.201, -10.7)
    );
    zoneArcenCiel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), -4.7, 65, 3.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), 5.7, 68.201, -10.7)
    );
    zoneArcenCiel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), -7.7, 65, 2.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), 8.7, 68.201, -10.7)
    );
    zoneArcenCiel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), -8.7, 65, 1.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), 9.7, 68.201, -9.7)
    );
    zoneArcenCiel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), -9.7, 65, 0.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), 10.7, 68.201, -8.7)
    );
    zoneArcenCiel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), -10.7, 65, -0.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), 11.7, 68.201, -7.7)
    );

    gameZones.put("ArcEnCiel", zoneArcenCiel);

    Zone zoneCarrousel = new Zone();

    // [ Ascenseur ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -2.7, 65, 9.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 3.7, 69, -0.7)
    );

    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -3.7, 65, 0.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 4.7, 68.2, -14.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -4.7, 65, -0.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 5.7, 68.201, -14.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -7.7, 65, -1.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 8.7, 68.201, -14.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -8.7, 65, -2.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 9.7, 68.201, -13.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -9.7, 65, -3.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 10.7, 68.201, -12.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -10.7, 65, -4.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 11.7, 68.201, -11.7)
    );


    // [ Salle 1 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 18.7, 66.201, 2.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 16.3, 65, 12.7)
    );

    // [ Salle 2 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 24.7, 65, 6.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 22.3, 66.201, 16.7)
    );

    // [ Salle 3 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 25.3, 65, 17.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 35.7, 66.201, 19.7)
    );

    // [ Salle 4 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 29.3, 65, 23.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 39.7, 66.201, 25.7)
    );

    // [ Salle 5 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 32.3, 65, 32.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 42.7, 66.201, 34.7)
    );

    // [ Salle 6 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 33.3, 65, 40.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 43.7, 66.201, 42.7)
    );

    // [ Salle 7 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 32.3, 65, 48.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 42.7, 66.201, 50.7)
    );

    // [ Salle 8 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 29.3, 65, 57.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 39.7, 66.201, 59.7)
    );

    // [ Salle 9 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 25.3, 65, 63.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 35.7, 66.201, 65.7)
    );

    // [ Salle 10 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 22.3, 66.201, 76.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 24.7, 65, 66.3)
    );

    // [ Salle 11 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 16.3, 66.201, 80.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 18.7, 65, 70.3)
    );

    // [ Salle 12 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 7.3, 66.201, 83.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 9.7, 65, 73.3)
    );

    // [ Salle 13 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -0.7, 66.201, 84.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 1.7, 65, 74.3)
    );

    // [ Salle 14 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -8.7, 66.201, 83.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -6.3, 65, 73.3)
    );

    // [ Salle 15 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -17.7, 66.201, 80.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -15.3, 65, 70.3)
    );

    // [ Salle 16 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -23.7, 66.201, 76.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -21.3, 65, 66.3)
    );

    // [ Salle 17 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -34.7, 66.201, 63.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -24.3, 65, 65.7)
    );

    // [ Salle 18 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -38.7, 66.201, 57.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -28.3, 65, 59.7)
    );

    // [ Salle 19 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -41.7, 66.201, 48.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -31.3, 65, 50.7)
    );

    // [ Salle 20 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -42.7, 66.201, 40.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -32.3, 65, 42.7)
    );

    // [ Salle 21 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -41.7, 66.201, 32.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -31.3, 65, 34.7)
    );

    // [ Salle 22 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -38.7, 66.201, 23.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -28.3, 65, 25.7)
    );

    // [ Salle 23 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -34.7, 66.201, 17.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -24.3, 65, 19.7)
    );

    // [ Salle 24 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -21.3, 65, 16.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -23.7, 66.201, 6.3)
    );

    // [ Salle 25 ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -17.7, 66.201, 2.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -15.3, 65, 12.7)
    );


    gameZones.put("Carrousel", zoneCarrousel);

    Zone zonePRV = new Zone();
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -53.3, -60, -206.3),
        new Location(Bukkit.getWorld("world"), -109.7, -32.79, -262.7)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -54.3, -60, -263.7),
        new Location(Bukkit.getWorld("world"), -108.7, -32.79, -205.3)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -55.3, -60, -264.7),
        new Location(Bukkit.getWorld("world"), -107.7, -32.79, -204.3)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -57.3, -60, -265.7),
        new Location(Bukkit.getWorld("world"), -105.7, -32.79, -203.3)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -104.7, -60, -202.3),
        new Location(Bukkit.getWorld("world"), -58.3, -32.79, -266.7)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -59.3, -60, -267.7),
        new Location(Bukkit.getWorld("world"), -103.7, -32.79, -201.3)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -101.7, -60, -200.3),
        new Location(Bukkit.getWorld("world"), -61.3, -32.79, -268.7)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -63.3, -60, -269.7),
        new Location(Bukkit.getWorld("world"), -99.7, -32.79, -199.3)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -97.7, -60, -198.3),
        new Location(Bukkit.getWorld("world"), -65.3, -32.79, -270.7)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -67.3, -60, -271.7),
        new Location(Bukkit.getWorld("world"), -95.7, -32.79, -197.3)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -91.7, -60, -196.3),
        new Location(Bukkit.getWorld("world"), -71.3, -32.79, -272.7)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -75.3, -60, -273.7),
        new Location(Bukkit.getWorld("world"), -87.7, -32.79, -195.3)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -110.7, -60, -207.3),
        new Location(Bukkit.getWorld("world"), -52.3, -32.79, -261.7)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -51.3, -60, -260.7),
        new Location(Bukkit.getWorld("world"), -111.7, -32.79, -208.3)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -112.7, -60, -210.3),
        new Location(Bukkit.getWorld("world"), -50.3, -32.79, -258.7)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -49.3, -60, -257.7),
        new Location(Bukkit.getWorld("world"), -113.7, -32.79, -211.3)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -114.7, -60, -212.3),
        new Location(Bukkit.getWorld("world"), -48.3, -32.79, -256.7)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -47.3, -60, -254.7),
        new Location(Bukkit.getWorld("world"), -115.7, -32.8, -214.3)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -116.7, -60, -216.3),
        new Location(Bukkit.getWorld("world"), -46.3, -32.79, -252.7)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -45.3, -60, -250.7),
        new Location(Bukkit.getWorld("world"), -117.7, -32.79, -218.3)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -118.7, -60, -220.3),
        new Location(Bukkit.getWorld("world"), -44.3, -32.79, -248.7)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -43.3, -60, -244.7),
        new Location(Bukkit.getWorld("world"), -119.7, -32.79, -224.3)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -120.7, -60, -228.3),
        new Location(Bukkit.getWorld("world"), -42.3, -32.79, -240.7)
    );

    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -87.3, -59, -228.7),
        new Location(Bukkit.getWorld("world"), -75.3, -32.79, -240.7)
    );
    zonePRV.addSubZone(
        new Location(Bukkit.getWorld("world"), -98.7, -62, -206.3),
        new Location(Bukkit.getWorld("world"), -55.3, -55, -274.7)
    );

    gameZones.put("Poule_Renard_Vipere", zonePRV);

    Zone zoneLTTE = new Zone();
    // [ Aire de jeu ] //
    zoneLTTE.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 7.3, 64.5, 117.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 95.7, 109.201, 12.3)
    );
    zoneLTTE.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 77.7, 65, 12.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 16.3, 68.201, -6.7)
    );
    zoneLTTE.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 3.3, 65, 42.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 14.3, 68.201, 12.3)
    );
    zoneLTTE.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 4.2, 65, 11.8),
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), -3.2, 68.201, 16.2)
    );
    zoneLTTE.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), -3.7, 65, 13.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 6.7, 68.201, 15.7)
    );

    // [ Ascenseur ] //
    zoneLTTE.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), -2.7, 65, 12.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 3.7, 68.201, 8.3)
    );
    zoneLTTE.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), -3.7, 65, 9.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 4.7, 68.201, -5.7)
    );
    zoneLTTE.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), -4.7, 65, 8.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 5.7, 68.201, -5.7)
    );
    zoneLTTE.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), -7.7, 65, 7.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 8.7, 68.201, -5.7)
    );
    zoneLTTE.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), -8.7, 65, 6.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 9.7, 68.201, -4.7)
    );
    zoneLTTE.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), -9.7, 65, 5.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 10.7, 68.201, -3.7)
    );
    zoneLTTE.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), -10.7, 65, 4.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 11.7, 68.201, -2.7)
    );

    gameZones.put("LTTE", zoneLTTE);

    Zone zoneP4 = new Zone();

    // [ Aire de jeu ] //
    zoneP4.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), -0.3, 65, -39.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), -56.7, 84.21, 40.7)
    );

    // [ Ascenseur ] //
    zoneP4.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), -0.7, 65, -2.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), 3.7, 68.201, 3.7)
    );
    zoneP4.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), 3.3, 65, -3.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), 18.7, 68.201, 4.7)
    );
    zoneP4.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), 4.3, 65, -4.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), 18.7, 68.201, 5.7)
    );
    zoneP4.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), 5.3, 65, -7.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), 18.7, 68.201, 8.7)
    );
    zoneP4.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), 6.3, 65, -8.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), 17.7, 68.201, 9.7)
    );
    zoneP4.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), 7.3, 65, -9.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), 16.7, 68.201, 10.7)
    );
    zoneP4.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), 8.3, 65, -10.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), 15.7, 68.201, 11.7)
    );

    gameZones.put("Puissance4", zoneP4);

    Zone zoneBilles = new Zone();
    // [ Aire de jeu ] //
    zoneBilles.addSubZone(
        new Location(Bukkit.getWorld("world"), 179.7, -60, -93.7),
        new Location(Bukkit.getWorld("world"), 116.3, -37.79, -36.3)
    );
    zoneBilles.addSubZone(
        new Location(Bukkit.getWorld("world"), 155.7, -60, -38.7),
        new Location(Bukkit.getWorld("world"), 152.3, -58.79, -34.3)
    );
    zoneBilles.addSubZone(
        new Location(Bukkit.getWorld("world"), 148.3, -56.79, -34.7),
        new Location(Bukkit.getWorld("world"), 159.7, -60, -29.3)
    );
    zoneBilles.addSubZone(
        new Location(Bukkit.getWorld("world"), 149.3, -60, -28.3),
        new Location(Bukkit.getWorld("world"), 158.7, -56.8, -34.7)
    );
    zoneBilles.addSubZone(
        new Location(Bukkit.getWorld("world"), 147.3, -60, -33.7),
        new Location(Bukkit.getWorld("world"), 160.7, -56.79, -30.3)
    );

    gameZones.put("Billes", zoneBilles);

    Zone CordeASauterZone = new Zone();
    // [ Aire de jeu ] //
    CordeASauterZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -0.3, 127, -99.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -110.7, 171.201, 100.7)
    );

    // [ Ascenseur ] //
    CordeASauterZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -0.7, 135, -2.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), 3.7, 138.201, 3.7)
    );
    CordeASauterZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), 3.3, 135, -3.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), 18.7, 138.201, 4.7)
    );
    CordeASauterZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), 4.3, 135, -4.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), 18.7, 138.201, 5.7)
    );
    CordeASauterZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), 5.3, 135, -7.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), 18.7, 138.201, 8.7)
    );
    CordeASauterZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), 6.3, 135, -8.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), 17.7, 138.201, 9.7)
    );
    CordeASauterZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), 7.3, 135, -9.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), 16.7, 138.201, 10.7)
    );
    CordeASauterZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), 8.3, 135, -10.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), 15.7, 138.201, 11.7)
    );


    // [ Ascenseur 2 ] //
    CordeASauterZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -110.3, 135, 3.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -114.7, 138.201, -2.7)
    );
    CordeASauterZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -114.3, 135, 4.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -129.7, 138.201, -3.7)
    );
    CordeASauterZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -115.3, 135, 5.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -129.7, 138.201, -4.7)
    );
    CordeASauterZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -116.3, 135, 8.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -129.7, 138.201, -7.7)
    );
    CordeASauterZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -117.3, 135, 9.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -128.7, 138.201, -8.7)
    );
    CordeASauterZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -118.3, 135, 10.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -127.7, 138.201, -9.7)
    );
    CordeASauterZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -119.3, 135, 11.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -126.7, 138.201, -10.7)
    );


    gameZones.put("CordeASauter", CordeASauterZone);

    Zone zoneFtB = new Zone();
    // [ Aire de jeu ] //
    zoneFtB.addSubZone(
        new Location(Bukkit.getWorld("world"), 144.3, -35, 265.3),
        new Location(Bukkit.getWorld("world"), 223.7, 43.21, 504.7)
    );
    zoneFtB.addSubZone(
        new Location(Bukkit.getWorld("world"), 144.7, -12.79, 401.3),
        new Location(Bukkit.getWorld("world"), 138.3, -19, 408.7)
    );
    zoneFtB.addSubZone(
        new Location(Bukkit.getWorld("world"), 132.3, -15.79, 405.3),
        new Location(Bukkit.getWorld("world"), 138.7, -19, 407.7)
    );
    zoneFtB.addSubZone(
        new Location(Bukkit.getWorld("world"), 132.3, -19, 424.7),
        new Location(Bukkit.getWorld("world"), 136.7, -15.79, 405.3)
    );
    zoneFtB.addSubZone(
        new Location(Bukkit.getWorld("world"), 136.7, -19, 421.3),
        new Location(Bukkit.getWorld("world"), 127.3, -16.79, 424.7)
    );
    zoneFtB.addSubZone(
        new Location(Bukkit.getWorld("world"), 127.7, -19, 418.3),
        new Location(Bukkit.getWorld("world"), 120.3, -15.79, 427.7)
    );
    zoneFtB.addSubZone(
        new Location(Bukkit.getWorld("world"), 125.7, -19, 429.7),
        new Location(Bukkit.getWorld("world"), 122.3, -15.79, 416.3)
    );
    zoneFtB.addSubZone(
        new Location(Bukkit.getWorld("world"), 120.3, -19, 418.3),
        new Location(Bukkit.getWorld("world"), 127.7, -15.79, 427.7)
    );zoneFtB.addSubZone(
        new Location(Bukkit.getWorld("world"), 121.3, -19, 417.3),
        new Location(Bukkit.getWorld("world"), 126.7, -15.79, 428.7)
    );
    gameZones.put("Find_the_Button", zoneFtB);

    Zone BaPZone = new Zone();
    // [ Aire de jeu ] //
    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 19.3, 65, 5.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 79.7, 76.201, -76.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 20.3, 70, 5.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 78.7, 79.201, -76.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 77.7, 81.201, -76.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 21.3, 70, 5.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 22.3, 82.201, 5.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 76.7, 70, -76.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 75.7, 83.201, -76.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 23.3, 70, 5.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 24.3, 84.201, 5.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 74.7, 70, -76.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 73.7, 85.201, -76.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 25.3, 70, 5.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 72.7, 86.201, -76.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 26.3, 70, 5.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 27.3, 87.201, 5.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 71.7, 70, -76.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 70.7, 88.201, -76.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 28.3, 70, 5.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 30.3, 89.201, 5.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 68.7, 70, -76.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 66.7, 90.201, -76.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 32.3, 70, 5.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 34.3, 91.201, 5.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 64.7, 70, -76.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 60.7, 92.201, -76.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 38.3, 70, 5.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 42.3, 93.201, 5.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 56.7, 70, -76.7)
    );

    // [ Ascenseur ] //
    BaPZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 19.7, 65, 3.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), -0.7, 67.201, -0.7)
    );
    BaPZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 16.7, 65, 6.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 1.3, 69.201, -3.7)
    );
    BaPZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), -0.7, 65, -1.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 1.7, 68.201, 4.7)
    );
    BaPZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), -0.3, 65, 5.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), -15.7, 68.201, -2.7)
    );
    BaPZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), -1.3, 65, 6.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), -15.7, 68.201, -3.7)
    );
    BaPZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), -2.3, 65, 9.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), -15.7, 68.201, -6.7)
    );
    BaPZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), -3.3, 65, 10.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), -14.7, 68.201, -7.7)
    );
    BaPZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), -4.3, 65, 11.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), -13.7, 68.201, -8.7)
    );
    BaPZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), -5.3, 65, 12.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), -12.7, 68.201, -9.7)
    );

    gameZones.put("BAP", BaPZone);

    Zone CroqueCarotteZone = new Zone();
    CroqueCarotteZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 22.3, -61, -116.3),
        new Location(Bukkit.getWorld("world"), 63.7, -43.79, -156.7)
    );
    CroqueCarotteZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 44.7, -60, -97.3),
        new Location(Bukkit.getWorld("world"), 40.3, -53.79, -116.3)
    );
    CroqueCarotteZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 67.7, -50, -165.7),
        new Location(Bukkit.getWorld("world"), 11.3, -33.79, -107.3)
    );
    CroqueCarotteZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 47.7, -60, -89.3),
        new Location(Bukkit.getWorld("world"), 37.3, -56.79, -94.7)
    );
    CroqueCarotteZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 36.3, -60, -93.7),
        new Location(Bukkit.getWorld("world"), 48.7, -56.79, -90.3)
    );
    CroqueCarotteZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 46.7, -60, -95.7),
        new Location(Bukkit.getWorld("world"), 38.3, -56.79, -88.3)
    );
    CroqueCarotteZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 44.7, -60, -94.3),
        new Location(Bukkit.getWorld("world"), 39.3, -57.79, -97.7)
    );
    gameZones.put("Croque_Carotte", CroqueCarotteZone);

    Zone biscuitTeamZone = new Zone();
    biscuitTeamZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -40, 60, -40),
        new Location(Bukkit.getWorld("world"), -20, 80, -20)
    );
    gameZones.put("Biscuit_Team", biscuitTeamZone);

    Zone CMZone = new Zone();
    CMZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), 26.7, 62, -69.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), -26.7, 76.201, -2.3)
    );

    // [ Ascenseur ] //
    CMZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), 5.2, 65, -1.8),
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), -5.2, 68.701, -2.7)
    );
    CMZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), 4.7, 65, -0.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), -4.7, 68.201, -2.7)
    );
    CMZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), 3.7, 65, 1.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), -3.7, 68.201, -0.7)
    );
    CMZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), 3.7, 65, -0.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), -3.7, 68.201, 1.7)
    );
    CMZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), 4.7, 65, 1.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), -4.7, 68.201, 16.7)
    );
    CMZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), 5.7, 65, 2.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), -5.7, 68.201, 16.7)
    );
    CMZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), 8.7, 65, 3.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), -8.7, 68.201, 16.7)
    );
    CMZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), 9.7, 65, 4.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), -9.7, 68.201, 15.7)
    );
    CMZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), 10.7, 65, 5.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), -10.7, 68.201, 14.7)
    );
    CMZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), 11.7, 65, 6.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), -11.7, 68.201, 13.7)
    );


    gameZones.put("ChaiseMusicale", CMZone);

    Zone SquidGameZone = new Zone();
    SquidGameZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -281.7, -60, -92.7),
        new Location(Bukkit.getWorld("world"), -228.3, -39.79, -8.3)
    );
    SquidGameZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -281.7, -150, -92.7),
        new Location(Bukkit.getWorld("world"), -228.3, -43.79, -8.3)
    );
    SquidGameZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -228.7, -60, -54.7),
        new Location(Bukkit.getWorld("world"), -220.3, -56.79, -46.3)
    );
    SquidGameZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -225.7, -60, -44.3),
        new Location(Bukkit.getWorld("world"), -221.3, -56.79, -55.7)
    );
    SquidGameZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -222.3, -60, -56.7),
        new Location(Bukkit.getWorld("world"), -225.7, -56.79, -44.3)
    );
    SquidGameZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -226.7, -60, -45.3),
        new Location(Bukkit.getWorld("world"), -221.3, -56.79, -55.7)
    );

    gameZones.put("Squid_Game", SquidGameZone);

    Zone SquidGameAerienZone = new Zone();
    // [ Aire de jeu ] //
    SquidGameAerienZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), 25.7, 65, 1.3),
            new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), -96.7, 160.201, 76.7)
    );

    SquidGameAerienZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), -94.7, 160.201, 75.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), 25.7, 100, 2.3)
    );
    SquidGameAerienZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), -93.7, 162.201, 74.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), 24.7, 100, 3.3)
    );
    SquidGameAerienZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), -92.7, 166.201, 73.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), 23.7, 100, 4.3)
    );
    SquidGameAerienZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), -92.7, 100, 72.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), 22.7, 172.201, 5.3)
    );
    SquidGameAerienZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), -91.7, 100, 71.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), 21.7, 174.201, 6.3)
    );
    SquidGameAerienZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), -90.7, 100, 70.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), 20.7, 178.201, 7.3)
    );
    SquidGameAerienZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), -89.7, 100, 69.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), 19.7, 181.201, 8.3)
    );

    // [ Ascenseur ] //
    SquidGameAerienZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), -2.7, 65, 2.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), 3.7, 68.201, -3.7)
    );
    SquidGameAerienZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), -3.7, 65, -3.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), 4.7, 68.201, -18.7)
    );
    SquidGameAerienZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), -4.7, 65, -4.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), 5.7, 68.201, -18.7)
    );
    SquidGameAerienZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), -7.7, 65, -5.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), 8.7, 68.201, -18.7)
    );
    SquidGameAerienZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), -8.7, 65, -6.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), 9.7, 68.201, -17.7)
    );
    SquidGameAerienZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), -9.7, 65, -7.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), 10.7, 68.201, -16.7)
    );
    SquidGameAerienZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), -10.7, 65, -8.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), 11.7, 68.201, -15.7)
    );


    gameZones.put("SquidGameAerien", SquidGameAerienZone);

    Zone SalleBlancheZone = new Zone();
    SalleBlancheZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -221.7, -60, -41.7),
        new Location(Bukkit.getWorld("world"), -181.3, -50.79, -15.3)
    );
    SalleBlancheZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -146.3, -60, 216.3),
        new Location(Bukkit.getWorld("world"), -154.7, -57.79, 226.7)
    );

    gameZones.put("Salle_Blanche", SalleBlancheZone);
    Zone SalleGriseZone = new Zone();
    SalleGriseZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -1.3, 65, 36.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -55.7, 73.201, -35.7)
    );
    SalleGriseZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -2.3, 65, -42.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -54.7, 68.201, 43.7)
    );
    SalleGriseZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -50, 65, -34.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -62.7, 68.201, 35.7)
    );

    // [ Ascenseur ] //
    SalleGriseZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -1.7, 65, -1.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), 2.7, 68.201, 2.7)
    );
    SalleGriseZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), 2.3, 65, -3.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), 17.7, 68.201, 4.7)
    );
    SalleGriseZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), 3.3, 65, -4.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), 17.7, 68.201, 5.7)
    );
    SalleGriseZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), 4.3, 65, -7.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), 17.7, 68.201, 8.7)
    );
    SalleGriseZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), 5.3, 65, -8.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), 16.7, 68.201, 9.7)
    );
    SalleGriseZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), 6.3, 65, -9.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), 15.7, 68.201, 10.7)
    );
    SalleGriseZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), 7.3, 65, -10.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), 14.7, 68.201, 11.7)
    );

    gameZones.put("SalleGrise", SalleGriseZone);
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    handlePlayerMovement(event.getPlayer(), event.getFrom(), event.getTo(), event);
  }
  @EventHandler
  public void onPlayerTeleport(PlayerTeleportEvent event) {
    handlePlayerMovement(event.getPlayer(), event.getFrom(), event.getTo(), event);
  }

  private void handlePlayerMovement(Player player, Location fromLocation, Location toLocation, Cancellable event) {
    if (toLocation == null) return;

    String currentEpreuve = EpreuveCommand.getEpreuve();
    Zone zone = gameZones.get(currentEpreuve);

    if (zone == null) return;


//    Bukkit.getLogger().info("toLocation : " + toLocation);
//    Bukkit.getLogger().info("zone : " + zone.subZones);

    boolean isInsideZone = zone.isInside(toLocation);
//    Bukkit.getLogger().info("isInsideZone : " + isInsideZone);
    if (!isInsideZone) {
      boolean wasInsideZone = zone.isInside(fromLocation);
//      Bukkit.getLogger().info("wasInsideZone : " + wasInsideZone);
      // Gestion des spectateurs
      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        if (onlinePlayer.getGameMode() == GameMode.SPECTATOR
            && onlinePlayer.getSpectatorTarget() == player) {
          var team = onlinePlayer.getScoreboard().getEntryTeam(onlinePlayer.getName());
          if (team != null && team.getName().equalsIgnoreCase("mort")) {
            onlinePlayer.setSpectatorTarget(null);
            onlinePlayer.sendMessage("§cVous ne pouvez plus observer ce joueur car il a quitté la zone autorisée.");
          }
        }
      }

      if (!player.getScoreboardTags().contains("joueur")) return;

      if (wasInsideZone) {
        event.setCancelled(true);
        player.sendActionBar("§cVous ne pouvez pas quitter la zone !");
      } else {
        Location zoneCenter = zone.getCenter();
        if (zoneCenter != null) {
//          Bukkit.getLogger().info("worlds/SquidGame/" + EpreuveCommand.getEpreuve());
//          Bukkit.getLogger().info("zoneCenter.getWorld().getName() : " + zoneCenter.getWorld().getName());
//          Bukkit.getLogger().info("Le joueur est hors de la zone, téléportation au centre : " + zoneCenter);
          player.teleport(zoneCenter);
          player.sendActionBar("§cVous avez été téléporté au centre de la zone !");
        }
      }
    }
  }

  public class Zone {
    private final List<SubZone> subZones = new ArrayList<>();

    // Ajoute une sous-zone (pavé droit)
    public void addSubZone(Location corner1, Location corner2) {
      subZones.add(new SubZone(corner1, corner2));
    }

    // Vérifie si une position est dans l'une des sous-zones
    public boolean isInside(Location location) {
      if (location == null || location.getWorld() == null) {
        Bukkit.getLogger().warning("Location ou monde null dans isInside.");
        return false;
      }

      // Vérifie si la position est dans l'une des sous-zones du lobby
      Zone lobbyZone = gameZones.get("Lobby");
      if (lobbyZone != null) {
        for (SubZone subZone : lobbyZone.subZones) {
          if (subZone.isInside(location)) {
            return true;
          }
        }
      }

      // Vérifie si la position est dans l'une des sous-zones de la salle grise
      Zone SalleGriseZone = gameZones.get("SalleGrise");
      if (SalleGriseZone != null) {
        for (SubZone subZone : SalleGriseZone.subZones) {
          if (subZone.isInside(location)) {
            return true;
          }
        }
      }

      Zone carrouselZone = gameZones.get("Carrousel");
      if (carrouselZone != null) {
        for (SubZone subZone : carrouselZone.subZones) {
          if (subZone.isInside(location)) {
            return true;
          }
        }
        if (isInCylinder(location, centerCarrousel, 34.7, centerCarrousel.getY(), 80)) return true;
      }

      Zone cordeASauterZone = gameZones.get("CordeASauter");
      if (cordeASauterZone != null) {
        for (SubZone subZone : cordeASauterZone.subZones) {
          if (subZone.isInside(location)) {
            return true;
          }
        }
        if (isInCylinder(location, centerCordeASauter, 50.7, centerCordeASauter.getY(), 134)) return true;
      }

//      String worldName = location.getWorld().getName();
//      Bukkit.getLogger().info("Vérification de la zone pour le monde : " + worldName);

      // Vérifie si le monde est valide et correspond à l'épreuve actuelle
      if ("Lobby".equalsIgnoreCase(EpreuveCommand.getEpreuve())) {
        if (location.getWorld() == null || !location.getWorld().getName().equals("world")) {
          return false;
        }
      } else {
        if (location.getWorld() == null || !location.getWorld().getName().equals("worlds/SquidGame/" + EpreuveCommand.getEpreuve())) {
          return false;
        }
      }


      // Vérifie si la position est dans l'une des sous-zones de cette zone
      for (SubZone subZone : subZones) {
        if (subZone.isInside(location)) {
          return true;
        }
      }

//      Bukkit.getLogger().info("Le joueur n'est dans aucune sous-zone.");
      return false;
    }

    // Retourne le centre de la première sous-zone (par défaut)
    public Location getCenter() {
      if (subZones.isEmpty()) return null;
      return subZones.get(0).getCenter();
    }

    // Classe interne pour représenter une sous-zone
    private static class SubZone {
      private final Location corner1;
      private final Location corner2;

      public SubZone(Location corner1, Location corner2) {
        this.corner1 = corner1;
        this.corner2 = corner2;
      }
      public Location getCorner1() {
        return corner1;
      }

      public Location getCorner2() {
        return corner2;
      }

      public boolean isInside(Location location) {
        if (location == null || location.getWorld() == null) return false;
        if (!location.getWorld().equals(corner1.getWorld())) return false;

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return x >= Math.min(corner1.getX(), corner2.getX()) && x <= Math.max(corner1.getX(), corner2.getX())
            && y >= Math.min(corner1.getY(), corner2.getY()) && y <= Math.max(corner1.getY(), corner2.getY())
            && z >= Math.min(corner1.getZ(), corner2.getZ()) && z <= Math.max(corner1.getZ(), corner2.getZ());
      }

      public Location getCenter() {

        System.out.println(corner1.getWorld());

        double centerX = (corner1.getX() + corner2.getX()) / 2;
        double centerY = (corner1.getY() + corner2.getY()) / 2;
        double centerZ = (corner1.getZ() + corner2.getZ()) / 2;

        return new Location(
            "Lobby".equalsIgnoreCase(EpreuveCommand.getEpreuve())
                ? Bukkit.getWorld("world")
                : Bukkit.getWorld("worlds/SquidGame/" + EpreuveCommand.getEpreuve()),
            centerX,
            centerY,
            centerZ
        );
      }
    }
  }

  public boolean isInCylinder(Location loc, Location center, double radius, double minY, double maxY) {

    if (loc.getWorld() == null || center.getWorld() == null) return false;
    if (!loc.getWorld().equals(center.getWorld())) return false;

    if (loc.getY() < minY || loc.getY() > maxY) return false;

    double dx = loc.getX() - center.getX();
    double dz = loc.getZ() - center.getZ();
    double distanceSquared = dx * dx + dz * dz;

    return distanceSquared <= radius * radius;
  }

}