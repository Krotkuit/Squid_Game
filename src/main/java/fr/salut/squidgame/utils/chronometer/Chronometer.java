package fr.salut.squidgame.utils.chronometer;

import fr.salut.squidgame.SquidGame;
import lombok.Getter;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Chronometer{

    /**
     * CREDIT : MADE BY NOCOLM
     */

    // Map structure: UUID -> (Group -> Time)
    public static final HashMap<UUID, HashMap<String, Integer>> chronometer = new HashMap<>();
    // Map structure: Group -> Time
    public static final HashMap<String, Integer> serverChronometer = new HashMap<>();
    // Map structure: UUID -> Group -> Task
    private static final HashMap<UUID, HashMap<String, BukkitRunnable>> activeTasks = new HashMap<>();
    // Map structure: Group -> Task
    private static final HashMap<String, BukkitRunnable> serverActiveTasks = new HashMap<>();
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

    @Getter
    public static class ChronometerTimeChangeEvent extends Event {
        private static final HandlerList HANDLERS = new HandlerList();
        private final String group;
        private final int time;

        public ChronometerTimeChangeEvent(String group, int time) {
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
     * put "%sec%" in your message to display the remaining time
     * otherwise the default message will be displayed
     * the display time is in second

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
    public static void startChronometer(Entity entity, String group, int time, ChronometerType messageType, String message, ChronometerType finishMessageType, String finishMessage) {
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
                String timerMessage = "Il reste : " + remainingTime + "s";
                if (message!=null){
                    if (!message.contains("%null%")){
                        if (message.contains("%sec%")) {
                            timerMessage = message.replace("%sec%", String.valueOf(remainingTime));
                        }
                        if (entity instanceof Player player){
                            player.spigot().sendMessage(messageType.getChatMessageType(),new TextComponent(timerMessage));
                        }
                    }
                } else {
                    if (entity instanceof Player player){
                        player.spigot().sendMessage(messageType.getChatMessageType(),new TextComponent(timerMessage));
                    }
                }


                if (timerEnd(entityUUID, group)) {
                    if (entity instanceof Player player){
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

                chronometer.get(entityUUID).put(group, remainingTime - 1);
            }
        };task.runTaskTimer(SquidGame.getInstance(), 0, 20);

        activeTasks.computeIfAbsent(entityUUID, k -> new HashMap<>()).put(group, task);
    }

    public static void startServerChronometer(String tag, String group, int time, ChronometerType messageType, String message, ChronometerType finishMessageType, String finishMessage) {
        serverChronometer.put(group, time);
        List<Player> players = new ArrayList<>();

        if (serverActiveTasks.containsKey(group)) {
            serverActiveTasks.get(group).cancel();
        }

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {

                if (tag == null){
                    players.addAll(SquidGame.getInstance().getServer().getOnlinePlayers());
                } else {
                    for (Player player : SquidGame.getInstance().getServer().getOnlinePlayers()){
                        if (player.getScoreboardTags().contains(tag)){
                            players.add(player);
                        }
                    }
                }

                int remainingTime = serverChronometer.get(group);
                String timerMessage = "Il reste : " + remainingTime + "s";

                for (Player player : players){
                    if (message!=null){
                        if (!message.contains("%null%")){
                            if (message.contains("%sec%")) {
                                timerMessage = message.replace("%sec%", String.valueOf(remainingTime));
                            }
                            if (player != null){
                                player.spigot().sendMessage(messageType.getChatMessageType(),new TextComponent(timerMessage));
                            }
                        }
                    } else {
                        if (player != null){
                            player.spigot().sendMessage(messageType.getChatMessageType(),new TextComponent(timerMessage));
                        }
                    }
                }

                if (serverTimerEnd(group)) {

                    for (Player player : players){
                        if (player != null){
                            player.spigot().sendMessage(finishMessageType.getChatMessageType(), new TextComponent(finishMessage != null ? finishMessage : "Le chronomètre est terminé !"));
                        }
                        serverChronometer.remove(group);
                        cancel();
                        Bukkit.getPluginManager().callEvent(new ServerChronometerEndEvent(group));
                        return;
                    }
                }

                Bukkit.getPluginManager().callEvent(new ChronometerTimeChangeEvent(group, remainingTime-1));
                serverChronometer.put(group, remainingTime - 1);
            }
        };task.runTaskTimer(SquidGame.getInstance(), 0, 20);

        serverActiveTasks.put(group, task);
    }

    /**
     * @param entity entity who is affect
     * @param messageType display type
     * @param message message display when the chronometer is stopped
     */
    public static void stopAllChronometer(Entity entity, ChronometerType messageType, String message) {
        UUID entityUUID = entity.getUniqueId();
        if (chronometer.containsKey(entityUUID)) {
            chronometer.remove(entityUUID);
            if (message!=null){
                if (!message.contains("%null%")){
                    if (entity instanceof Player player){
                        player.spigot().sendMessage(messageType.getChatMessageType(), new TextComponent(message));
                    }
                }
            } else {
                if (entity instanceof Player player){
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
    }

    public static void stopAllServerChronometer(String tag, ChronometerType messageType, String message) {
        List<Player> players = new ArrayList<>();

        if (tag == null){
            players.addAll(SquidGame.getInstance().getServer().getOnlinePlayers());
        } else {
            for (Player player : SquidGame.getInstance().getServer().getOnlinePlayers()){
                if (player.getScoreboardTags().contains(tag)){
                    players.add(player);
                }
            }
        }

        for (Player player : players){
            if (!serverChronometer.isEmpty()) {
                if (message!=null){
                    if (!message.contains("%null%")){
                        if (player != null){
                            player.spigot().sendMessage(messageType.getChatMessageType(), new TextComponent(message));
                        }
                    }
                } else {
                    if (player != null){
                        player.spigot().sendMessage(messageType.getChatMessageType(), new TextComponent("Chronomètre arrêté"));
                    }
                }
            }
        }

        for (Map.Entry<String, BukkitRunnable> entry : serverActiveTasks.entrySet()) {
            entry.getValue().cancel();
        }

        serverActiveTasks.clear();
    }

    /**
     * @param entity entity who is affect
     * @param group Chronometer group
     * @param messageType display type
     * @param message message display when the chronometer is stopped
     */
     public static void stopChronometer(Entity entity, String group, ChronometerType messageType, String message) {
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
                        player.spigot().sendMessage(messageType.getChatMessageType(), new TextComponent(message));
                    }
                }
            } else {
                if (entity instanceof Player player){
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
    }

    public static void stopServerChronometer(String tag, String group, ChronometerType messageType, String message) {
        List<Player> players = new ArrayList<>();

        if (tag == null){
            players.addAll(SquidGame.getInstance().getServer().getOnlinePlayers());
        } else {
            for (Player player : SquidGame.getInstance().getServer().getOnlinePlayers()){
                if (player.getScoreboardTags().contains(tag)){
                    players.add(player);
                }
            }
        }

        for (Player player : players){
            if (serverChronometer.containsKey(group)) {
                serverChronometer.remove(group);
                if (message!=null){
                    if (!message.contains("%null%")){
                        if (player != null){
                            player.spigot().sendMessage(messageType.getChatMessageType(), new TextComponent(message));
                        }
                    }
                } else {
                    if (player != null){
                        player.spigot().sendMessage(messageType.getChatMessageType(), new TextComponent("Chronomètre du " + group + " arrêté"));
                    }
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
}
