package fr.salut.squidgame.utils.chronometer;

import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.ListenerManager.intance.TeamManager;
import fr.salut.squidgame.utils.DateUtils;
import lombok.Getter;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Chronometer{

    /**
     * CREDIT : MADE BY NOCOLM
     */

    // [ CHRONOMETER TYPE ] //

    // Map structure: UUID -> (Group -> Time)
    public static final HashMap<UUID, HashMap<String, Integer>> chronometer = new HashMap<>();
    // Map structure: Group -> Time
    public static final HashMap<String, Integer> serverChronometer = new HashMap<>();

    // [ BOSS BAR TYPE ] //

    // Map structure: UUID -> (Group -> BossBar)
    private static final HashMap<UUID, HashMap<String, BossBarChronometer>> chronometerBossBar = new HashMap<>();
    // Map structure: Group -> BossBar
    private static final HashMap<String, BossBarChronometer> serverChronometerBossBar = new HashMap<>();

    // [ TASKS TYPE ] //

    // Map structure: UUID -> Group -> Task
    private static final HashMap<UUID, HashMap<String, BukkitRunnable>> activeTasks = new HashMap<>();
    // Map structure: Group -> Task
    private static final HashMap<String, BukkitRunnable> serverActiveTasks = new HashMap<>();

    // [ CHRONOMETER EVENT ] //

    // new @EventHandler > ChronometerEndEvent
    @Getter
    public static class ChronometerEndEvent extends Event {
        private static final HandlerList HANDLERS = new HandlerList();
        private final Entity entity;
        private final String group;

        public ChronometerEndEvent(Entity entity, String group) {
            this.entity = entity;
            this.group = group;
        }

        public static HandlerList getHandlerList() {
            return HANDLERS;
        }

        @Override
        public @NotNull HandlerList getHandlers() {
            return HANDLERS;
        }
    }

    // new @EventHandler > ServerChronometerEndEvent
    @Getter
    public static class ServerChronometerEndEvent extends Event {
        private static final HandlerList HANDLERS = new HandlerList();
        private final String group;

        public ServerChronometerEndEvent(String group) {
            this.group = group;
        }

        public static HandlerList getHandlerList() {
            return HANDLERS;
        }

        @Override
        public @NotNull HandlerList getHandlers() {
            return HANDLERS;
        }
    }

    // new @EventHandler > ServerChronometerTimeChangeEvent
    @Getter
    public static class ServerChronometerTimeChangeEvent extends Event {
        private static final HandlerList HANDLERS = new HandlerList();
        private final String group;
        private final int time;

        public ServerChronometerTimeChangeEvent(String group, int time) {
            this.group = group;
            this.time = time;
        }

        public static HandlerList getHandlerList() {
            return HANDLERS;
        }

        @Override
        public @NotNull HandlerList getHandlers() {
            return HANDLERS;
        }
    }

    // new @EventHandler > ChronometerTimeChangeEvent
    @Getter
    public static class ChronometerTimeChangeEvent extends Event {
        private static final HandlerList HANDLERS = new HandlerList();
        private final Entity entity;
        private final String group;
        private final int time;

        public ChronometerTimeChangeEvent(Entity entity, String group, int time) {
            this.entity = entity;
            this.group = group;
            this.time = time;
        }

        public static HandlerList getHandlerList() {
            return HANDLERS;
        }

        @Override
        public @NotNull HandlerList getHandlers() {
            return HANDLERS;
        }
    }

    /**
     * FOR "start" :
     * put "%time%" in your message to display the remaining time
     * otherwise the default message will be displayed
     * the display time is converted into 0j 0h 0m 0s

     * FOR "start" / "stopAll" / "stop" :
     * if you don't want to display a message just put "%null%"

     * @param entity entity to add
     * @param group Chronometer group
     * @param time duration in second
     * @param messageType display type
     * @param message to display the time
     * @param finishMessageType display type
     * @param finishMessage message display when the chronometer end normally
     */
    public static void startChronometer(Entity entity, String group, int time, @NotNull ChronometerType messageType, String message, @NotNull ChronometerType finishMessageType, String finishMessage, BarColor barColor, BarStyle barStyle) {
        UUID entityUUID = entity.getUniqueId();
        chronometer.computeIfAbsent(entityUUID, k -> new HashMap<>()).put(group, time);

        if (activeTasks.containsKey(entityUUID) && activeTasks.get(entityUUID).containsKey(group)) {
            activeTasks.get(entityUUID).get(group).cancel();
        }

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {

                if (!chronometer.containsKey(entityUUID)) {
                    cancel();
                    return;
                }

                int remainingTime = chronometer.get(entityUUID).get(group);
                String timerMessage = "Il reste : " + DateUtils.convertSecondToTime(remainingTime);
                if (message!=null){
                    if (!message.contains("%null%")){
                        if (message.contains("%time%")) {
                            timerMessage = message.replace("%time%", DateUtils.convertSecondToTime(remainingTime));
                        }
                        if (entity instanceof Player player){
                            if (messageType.equals(ChronometerType.BOSSBAR)){
                                if (!chronometerBossBar.containsKey(entityUUID))
                                    chronometerBossBar.computeIfAbsent(entityUUID, k -> new HashMap<>()).put(group, new BossBarChronometer(time, barColor, barStyle));
                                chronometerBossBar.get(entityUUID).get(group).bossBar.addPlayer(player);
                                chronometerBossBar.get(entityUUID).get(group).updateBossBar(remainingTime, timerMessage);
                            } else
                                player.spigot().sendMessage(messageType.getChatMessageType(),new TextComponent(timerMessage));
                        }
                    }
                } else {
                    if (entity instanceof Player player){
                        if (messageType.equals(ChronometerType.BOSSBAR)){
                            if (!chronometerBossBar.containsKey(entityUUID))
                                chronometerBossBar.computeIfAbsent(entityUUID, k -> new HashMap<>()).put(group, new BossBarChronometer(time, barColor, barStyle));
                            chronometerBossBar.get(entityUUID).get(group).bossBar.addPlayer(player);
                            chronometerBossBar.get(entityUUID).get(group).updateBossBar(remainingTime, timerMessage);
                        } else
                            player.spigot().sendMessage(messageType.getChatMessageType(),new TextComponent(timerMessage));
                    }
                }


                if (timerEnd(entityUUID, group)) {
                    if (entity instanceof Player player && !finishMessage.contains("%null%")){
                        if (messageType.equals(ChronometerType.BOSSBAR)){
                            chronometerBossBar.get(entityUUID).get(group).updateBossBar(0, finishMessage != null ? finishMessage : "Le chronomètre est terminé !");
                            Bukkit.getScheduler().scheduleSyncDelayedTask(SquidGame.getInstance(), new Runnable() {
                                public void run() {
                                    chronometerBossBar.get(entityUUID).get(group).destroy();
                                    chronometerBossBar.get(entityUUID).remove(group);
                                    if (chronometerBossBar.get(entityUUID).isEmpty()) chronometerBossBar.remove(entityUUID);
                                }
                            }, 40); // delay de 2s
                        } else
                            player.spigot().sendMessage(finishMessageType.getChatMessageType(), new TextComponent(finishMessage != null ? finishMessage : "Le chronomètre est terminé !"));
                    }
                    chronometer.get(entityUUID).remove(group);
                    if (chronometer.get(entityUUID).isEmpty()){
                        chronometer.remove(entityUUID);
                    }
                    Bukkit.getPluginManager().callEvent(new ChronometerEndEvent(entity, group));
                    cancel();
                    return;
                }

                Bukkit.getPluginManager().callEvent(new ChronometerTimeChangeEvent(entity, group, remainingTime-1));
                chronometer.get(entityUUID).put(group, remainingTime - 1);

            }
        };

        task.runTaskTimer(SquidGame.getInstance(), 0, 20);

        activeTasks.computeIfAbsent(entityUUID, k -> new HashMap<>()).put(group, task);
    }

    public static void startChronometer(Entity entity, String group, int time, @NotNull ChronometerType messageType, String message, @NotNull ChronometerType finishMessageType, String finishMessage) {
        startChronometer(entity, group, time, messageType, message, finishMessageType, finishMessage, null, null);
    }

    public static void startServerChronometer(String tag, Team team, String group, int time, @NotNull ChronometerType messageType, String message, @NotNull ChronometerType finishMessageType, String finishMessage, BarColor barColor, BarStyle barStyle) {
        serverChronometer.put(group, time);
        List<Player> players = new ArrayList<>();

        if (serverActiveTasks.containsKey(group)) {
            serverActiveTasks.get(group).cancel();
        }

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {

                if (tag == null && team == null){
                    players.addAll(SquidGame.getInstance().getServer().getOnlinePlayers());
                } else if (tag != null){
                    for (Player player : SquidGame.getInstance().getServer().getOnlinePlayers()){
                        if (player.getScoreboardTags().contains(tag)){
                            players.add(player);
                        }
                    }
                } else {
                    players.addAll(TeamManager.getTeamOnlinePlayers(team));
                }

                int remainingTime = serverChronometer.get(group);
                String timerMessage = "Il reste : " + DateUtils.convertSecondToTime(remainingTime);

                for (Player player : players){
                    if (message!=null){
                        if (!message.contains("%null%")){
                            if (message.contains("%time%")) {
                                timerMessage = message.replace("%time%", DateUtils.convertSecondToTime(remainingTime));
                            }
                            if (player != null){
                                if (messageType.equals(ChronometerType.BOSSBAR)){
                                    if (!serverChronometerBossBar.containsKey(group))
                                        serverChronometerBossBar.put(group, new BossBarChronometer(time, barColor, barStyle));
                                    serverChronometerBossBar.get(group).bossBar.addPlayer(player);
                                    serverChronometerBossBar.get(group).updateBossBar(remainingTime, timerMessage);
                                } else
                                    player.spigot().sendMessage(messageType.getChatMessageType(),new TextComponent(timerMessage));
                            }
                        }
                    } else {
                        if (player != null){
                            if (messageType.equals(ChronometerType.BOSSBAR)){
                                if (!serverChronometerBossBar.containsKey(group))
                                    serverChronometerBossBar.put(group, new BossBarChronometer(time, barColor, barStyle));
                                serverChronometerBossBar.get(group).bossBar.addPlayer(player);
                                serverChronometerBossBar.get(group).updateBossBar(remainingTime, timerMessage);
                            } else
                                player.spigot().sendMessage(messageType.getChatMessageType(),new TextComponent(timerMessage));
                        }
                    }
                }

                if (serverTimerEnd(group)) {

                    for (Player player : players){
                        if (player != null){
                            if (messageType.equals(ChronometerType.BOSSBAR)){
                                serverChronometerBossBar.get(group).updateBossBar(0, finishMessage != null ? finishMessage : "Le chronomètre est terminé !");
                                Bukkit.getScheduler().scheduleSyncDelayedTask(SquidGame.getInstance(), new Runnable() {
                                    public void run() {
                                        serverChronometerBossBar.get(group).destroy();
                                        serverChronometerBossBar.remove(group);
                                    }
                                }, 40); // delay de 2s
                            } else
                                player.spigot().sendMessage(finishMessageType.getChatMessageType(), new TextComponent(finishMessage != null ? finishMessage : "Le chronomètre est terminé !"));
                        }
                        serverChronometer.remove(group);
                        cancel();
                        Bukkit.getPluginManager().callEvent(new ServerChronometerEndEvent(group));
                        return;
                    }
                }

                Bukkit.getPluginManager().callEvent(new ServerChronometerTimeChangeEvent(group, remainingTime-1));
                serverChronometer.put(group, remainingTime - 1);
            }
        };task.runTaskTimer(SquidGame.getInstance(), 0, 20);

        serverActiveTasks.put(group, task);
    }

    public static void startServerChronometer(Team team, String group, int time, @NotNull ChronometerType messageType, String message, @NotNull ChronometerType finishMessageType, String finishMessage) {
        startServerChronometer(null, team, group, time, messageType, message, finishMessageType, finishMessage, null, null);
    }

    public static void startServerChronometer(String tag, String group, int time, @NotNull ChronometerType messageType, String message, @NotNull ChronometerType finishMessageType, String finishMessage) {
        startServerChronometer(tag, null, group, time, messageType, message, finishMessageType, finishMessage, null, null);
    }

    public static void startServerChronometer(String group, int time, @NotNull ChronometerType messageType, String message, @NotNull ChronometerType finishMessageType, String finishMessage) {
        startServerChronometer(null, null, group, time, messageType, message, finishMessageType, finishMessage, null, null);
    }

    /**
     * @param entity entity who is affect
     * @param messageType display type
     * @param message message display when the chronometer is stopped
     */
    public static void stopAllChronometer(Entity entity, @NotNull ChronometerType messageType, String message) {
        UUID entityUUID = entity.getUniqueId();
        if (chronometer.containsKey(entityUUID)) {
            chronometer.remove(entityUUID);
            if (message!=null){
                if (!message.contains("%null%")){
                    if (entity instanceof Player player){
                        if (!messageType.equals(ChronometerType.BOSSBAR))
                            player.spigot().sendMessage(messageType.getChatMessageType(), new TextComponent(message));
                    }
                }
            } else {
                if (entity instanceof Player player){
                    if (!messageType.equals(ChronometerType.BOSSBAR))
                        player.spigot().sendMessage(messageType.getChatMessageType(), new TextComponent("Chronomètre arrêté"));
                }
            }
        }

        if (activeTasks.containsKey(entityUUID)) {
            for (Map.Entry<String, BukkitRunnable> entry : activeTasks.get(entityUUID).entrySet()) {
                entry.getValue().cancel();
            }
            activeTasks.remove(entityUUID);
        }

        if (chronometerBossBar.containsKey(entityUUID)){
            for (Map.Entry<String, BossBarChronometer> entry : chronometerBossBar.get(entityUUID).entrySet()) {
                entry.getValue().destroy();
            }
            chronometerBossBar.remove(entityUUID);
        }
    }

    public static void stopAllServerChronometer(String tag, Team team, @NotNull ChronometerType messageType, String message) {
        List<Player> players = new ArrayList<>();

        if (tag == null && team == null){
            players.addAll(SquidGame.getInstance().getServer().getOnlinePlayers());
        } else if (tag != null){
            for (Player player : SquidGame.getInstance().getServer().getOnlinePlayers()){
                if (player.getScoreboardTags().contains(tag)){
                    players.add(player);
                }
            }
        } else {
            players.addAll(TeamManager.getTeamOnlinePlayers(team));
        }

        for (Player player : players){
            if (!serverChronometer.isEmpty()) {
                if (message!=null){
                    if (!message.contains("%null%")){
                        if (player != null)
                            if (!messageType.equals(ChronometerType.BOSSBAR))
                                player.spigot().sendMessage(messageType.getChatMessageType(), new TextComponent(message));
                    }
                } else {
                    if (player != null)
                        if (!messageType.equals(ChronometerType.BOSSBAR))
                            player.spigot().sendMessage(messageType.getChatMessageType(), new TextComponent(message));
                }
            }
        }

        for (Map.Entry<String, BukkitRunnable> entry : serverActiveTasks.entrySet()) {
            entry.getValue().cancel();
        }

        serverActiveTasks.clear();

        for (Map.Entry<String, BossBarChronometer> entry : serverChronometerBossBar.entrySet()) {
            entry.getValue().destroy();
        }

        serverChronometerBossBar.clear();
    }

    public static void stopAllServerChronometer(String tag, @NotNull ChronometerType messageType, String message) {
        stopAllServerChronometer(tag, null, messageType, message);
    }

    public static void stopAllServerChronometer(Team team, @NotNull ChronometerType messageType, String message) {
        stopAllServerChronometer(null, team, messageType, message);
    }

    public static void stopAllServerChronometer(@NotNull ChronometerType messageType, String message) {
        stopAllServerChronometer(null, null, messageType, message);
    }


        /**
         * @param entity entity who is affect
         * @param group Chronometer group
         * @param messageType display type
         * @param message message display when the chronometer is stopped
         */
     public static void stopChronometer(Entity entity, String group, @NotNull ChronometerType messageType, String message) {
        UUID entityUUID = entity.getUniqueId();

        if (chronometer.containsKey(entityUUID) && chronometer.get(entityUUID).containsKey(group)) {
            chronometer.get(entityUUID).remove(group);

            if (activeTasks.containsKey(entityUUID) && activeTasks.get(entityUUID).containsKey(group)) {
                activeTasks.get(entityUUID).get(group).cancel();
                activeTasks.get(entityUUID).remove(group);
            }

            if (message!=null){
                if (!message.contains("%null%")){
                    if (entity instanceof Player player){
                        if (!messageType.equals(ChronometerType.BOSSBAR))
                            player.spigot().sendMessage(messageType.getChatMessageType(), new TextComponent(message));
                    }
                }
            } else {
                if (entity instanceof Player player){
                    if (!messageType.equals(ChronometerType.BOSSBAR))
                        player.spigot().sendMessage(messageType.getChatMessageType(), new TextComponent("Chronomètre du " + group + " arrêté"));
                }
            }

            if (chronometer.get(entityUUID).isEmpty()) {
                chronometer.remove(entityUUID);
            }
        } else {
            if (entity instanceof Player player){
                player.sendMessage("§cAucun chronomètre trouvé pour le groupe §e" + group + ".");
            }
        }

        if (chronometerBossBar.containsKey(entityUUID) && chronometerBossBar.get(entityUUID).containsKey(group))
            chronometerBossBar.get(entityUUID).get(group).destroy();
    }

    public static void stopServerChronometer(String tag, Team team, String group, @NotNull ChronometerType messageType, String message) {
        List<Player> players = new ArrayList<>();

        if (tag == null && team == null){
            players.addAll(SquidGame.getInstance().getServer().getOnlinePlayers());
        } else if (tag != null){
            for (Player player : SquidGame.getInstance().getServer().getOnlinePlayers()){
                if (player.getScoreboardTags().contains(tag)){
                    players.add(player);
                }
            }
        } else {
            players.addAll(TeamManager.getTeamOnlinePlayers(team));
        }

        for (Player player : players){
            if (serverChronometer.containsKey(group)) {
                serverChronometer.remove(group);
                if (message!=null){
                    if (!message.contains("%null%")){
                        if (player != null)
                            if (!messageType.equals(ChronometerType.BOSSBAR))
                                player.spigot().sendMessage(messageType.getChatMessageType(), new TextComponent(message));
                    }
                } else {
                    if (player != null)
                        if (!messageType.equals(ChronometerType.BOSSBAR))
                            player.spigot().sendMessage(messageType.getChatMessageType(), new TextComponent("Chronomètre du " + group + " arrêté"));
                }
            } else {
                if (player != null){
                    player.sendMessage("§cAucun chronomètre trouvé pour le groupe §e" + group + ".");
                }
            }
        }

        if (serverActiveTasks.containsKey(group)) {
            serverActiveTasks.get(group).cancel();
            serverActiveTasks.remove(group);
        }

        if (serverChronometerBossBar.containsKey(group)){
            serverChronometerBossBar.get(group).destroy();
            serverChronometerBossBar.remove(group);
        }
    }

    public static void stopServerChronometer(String tag, String group, @NotNull ChronometerType messageType, String message) {
         stopServerChronometer(tag, null, group, messageType, message);
    }

    public static void stopServerChronometer(Team team, String group, @NotNull ChronometerType messageType, String message) {
        stopServerChronometer(null, team, group, messageType, message);
    }

    public static void stopServerChronometer(String group, @NotNull ChronometerType messageType, String message) {
        stopServerChronometer(null, null, group, messageType, message);
    }

    public static void listChronometers(Entity entity, Player owner) {
        UUID entitytUUID = entity.getUniqueId();

        if (chronometer.containsKey(entitytUUID)) {
            owner.sendMessage("§aChronomètres actifs :");
            chronometer.get(entitytUUID).forEach((group, time) ->
                    owner.sendMessage(" §e- " + group + ": §6" + time + "s")
            );
        } else {
            owner.sendMessage("§cCe joueur n'a aucun chronomètre actif.");
        }
    }

    /**
     * @return the remaining time
     */
    public static int getRemainingTime(UUID entityUUID, String group){
        return chronometer.get(entityUUID).get(group);
    }

    /**
     * @return true if chronometer has expired
     */
    public static boolean timerEnd(UUID entityUUID, String group){
        return chronometer.get(entityUUID).get(group) <= 0;
    }

    public static boolean serverTimerEnd(String group){
        return serverChronometer.get(group) <= 0;
    }

    public static boolean containsChronometer(UUID entityUUID, String group) {
        if (chronometer.containsKey(entityUUID)){
            return chronometer.get(entityUUID).containsKey(group);
        }
        return false;
    }

    public static class BossBarChronometer {

        BossBar bossBar;
        NamespacedKey key;
        double maxTime;

        public BossBarChronometer(double maxTime, BarColor barColor, BarStyle barStyle){
            if (barColor == null) barColor = BarColor.RED;
            if (barStyle == null) barStyle = BarStyle.SEGMENTED_6;

            this.maxTime = maxTime;
            this.key = new NamespacedKey("squidgame", UUID.randomUUID().toString()); // identifiant de la bossBar
            this.bossBar = Bukkit.createBossBar(key, DateUtils.convertSecondToTime((int) maxTime), barColor, barStyle);
            bossBar.setProgress(1.0);
        }

        private void updateBossBar(double time, String message){
            bossBar.setProgress(time/maxTime);
            bossBar.setTitle(message);
        }

        private void destroy(){
            bossBar.removeAll();
            Bukkit.removeBossBar(key);
        }
    }
}
