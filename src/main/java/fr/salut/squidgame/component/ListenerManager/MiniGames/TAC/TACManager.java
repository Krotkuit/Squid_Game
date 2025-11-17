package fr.salut.squidgame.component.ListenerManager.MiniGames.TAC;

import fr.salut.squidgame.SquidGame;
import fr.salut.squidgame.component.ListenerManager.intance.TeamManager;
import fr.salut.squidgame.utils.chronometer.Chronometer;
import fr.salut.squidgame.utils.chronometer.ChronometerType;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Stream;

public class TACManager implements Listener {

    public static double team1Click = 0;
    public static double team2Click = 0;
    public static int maxSpeed = 20;
    private final int minSpeed = 4;

    public static Team team1;
    public static Team team2;

    private static final Vector west = new Vector(-1, 0, 0).normalize().multiply(0.1);
    private static final Vector east = new Vector(1, 0, 0).normalize().multiply(0.1);
    private static final Vector upeast = new Vector(1, 1, 0).normalize().multiply(0.1);
    private static final Vector upwest = new Vector(-1, 1, 0).normalize().multiply(0.1);
    private static final Vector down = new Vector(0, -0.5, 0).normalize().multiply(0.1);
    private static final Vector endWest = new Vector(-2, 0, 0).normalize().multiply(0.1);
    private static final Vector endEast = new Vector(2, 0, 0).normalize().multiply(0.1);

    private static final Map<Player, PlayerTACData> playersTACData = new HashMap<>();

    private static final World TACW = Bukkit.getWorld("world");

    private static final ItemStack gray_glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    private static final ItemStack red_glass = new ItemStack(Material.RED_STAINED_GLASS_PANE);
    private static final ItemStack lime_glass = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
    private static final ItemStack green_glass = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
    private static final ItemStack air = new ItemStack(Material.AIR);
    private static final ItemStack barrier = new ItemStack(Material.BARRIER);

    private static final Random random = new Random();

    public static List<Player> playersTeam1 = new ArrayList<>();
    public static List<Player> playersTeam2 = new ArrayList<>();
    private static final List<Location> team1Locs = List.of(
            new Location(TACW, -22.5, 151, -126.5, -90, 0),
            new Location(TACW, -21.5, 151, -128.5, -90, 0),
            new Location(TACW, -20.5, 151, -126.5, -90, 0),
            new Location(TACW, -19.5, 151, -128.5, -90, 0),
            new Location(TACW, -18.5, 151, -126.5, -90, 0),
            new Location(TACW, -17.5, 151, -128.5, -90, 0),
            new Location(TACW, -16.5, 151, -126.5, -90, 0),
            new Location(TACW, -15.5, 151, -128.5, -90, 0),
            new Location(TACW, -14.5, 151, -126.5, -90, 0),
            new Location(TACW, -13.5, 151, -128.5, -90, 0)
    );
    private static final List<Location> team2Locs = List.of(
            new Location(TACW, 21.5, 151, -128.5, 90, 0),
            new Location(TACW, 20.5, 151, -126.5, 90, 0),
            new Location(TACW, 19.5, 151, -128.5, 90, 0),
            new Location(TACW, 18.5, 151, -126.5, 90, 0),
            new Location(TACW, 17.5, 151, -128.5, 90, 0),
            new Location(TACW, 16.5, 151, -126.5, 90, 0),
            new Location(TACW, 15.5, 151, -128.5, 90, 0),
            new Location(TACW, 14.5, 151, -126.5, 90, 0),
            new Location(TACW, 13.5, 151, -128.5, 90, 0),
            new Location(TACW, 12.5, 151, -126.5, 90, 0)
    );
    private static final List<BukkitRunnable> actualTasks = new ArrayList<>();

