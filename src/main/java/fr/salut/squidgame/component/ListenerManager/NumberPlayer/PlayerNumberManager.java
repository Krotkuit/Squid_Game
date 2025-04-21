package fr.salut.squidgame.component.ListenerManager.NumberPlayer;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerNumberManager {

    @Getter
    static PlayerNumberManager instance;

    private final Map<Player, Integer> playerNumbers = new HashMap<>();
    private int currentNumber = 1;

    public PlayerNumberManager(){
        instance = this;
    }

    // Attribue un numéro unique à un joueur
    public void assignNumber(Player player) {
        if (!playerNumbers.containsKey(player)) {
            playerNumbers.put(player, currentNumber);
            currentNumber++;
        }
    }

    // Récupère le numéro attribué à un joueur
    public int getPlayerNumber(Player player) {
        return playerNumbers.getOrDefault(player, -1); // Retourne -1 si le joueur n'a pas de numéro
    }

    // Réinitialise les numéros
    public void resetNumbers() {
        playerNumbers.clear();
        currentNumber = 1;
    }
}