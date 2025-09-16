package fr.salut.squidgame.component.ListenerManager.GameZone;

import fr.salut.squidgame.component.ListenerManager.BlockDetector;
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

  private final Map<String, Zone> gameZones = new HashMap<>();

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
        new Location(Bukkit.getWorld("world"), -28.3, -59, -18.3),
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
        new Location(Bukkit.getWorld("world"), 279.7, 12.2, -36.7)
    );
    gameZones.put("Escaliers", StairsZone);

    Zone zone123Soleil = new Zone();
    zone123Soleil.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), 25.7, 1, 17.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), -23.7, 21, 203.7)
    );
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
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), 6, 1, -9.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), -4.7, 4.21, -1.7)
    );
    zone123Soleil.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), -5.7, 1, 2.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), 6.7, 4.21, -0.7)
    );
    zone123Soleil.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), 5.7, 1, -1.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/123Soleil"), -4.7, 4.2, 3.7)
    );

    gameZones.put("123Soleil", zone123Soleil);

    Zone zoneArcenCiel = new Zone();

    // [ Ascenseur ] //
    zoneArcenCiel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), -4.7, 65, -10.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), 5.7, 68, 3.7)
    );

    // [ Ascenseur ] //
    zoneArcenCiel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), 3.7, 65, 7.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), -2.7, 68, 3.7)
    );
    zoneArcenCiel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), 54.7, 65, 7.3),
            new Location(Bukkit.getWorld("worlds/SquidGame/ArcEnCiel"), -53.7, 86, 89.7)
    );

    gameZones.put("ArcEnCiel", zoneArcenCiel);

    Zone zoneCarrousel = new Zone();

    // [ Ascenseur ] //
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -2.7, 65, 9.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 3.7, 69, -13.7)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -17.9, 67, 2.2),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -15.3, 65, 12.7)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -21.3, 65, 16.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -23.9, 67, 6.2)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -34.9, 67, 17.2),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -24.3, 65, 19.8)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -38.9, 67, 23.2),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -28.3, 65, 25.8)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -41.9, 67, 32.2),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -31.3, 65, 34.8)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -42.9, 67, 40.2),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -32.3, 65, 42.8)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -41.9, 67, 48.2),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -31.3, 65, 50.8)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -38.9, 67, 57.2),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -28.3, 65, 59.8)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -34.9, 67, 63.2),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -24.3, 65, 65.8)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -23.9, 67, 76.9),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -21.2, 65, 66.3)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -17.9, 67, 80.9),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -15.2, 65, 70.3)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -8.9, 67, 83.9),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -6.2, 65, 73.3)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), -0.9, 67, 84.9),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 1.8, 65, 74.3)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 7.3, 67, 83.9),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 9.8, 65, 73.3)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 16.3, 67, 80.9),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 18.8, 65, 70.3)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 22.3, 67, 76.9),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 24.8, 65, 66.3)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 22.3, 67, 76.9),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 24.8, 65, 66.3)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 25.3, 65, 63.2),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 35.8, 67, 65.7)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 29.3, 65, 57.2),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 39.8, 67, 59.7)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 32.3, 65, 48.2),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 42.8, 67, 50.7)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 33.3, 65, 40.2),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 43.8, 67, 42.7)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 32.3, 65, 32.2),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 42.8, 67, 34.7)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 29.3, 65, 23.2),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 39.8, 67, 25.7)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 25.3, 65, 17.2),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 35.8, 67, 19.7)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 24.8, 65, 16.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 22.3, 67, 6.2)
    );

    zoneCarrousel.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 18.9, 67, 2.2),
            new Location(Bukkit.getWorld("worlds/SquidGame/Carrousel"), 16.3, 65, 12.7)
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

    zoneLTTE.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), -0.7, 65, 107.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 87.7, 104.21, 2.3)
    );

    // [ Ascenseur ] //
    zoneLTTE.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), -11.7, 65, -1.3),
            new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), -3.3, 69, -15.3)
    );

    // [ Ascenseur ] //
    zoneLTTE.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), -11.7, 65, -2.3),
            new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), -4.3, 69, 3.7)
    );

    zoneLTTE.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), -4.7, 66, 8.3),
            new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 6.7, 72.21, 32.7)
    );

    zoneLTTE.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 8.3, 69.21, -16.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 69.7, 66, 2.3)
    );

    zoneLTTE.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 26.3, 72, 2.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 37.7, 80.21, -3.3)
    );

    zoneLTTE.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), -11.7, 69, 2.3),
            new Location(Bukkit.getWorld("worlds/SquidGame/LTTE"), 0.7, 65, 6.7)
    );

    gameZones.put("LTTE", zoneLTTE);

    Zone zoneP4 = new Zone();
    zoneP4.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), -0.3, 65, -39.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), -56.7, 84.21, 40.7)
    );

    // [ Ascenseur ] //
    zoneP4.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), -1.7, 65, -1.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), 4.7, 69, 2.7)
    );

    // [ Ascenseur ] //
    zoneP4.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"),3.3, 65, -3.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/Puissance4"), 18.7, 68, 4.7)
    );


    gameZones.put("Puissance4", zoneP4);

    Zone zoneBilles = new Zone();
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

    // [ Ascenseur ] //
    CordeASauterZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), 2.3, 135, 3.8),
            new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), 17.7, 139, -3.7)
    );

    // [ Ascenseur ] //
    CordeASauterZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), 5.7, 135, 2.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -0.7, 138, -1.7)
    );

    CordeASauterZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -0.3, 172, 100.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -110.7, 127, -99.7)
    );

    // [ Ascenseur 2 ] //
    CordeASauterZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -129.7, 138, -3.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -115.3, 135, 4.7)
    );

    // [ Ascenseur 2 ] //
    CordeASauterZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -116.7, 138, -1.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/CordeASauter"), -109.3, 135, 2.7)
    );

    gameZones.put("CordeASauter", CordeASauterZone);

    Zone zoneFtB = new Zone();
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
    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 19.3, 65.79, 5.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 79.7, 76, -76.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 20.3, 77, 5.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 78.7, 75.79, -76.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 77.7, 80, -76.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 21.3, 74.79, 5.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 22.3, 82, 5.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 76.7, 74.79, -76.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 75.7, 83, -76.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 23.3, 74.79, 5.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 24.3, 84, 5.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 74.7, 74.79, -76.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 73.7, 85, -76.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 25.3, 74.79, 5.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 72.7, 86, -76.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 26.3, 74.79, 5.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 27.3, 87, 5.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 71.7, 74.79, -76.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 70.7, 88, -76.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 28.3, 74.79, 5.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 30.3, 89, 5.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 68.7, 74.79, -76.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 66.7, 90, -76.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 32.3, 74.79, 5.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 34.3, 91, 5.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 64.7, 74.79, -76.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 60.7, 92, -76.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 38.3, 74.79, 5.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 42.3, 93, 5.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 56.7, 74.79, -76.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 20.7, 65, 3.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 15.3, 68, -0.7)
    );

    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 16.7, 70, 6.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 1.3, 65, -3.7)
    );

    // [ Ascenseur ] //
    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), 2.7, 65, 4.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), -1.7, 69, -1.7)
    );

    // [ Ascenseur ] //
    BaPZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), -0.3, 65, 5.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/BAP"), -15.7, 68, -2.7)
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
            new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), -24.7, 76.21, -2.3)
    );

    // [ Ascenseur ] //
    CMZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), 11.7, 65, 16.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/ChaiseMusicale"), -11.7, 69.201, -2.7)
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
    SquidGameAerienZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), 25.7, 65, 1.3),
            new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), -95.7, 160, 76.7)
    );

    // [ Ascenseur ] //
    SquidGameAerienZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), -2.7, 65, 3.7),
            new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), 3.7, 69, -4.7)
    );

    // [ Ascenseur ] //
    SquidGameAerienZone.addSubZone(
            new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), -3.7, 65, -3.3),
            new Location(Bukkit.getWorld("worlds/SquidGame/SquidGameAerien"), 4.7, 68, -18.7)
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
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -154.3, -60, 185.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -208.7, -51.79, 257.7)
    );
    SalleGriseZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -155.3, -60, 178.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -207.7, -56.79, 185.7)
    );
    SalleGriseZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -215.7, -60, 186.3),
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -208.3, -56.79, 256.7)
    );
    SalleGriseZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -207.7, -60, 264.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -155.3, -56.79, 257.3)
    );
    SalleGriseZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -145.3, -60, 225.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -154.7, -56.79, 217.3)
    );
    SalleGriseZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -146.3, -60, 226.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -151.7, -56.79, 216.3)
    );
    SalleGriseZone.addSubZone(
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -147.3, -60, 227.7),
        new Location(Bukkit.getWorld("worlds/SquidGame/SalleGrise"), -150.7, -56.79, 215.3)
    );

    gameZones.put("SalleGrise", SalleGriseZone);
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    handlePlayerMovement(event.getPlayer(), event.getFrom(), event.getTo(), event);
  }