    public static void startTAC(){

        playersTeam1.addAll(TeamManager.getTeamOnlinePlayers(team1));
        playersTeam2.addAll(TeamManager.getTeamOnlinePlayers(team2));

//        if (playersTeam1.isEmpty() || playersTeam2.isEmpty()){
//            SquidGame.getInstance().getLogger().warning("Teams not loaded, pls use ");
//            return;
//        }

        TACW.setDifficulty(Difficulty.EASY);

        for (Player player : playersTeam1) {
            setActionBar(player);
            player.getScoreboardTags().add("no_drop");
            player.setGravity(false);
            player.setFoodLevel(2);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, PotionEffect.INFINITE_DURATION, 255, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, PotionEffect.INFINITE_DURATION, 150, true, false));
        }
        for (Player player : playersTeam2){
            setActionBar(player);
            player.getScoreboardTags().add("no_drop");
            player.setGravity(false);
            player.setFoodLevel(2);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, PotionEffect.INFINITE_DURATION, 255, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, PotionEffect.INFINITE_DURATION, 150, true, false));
        }

        moveBarCursor();

        BukkitRunnable gameTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (SquidGame.getInstance().getTacState().equals(TACState.OFF)) cancel();
                if (SquidGame.getInstance().getTacState().equals(TACState.STOP)) return;

                if (team1Click > 25 || team2Click > 25){
                    for (Player player : TeamManager.getTeamOnlinePlayers(team1)){
                        if (team1Click<team2Click) pushForward(player, team1);
                        if (team2Click<team1Click) pushBackward(player, team1);
                    }
                    for (Player player : TeamManager.getTeamOnlinePlayers(team2)){
                        if (team1Click<team2Click) pushBackward(player, team2);
                        if (team2Click<team1Click) pushForward(player, team2);
                    }
                }
            }
        }; gameTask.runTaskTimer(SquidGame.getInstance(), 0, 20);

        actualTasks.add(gameTask);
    }

    public static void placePlayers() {

        if (team1 == null || team2 == null){
            SquidGame.getInstance().getLogger().warning("Team 1 or Team 2 isn't set yet !");
            return;
        }

        playersTeam1.addAll(TeamManager.getTeamOnlinePlayers(team1));
        playersTeam2.addAll(TeamManager.getTeamOnlinePlayers(team2));

        // Créer des copies modifiables et INVERSER l'ordre
        List<Location> availableTeam1Locs = new ArrayList<>(team1Locs);
        List<Location> availableTeam2Locs = new ArrayList<>(team2Locs);

        Collections.reverse(availableTeam1Locs);
        Collections.reverse(availableTeam2Locs);

        // Team 1 : assigner les joueurs aux positions les plus proches
        assignPlayersToClosestLocations(playersTeam1, availableTeam1Locs);

        // Team 2 : assigner les joueurs aux positions les plus proches
        assignPlayersToClosestLocations(playersTeam2, availableTeam2Locs);
    }

    private static void assignPlayersToClosestLocations(List<Player> players, List<Location> locations) {
        List<Player> unassignedPlayers = new ArrayList<>(players);

        // Parcourir chaque position de 1 à 10
        for (Location location : locations) {
            if (unassignedPlayers.isEmpty()) break;

            Player closestPlayer = null;
            double minDistance = Double.MAX_VALUE;

            // Trouver le joueur le plus proche de cette position
            for (Player player : unassignedPlayers) {
                double distance = player.getLocation().distance(location);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPlayer = player;
                }
            }

            // Téléporter le joueur le plus proche et le retirer de la liste
            if (closestPlayer != null) {
                closestPlayer.teleport(location);
                unassignedPlayers.remove(closestPlayer);
            }
        }

        // Si plus de 10 joueurs, répartir les restants sur les positions déjà utilisées
        if (!unassignedPlayers.isEmpty()) {
            int index = 0;
            for (Player player : unassignedPlayers) {
                player.teleport(locations.get(index % locations.size()));
                index++;
            }
        }
    }

    public static void resetTAC(){
        Stream.concat(TeamManager.getTeamOnlinePlayers(team1).stream(),
                TeamManager.getTeamOnlinePlayers(team2).stream()).forEach(TACManager::clearActionBar);
        for (BukkitRunnable runnable : actualTasks)
            runnable.cancel();
        actualTasks.clear();
    }

    public static void stopTAC(Team looser, Team winner){
        for (BukkitRunnable runnable : actualTasks)
            runnable.cancel();
        actualTasks.clear();

        // reset les joueurs de la team looser
        for (Player teamPlayer : TeamManager.getTeamOnlinePlayers(looser)){
            teamPlayer.setGravity(true);
            teamPlayer.setFoodLevel(20);
            teamPlayer.removePotionEffect(PotionEffectType.SLOW);
            teamPlayer.removePotionEffect(PotionEffectType.JUMP);
            clearActionBar(teamPlayer);
        }

        // reset les joueurs de la team winner
        for (Player teamPlayer : TeamManager.getTeamOnlinePlayers(winner)){
            teamPlayer.setGravity(true);
            teamPlayer.setFoodLevel(20);
            teamPlayer.removePotionEffect(PotionEffectType.SLOW);
            teamPlayer.removePotionEffect(PotionEffectType.JUMP);
            clearActionBar(teamPlayer);

            if (winner.equals(team1)) teamPlayer.setVelocity(endWest);
            if (winner.equals(team2)) teamPlayer.setVelocity(endEast);
        }

        TACW.setDifficulty(Difficulty.PEACEFUL);
        SquidGame.getInstance().setTacState(TACState.OFF);
    }

    private static void pushForward(Player player, Team team) {
        if (new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - 0.7, player.getLocation().getZ()).getBlock().getType().isAir()){
            if (playersTeam1.contains(player)) player.teleport(new Location(player.getWorld(), player.getLocation().getX() + 0.35, player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
            if (playersTeam2.contains(player)) player.teleport(new Location(player.getWorld(), player.getLocation().getX() - 0.35, player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
            if (team.equals(team1)) playersTeam1.remove(player);
            if (team.equals(team2)) playersTeam2.remove(player);

            player.setVelocity(down);
        } else {
            if (team.equals(team1)){
                player.setVelocity(east);
            }
            if (team.equals(team2)) {
                player.setVelocity(west);
            }
        }
        if (playersTeam1.isEmpty() && team==team1){
            stopTAC(team1, team2);
            return;
        }
        if (playersTeam2.isEmpty() && team==team2){
            stopTAC(team2, team1);
        }
    }

    private static void pushBackward(Player player, Team team) {
        if (new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - 0.7, player.getLocation().getZ()).getBlock().getType().isAir()){
            if (team.equals(team1)) player.setVelocity(upwest);
            if (team.equals(team2)) player.setVelocity(upeast);
            return;
        }
        if (team.equals(team1)){ // west
            if (!playersTeam1.contains(player)) playersTeam1.add(player);
            player.setVelocity(west);
        }
        if (team.equals(team2)) { // east
            if (!playersTeam2.contains(player)) playersTeam2.add(player);
            player.setVelocity(east);
        }
    }

    private void showClick(){
        for (Player player : TeamManager.getTeamOnlinePlayers(team1)) {
            if (team1Click < team2Click) player.sendActionBar("§6Nombre de click : §l§c" + team1Click + "§r§l | §r§6différnce : §l§c" + (team1Click-team2Click));
            if (team2Click < team1Click) player.sendActionBar("§6Nombre de click : §l§a" + team1Click + "§r§l | §r§6différnce : §l§a+" + (team1Click-team2Click));
        }
        for (Player player : TeamManager.getTeamOnlinePlayers(team2)) {
            if (team1Click < team2Click) player.sendActionBar("§6Nombre de click : §l§a" + team2Click + "§r§l | §r§6différnce : §l§a+" + (team2Click-team1Click));
            if (team2Click < team1Click) player.sendActionBar("§6Nombre de click : §l§c" + team2Click + "§r§l | §r§6différnce : §l§c" + (team2Click-team1Click));
        }
    }

    private static void setActionBar(Player player){
        if (!playersTACData.containsKey(player)){
            playersTACData.put(player, new PlayerTACData(0, maxSpeed, true, true, 0));
            player.getInventory().setItem(0, gray_glass);
        }

        int randomSlot = random.nextInt(0,9);

        for (int i = 0; i <= 9; i++) {
            if (player.getInventory().getItem(i) != null)
                if (i == playersTACData.get(player).cursorSlot){
                    player.getInventory().setItem(i, i == randomSlot ? green_glass : gray_glass);
                    continue;
                }
            if (randomSlot == i)
                player.getInventory().setItem(i, i == playersTACData.get(player).cursorSlot ? green_glass : lime_glass);
            else player.getInventory().setItem(i, red_glass);
        }
    }

    private static void lockActionBar(Player player){
        Chronometer.startChronometer(player, "TacLock", 3, ChronometerType.ACTION_BAR, "%null%", ChronometerType.ACTION_BAR, "%null%");
        for (int i = 0; i <= 9; i++)
            player.getInventory().setItem(i, barrier);
    }

    private static void clearActionBar(Player player){
        for (int i = 0; i <= 9; i++)
            player.getInventory().setItem(i, air);
        for (Map.Entry<Player, PlayerTACData> dataEntry : playersTACData.entrySet()){
            PlayerTACData data = dataEntry.getValue();
            data = null;
        }
        playersTACData.clear();
    }

    private static void moveBarCursor(){
        BukkitRunnable barTask = new BukkitRunnable() {

            @Override
            public void run() {

                Stream.concat(playersTeam1.stream(), playersTeam2.stream()).forEach( player ->  {

                    PlayerTACData playerTACData = playersTACData.get(player);
                    System.out.println(playerTACData.canClick);
                    System.out.println(playerTACData.ticks);
                    System.out.println(playerTACData.speed / 2);

                    if (playerTACData.canClick) {

                        if (playerTACData.ticks == (playerTACData.speed / 2)) { // 49/2 = 20 speed | 48/2 = 19 speed

                            int slot = playerTACData.cursorSlot;

                            if (playerTACData.sense) { // déplace vers la droite

                                if (player.getInventory().getItem(slot).equals(green_glass)) { // correspond a gray + lime
                                    player.getInventory().setItem(slot, lime_glass); // slot à cliquer quand le gray est dessus
                                } else
                                    player.getInventory().setItem(slot, red_glass); // slot à ne pas cliquer
                                slot++;
                                if (player.getInventory().getItem(slot).equals(lime_glass))
                                    player.getInventory().setItem(slot, green_glass);
                                else
                                    player.getInventory().setItem(slot, gray_glass); // actuel slot

                                if (slot == 8) {
                                    playerTACData.setSense(false);// changer le sens si le dernier slot est atteint
                                }
                            } else { // déplace vers la gauche

                                if (player.getInventory().getItem(slot).equals(green_glass)) { // correspond a gray + lime
                                    player.getInventory().setItem(slot, lime_glass); // slot à cliquer quand le gray est dessus
                                } else
                                    player.getInventory().setItem(slot, red_glass); // slot à ne pas cliquer
                                slot--;
                                if (player.getInventory().getItem(slot).equals(lime_glass))
                                    player.getInventory().setItem(slot, green_glass);
                                else
                                    player.getInventory().setItem(slot, gray_glass); // actuel slot

                                if (slot == 0) {
                                    playerTACData.setSense(true); // changer le sens si le premier slot est atteint
                                }
                            }

                            playerTACData.setCursorSlot(slot);
                        }


                        if (playerTACData.ticks == playerTACData.speed / 2)
                            playerTACData.ticks = 0;
                        else
                            playerTACData.increaseTicks();
                    }
                });
            }
        };
        barTask.runTaskTimer(SquidGame.getInstance(), 0, 1); // tous les ticks

        actualTasks.add(barTask);
    }

    @EventHandler
    public void onPlayerClick(PlayerAnimationEvent event){
        if (!SquidGame.getInstance().getTacState().equals(TACState.ON)) return;

        Player player = event.getPlayer();

        if (!Stream.concat(playersTeam1.stream(), playersTeam2.stream()).toList().contains(player)) return;

        // Vérifier si le joueur regarde un bloc (raytrace)
        Block targetBlock = player.getTargetBlockExact(3); // 3 blocs de portée
        if (targetBlock == null || targetBlock.getType().isAir()) return;
        if (targetBlock.getType() != Material.valueOf("COPYCATS_COPYCAT_SHAFT")) return;

        Team team = TeamManager.getTeam(player);
        if (team != null){

            PlayerTACData data = playersTACData.get(player);

            if (!data.canClick){
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 8, 1);
                return;
            }

            if (player.getInventory().getItem(data.cursorSlot).equals(green_glass)){
                if (data.speed != minSpeed)
                    data.decreaseSpeed();
                setActionBar(player);
                if (team.equals(team1)) team1Click++;
                if (team.equals(team2)) team2Click++;
            } else {
                playersTACData.get(player).canClick = false;
                data.resetSpeed();
                lockActionBar(player);
            }

            showClick();
        }
    }

    @EventHandler
    public void onFoodRegen(FoodLevelChangeEvent event){
        if (!SquidGame.getInstance().getTacState().equals(TACState.ON)) return;

        // Vérifier que c'est un joueur
        if (!(event.getEntity() instanceof Player player)) return;

        // Vérifier que le joueur est dans une des équipes
        if (!Stream.concat(playersTeam1.stream(), playersTeam2.stream()).toList().contains(player)) return;

        event.setFoodLevel(2);
        event.setCancelled(true);
    }

    @EventHandler
    public void onPLayerMove(PlayerMoveEvent event){
        if (!SquidGame.getInstance().getTacState().equals(TACState.ON)) return;
        Player player = event.getPlayer();
        if (!Stream.concat(playersTeam1.stream(), playersTeam2.stream()).toList().contains(player)) return;
        player.setGravity(!new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - 0.7, player.getLocation().getZ()).getBlock().getType().isAir());
    }

    @EventHandler
    public void onPLayerInventoryOpen(InventoryOpenEvent event){
        if (!SquidGame.getInstance().getTacState().equals(TACState.ON)) return;
        Player player = (Player) event.getPlayer();
        if (!Stream.concat(playersTeam1.stream(), playersTeam2.stream()).toList().contains(player)) return;
        player.closeInventory();
    }

    @EventHandler
    public void onTimerChangeEvent(Chronometer.ChronometerTimeChangeEvent event){
        if (event.getEntity() instanceof Player player){
            if (event.getGroup().equals("TacLock")){
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, 8, 1);
            }
        }
    }

    @EventHandler
    public void onTimerEnd(Chronometer.ChronometerEndEvent event){
        if (event.getEntity() instanceof Player player){
            if (event.getGroup().equals("TacLock")){
                setActionBar(player);
                playersTACData.get(player).canClick = true;
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 8, 1);
            }
        }
    }

    public static class PlayerTACData{

        @Setter
        int cursorSlot;
        @Setter
        int speed;
        @Setter
        boolean sense;
        @Setter
        boolean canClick;
        @Setter
        int ticks;

        public PlayerTACData(int cursorSlot, int speed, boolean sense, boolean canClick, int ticks){
            this.cursorSlot = cursorSlot;
            this.speed = speed;
            this.sense = sense;
            this.canClick = canClick;
            this.ticks = ticks;
        }

        private void decreaseSpeed(){
            speed -= 1;
        }

        private void resetSpeed(){
            speed = TACManager.maxSpeed;
        }

        private void increaseTicks(){
            ticks += 1;
        }
    }

}