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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class GameZoneManager implements Listener {

  private final Map<String, Zone> gameZones = new HashMap<>();

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
        new Location(Bukkit.getWorld("world"), -53.3, -60, 70.3),
        new Location(Bukkit.getWorld("world"), -102.7, -40, 256.7)
    );
    zone123Soleil.addSubZone(
        new Location(Bukkit.getWorld("world"), -57.3, -60, 74),
        new Location(Bukkit.getWorld("world"), -99.7, -56.79, 59.3)
    );
    zone123Soleil.addSubZone(
        new Location(Bukkit.getWorld("world"), -82.7, -60, 59.7),
        new Location(Bukkit.getWorld("world"), -74.3, -56.79, 50.3)
    );
    zone123Soleil.addSubZone(
        new Location(Bukkit.getWorld("world"), -73,3, -60, -56,7),
        new Location(Bukkit.getWorld("world"), -83.7, -56.79, 51.3)
    );
    zone123Soleil.addSubZone(
        new Location(Bukkit.getWorld("world"), -84.7, -60, 55.7),
        new Location(Bukkit.getWorld("world"), -72.3, -56.79, 52.3)
    );
    zone123Soleil.addSubZone(
        new Location(Bukkit.getWorld("world"), -73.3, -60, 51.3),
        new Location(Bukkit.getWorld("world"), -83.7, -56.8, 56.7)
    );
    gameZones.put("123Soleil", zone123Soleil);

    Zone zoneArcenCiel = new Zone();
    zoneArcenCiel.addSubZone(
        new Location(Bukkit.getWorld("world"), -171.3, -60, -95.3),
        new Location(Bukkit.getWorld("world"), -279.7, -49.79, -178.7)
    );
    zoneArcenCiel.addSubZone(
        new Location(Bukkit.getWorld("world"), -220.3, -60, -185.7),
        new Location(Bukkit.getWorld("world"), -231.7, -56.79, -180.3)
    );
    zoneArcenCiel.addSubZone(
        new Location(Bukkit.getWorld("world"), -232.7, -60, -181.3),
        new Location(Bukkit.getWorld("world"), -219.3, -56.79, -184.7)
    );
    zoneArcenCiel.addSubZone(
        new Location(Bukkit.getWorld("world"), -211.3, -60, -186.7),
        new Location(Bukkit.getWorld("world"), -230.7, -56.79, -178.3)
    );

    gameZones.put("Arc_en_Ciel", zoneArcenCiel);

    Zone zoneCarrousel = new Zone();
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -121.3, -59, -21.7),
        new Location(Bukkit.getWorld("world"), -131.7, -45.79, 46.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -136.7, -59, 45.7),
        new Location(Bukkit.getWorld("world"), -116.3, -45.79, -20.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -113.3, -59, -19.7),
        new Location(Bukkit.getWorld("world"), -139.7, -45.79, 44.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -141.77, -59, 43.7),
        new Location(Bukkit.getWorld("world"), -111.3, -45.79, -18.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -109.3, -59, -17.7),
        new Location(Bukkit.getWorld("world"), -143.7, -45.79, 42.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -144.7, -59, 41.7),
        new Location(Bukkit.getWorld("world"), -108.3, -45.79, -16.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -106.3, -59, -15.7),
        new Location(Bukkit.getWorld("world"), -146.7, -45.79, 40.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -147.7, -59, 39.7),
        new Location(Bukkit.getWorld("world"), -105.3, -45.79, -14.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -103.3, -59, -13.7),
        new Location(Bukkit.getWorld("world"), -149.7, -45.79, 38.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -150.7, -59, 37.7),
        new Location(Bukkit.getWorld("world"), -102.3, -45.79, -12.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -101.3, -59, -11.7),
        new Location(Bukkit.getWorld("world"), -151.7, -45.79, 36.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -152.7, -59, 35.7),
        new Location(Bukkit.getWorld("world"), -100.3, -45.79, -10.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -99.3, -59, -8.7),
        new Location(Bukkit.getWorld("world"), -153.7, -45.79, 33.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -154.7, -59, 32.7),
        new Location(Bukkit.getWorld("world"), -98.3, -45.79, -7.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -97.3, -59, -5.7),
        new Location(Bukkit.getWorld("world"), -155.7, -45.79, 30.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -156.7, -59, 29.7),
        new Location(Bukkit.getWorld("world"), -96.3, -45.79, -4.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -95.3, -59, -2.7),
        new Location(Bukkit.getWorld("world"), -157.7, -45.79, 27.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -158.7, -59, 25.7),
        new Location(Bukkit.getWorld("world"), -94.3, -45.79, -0.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -93.3, -59, 2.3),
        new Location(Bukkit.getWorld("world"), -159.7, -45.79, 22.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -160.7, -59, 17.7),
        new Location(Bukkit.getWorld("world"), -92.3, -45.79, 7.3)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -130.7, -59, -35.7),
        new Location(Bukkit.getWorld("world"), -122.3, -55.79, -28.3)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -131.7, -59, -34.7),
        new Location(Bukkit.getWorld("world"), -121.3, -55.79, -29.3)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -132.7, -59, -33.7),
        new Location(Bukkit.getWorld("world"), -120.3, -55.79, -30.3)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -124.3, -59, -28.7),
        new Location(Bukkit.getWorld("world"), -128.7, -56.79, -21.3)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -110.7, -59, -23.7),
        new Location(Bukkit.getWorld("world"), -108.3, -57.79, -16.3)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -104.7, -59, -19.7),
        new Location(Bukkit.getWorld("world"), -102.3, -57.79, -12.3)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -94.3, -59, -9.3),
        new Location(Bukkit.getWorld("world"), -101.3, -57.79, -11.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -90.3, -59, -3.3),
        new Location(Bukkit.getWorld("world"), -97.3, -57.79, -5.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -87.3, -59, 5.7),
        new Location(Bukkit.getWorld("world"), -93.3, -57.79, 2.3)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -86.3, -59, 13.7),
        new Location(Bukkit.getWorld("world"), -92.3, -57.79, 11.3)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -87.3, -59, 21.7),
        new Location(Bukkit.getWorld("world"), -93.3, -57.79, 19.3)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -90.3, -59, 28.3),
        new Location(Bukkit.getWorld("world"), -97.7, -57.79, 30.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -94.3, -59, 34.3),
        new Location(Bukkit.getWorld("world"), -101.3, -57.79, 36.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -104.7, -59, 44.7),
        new Location(Bukkit.getWorld("world"), -102.3, -57.79, 37.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -110.7, -59, 48.7),
        new Location(Bukkit.getWorld("world"), -108.3, -57.79, 41.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -117.3, -59, 51.7),
        new Location(Bukkit.getWorld("world"), -119.7, -57.79, 45.3)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -125.3, -59, 52.7),
        new Location(Bukkit.getWorld("world"), -127.7, -57.79, 46.3)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -133.3, -59, 51.7),
        new Location(Bukkit.getWorld("world"), -135.7, -57.79, 45.3)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -142.3, -59, 48.7),
        new Location(Bukkit.getWorld("world"), -144.7, -57.79, 41.3)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -148.3, -59, 44.7),
        new Location(Bukkit.getWorld("world"), -150.7, -57.79, 37.3)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -158.7, -59, 34.3),
        new Location(Bukkit.getWorld("world"), -151.3, -57.79, 36.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -162.7, -59, 28.3),
        new Location(Bukkit.getWorld("world"), -155.3, -57.79, 30.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -165.7, -59, 19.3),
        new Location(Bukkit.getWorld("world"), -159.3, -57.79, 21.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -166.7, -59, 13.7),
        new Location(Bukkit.getWorld("world"), -160.3, -57.79, 11.3)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -166.7, -59, 5.7),
        new Location(Bukkit.getWorld("world"), -159.3, -57.79, 3.3)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -163.7, -59, -3.3),
        new Location(Bukkit.getWorld("world"), -155.3, -57.79, -5.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -158.7, -59, -9.3),
        new Location(Bukkit.getWorld("world"), -151.3, -57.79, -11.7)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -148.3, -59, -19.7),
        new Location(Bukkit.getWorld("world"), -150.7, -57.79, -12.3)
    );
    zoneCarrousel.addSubZone(
        new Location(Bukkit.getWorld("world"), -142.3, -59, -23.7),
        new Location(Bukkit.getWorld("world"), -144.7, -57.79, -16.3)
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
        new Location(Bukkit.getWorld("world"), -15.7, -59, -201.3),
        new Location(Bukkit.getWorld("world"), 72.7, -20.79, -306.7)
    );
    zoneLTTE.addSubZone(
        new Location(Bukkit.getWorld("world"), -10.3, -59, -306.3),
        new Location(Bukkit.getWorld("world"), -18.7, -55.79, -314.7)
    );
    zoneLTTE.addSubZone(
        new Location(Bukkit.getWorld("world"), -19.7, -59, -308.3),
        new Location(Bukkit.getWorld("world"), -9.3, -55.79, -313.7)
    );
    zoneLTTE.addSubZone(
        new Location(Bukkit.getWorld("world"), -8.3, -59, -309.3),
        new Location(Bukkit.getWorld("world"), -20.7, -55.79, -312.7)
    );
    zoneLTTE.addSubZone(
        new Location(Bukkit.getWorld("world"), -19.7, -59, -300.7),
        new Location(Bukkit.getWorld("world"), -8.3, -51.79, -274.3)
    );
    zoneLTTE.addSubZone(
        new Location(Bukkit.getWorld("world"), -6.7, -55.79, -325.7),
        new Location(Bukkit.getWorld("world"), 54.7, -59, -306.3)
    );
    zoneLTTE.addSubZone(
        new Location(Bukkit.getWorld("world"), 11.3, -53, -306.3),
        new Location(Bukkit.getWorld("world"), 22.7, -44.79, -311.7)
    );

    gameZones.put("Loup_Touche_Touche_Explosif", zoneLTTE);

    Zone zoneP4 = new Zone();
    zoneP4.addSubZone(
        new Location(Bukkit.getWorld("world"), -164.3, -60, 64.3),
        new Location(Bukkit.getWorld("world"), -220.7, -40.79, 144.7)
    );
    zoneP4.addSubZone(
        new Location(Bukkit.getWorld("world"), -155.3, -60, 100.3),
        new Location(Bukkit.getWorld("world"), -164.7, -56.79, 108.7)
    );
    zoneP4.addSubZone(
        new Location(Bukkit.getWorld("world"), -156.3, -60, 99.3),
        new Location(Bukkit.getWorld("world"), -161.7, -56.79, 109.7)
    );
    zoneP4.addSubZone(
        new Location(Bukkit.getWorld("world"), -160.7, -60, 110.7),
        new Location(Bukkit.getWorld("world"), -157.3, -56.79, 98.3)
    );
    zoneP4.addSubZone(
        new Location(Bukkit.getWorld("world"), -160.7, -60, 110.7),
        new Location(Bukkit.getWorld("world"), -157.3, -56.79, 98.3)
    );
    zoneP4.addSubZone(
        new Location(Bukkit.getWorld("world"), -155.3, -60, 100.3),
        new Location(Bukkit.getWorld("world"), -164.7, -56.79, 108.7)
    );
    gameZones.put("Puissance_4", zoneP4);

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
        new Location(Bukkit.getWorld("world"), 153.3, -59, -164.3),
        new Location(Bukkit.getWorld("world"), 213.7, -48.79, -246.7)
    );
    BaPZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 128.3, -59, -164.3),
        new Location(Bukkit.getWorld("world"), 155.7, -55.79, -173.7)
    );
    BaPZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 129.3, -59, -163.3),
        new Location(Bukkit.getWorld("world"), 134.7, -55.79, -174.7)
    );
    BaPZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 133.7, -59, -175.7),
        new Location(Bukkit.getWorld("world"), 130.3, -55.79, -162.3)
    );
    BaPZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 156.3, -50, -245.7),
        new Location(Bukkit.getWorld("world"), 210.7, -42.79, -165.3)
    );
    BaPZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 212.7, -50, -164.3),
        new Location(Bukkit.getWorld("world"), 154.3, -44.79, -246.7)
    );
    gameZones.put("Balle_aux_Prisonniers", BaPZone);

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
        new Location(Bukkit.getWorld("world"), 165.7, -56, 4.3),
        new Location(Bukkit.getWorld("world"), 114.3, -41.79, 71.7)
    );
    CMZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 144.7, -53, 71.3),
        new Location(Bukkit.getWorld("world"), 135.3, -49.79, 80.7)
    );
    CMZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 134.3, -53, 79.7),
        new Location(Bukkit.getWorld("world"), 145.7, -49.79, 74.3)
    );
    CMZone.addSubZone(
        new Location(Bukkit.getWorld("world"), 146.7, -53, 75.3),
        new Location(Bukkit.getWorld("world"), 133.3, -49.79, 78.7)
    );

    gameZones.put("Chaises_Musicales", CMZone);

    Zone SquidGameZone = new Zone();
    SquidGameZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -281.7, -43.79, -8.3),
        new Location(Bukkit.getWorld("world"), -228.3, -500, -92.7)
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
        new Location(Bukkit.getWorld("world"), -215.7, -60, 264),
        new Location(Bukkit.getWorld("world"), -154.3, -51.79, 177.3)
    );
    SalleGriseZone.addSubZone(
        new Location(Bukkit.getWorld("world"), -202.3, -60, -7.3),
        new Location(Bukkit.getWorld("world"), -211.7, -56.79, -15.7)
    );
    gameZones.put("Salle_Grise", SalleGriseZone);
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

    String currentEpreuve = BlockDetector.getEpreuve(); // Récupère l'épreuve actuelle
    Zone zone = gameZones.get(currentEpreuve);

    if (zone == null) return; // Pas de zone définie pour cette épreuve

    boolean isOutsideZone = !zone.isInside(toLocation);

    // Vérifie si un spectateur observe ce joueur
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      if (onlinePlayer.getGameMode() == GameMode.SPECTATOR && onlinePlayer.getSpectatorTarget() == player && isOutsideZone) {
        onlinePlayer.setSpectatorTarget(null);
        onlinePlayer.sendActionBar("§cVous suivez un joueur qui ne peut pas quitter la zone !");
      }
    }

    // Vérifie si le joueur a le tag "joueur"
    if (!player.getScoreboardTags().contains("joueur")) return;

    if (isOutsideZone) {
      if (!zone.isInside(fromLocation)) {
        // Si le joueur est déjà en dehors de la zone, on le téléporte au centre
        Location center = zone.getCenter();
        if (center != null) {
          Bukkit.getLogger().info("Le joueur est en dehors de la zone, téléportation au centre : " + center);
          player.teleport(center);
          player.sendActionBar("§cVous avez été téléporté au centre de la zone de jeu !");
        }
      } else {
        // Sinon, on bloque simplement le mouvement
        event.setCancelled(true);
        player.sendActionBar("§cVous ne pouvez pas quitter la zone de l'épreuve !");
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
      for (SubZone subZone : subZones) {
        if (subZone.isInside(location)) {
          return true;
        }
      }
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
        double centerX = (corner1.getX() + corner2.getX()) / 2;
        double centerY = (corner1.getY() + corner2.getY()) / 2;
        double centerZ = (corner1.getZ() + corner2.getZ()) / 2;
        return new Location(corner1.getWorld(), centerX, centerY, centerZ);
      }
    }
  }

}