//  @EventHandler
//  public void onPlayerTeleport(PlayerTeleportEvent event) {
//    handlePlayerMovement(event.getPlayer(), event.getFrom(), event.getTo(), event);
//  }

  private void handlePlayerMovement(Player player, Location fromLocation, Location toLocation, Cancellable event) {
    if (toLocation == null) return;

    String currentEpreuve = BlockDetector.getEpreuve();
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
//          Bukkit.getLogger().info("worlds/SquidGame/" + BlockDetector.getEpreuve());
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

      Zone carrouselZone = gameZones.get("Carrousel");
      if (carrouselZone != null) {
        for (SubZone subZone : carrouselZone.subZones) {
          if (subZone.isInside(location)) {
            return true;
          }
        }
        if (isInCylinder(location, centerCarrousel, 34, centerCarrousel.getY(), 80)) return true;
      }

      Zone cordeASauterZone = gameZones.get("CordeASauter");
      if (cordeASauterZone != null) {
        for (SubZone subZone : cordeASauterZone.subZones) {
          if (subZone.isInside(location)) {
            return true;
          }
        }
        if (isInCylinder(location, centerCordeASauter, 50, centerCordeASauter.getY(), 134)) return true;
      }

//      String worldName = location.getWorld().getName();
//      Bukkit.getLogger().info("Vérification de la zone pour le monde : " + worldName);

      // Vérifie si le monde est valide et correspond à l'épreuve actuelle
      if (location.getWorld() == null || !location.getWorld().getName().equals("worlds/SquidGame/" + BlockDetector.getEpreuve())) {
        return false;
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

        return new Location(Bukkit.getWorld("worlds/SquidGame/" + BlockDetector.getEpreuve()), centerX, centerY, centerZ);
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