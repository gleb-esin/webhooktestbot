package org.example.controller;

import lombok.Data;
import org.example.model.Card;
import org.example.model.Deck;
import org.example.model.Player;

import java.io.Serializable;
import java.util.*;

/**
 * This class provides control over waitingPlayers' behavior during round
 */

@Data
public class PlayerController implements Serializable {
    private boolean isGameOver = false;
    private List<Player> players;
    private Player attacker;
    private Player defender;
    private Player binder;
    private Player winner;
    private Deque<Player> playersQueue;


    public void setPlayersTurn() {
        for (Player player : players) {
            setPlayerMinWeight(player);
        }
        Collections.sort(players);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setTurn(i + 1);
        }
        this.playersQueue = new LinkedList<>(players);
        setAttacker(this.playersQueue.pop());
        setDefender(this.playersQueue.pop());
        this.playersQueue.addFirst(this.attacker);
    }

    private static void setPlayerMinWeight(Player player) {
        int playersMinWeight = 0;
        boolean weightNotSetYet;
        for (Card card : player.getPlayerHand()) {
            weightNotSetYet = playersMinWeight == 0;
            boolean thisCardIsTrump = card.getSuit().isTrump();
            if (thisCardIsTrump) {
                if (weightNotSetYet) {
                    playersMinWeight = card.getWeight();
                } else {
                    boolean setWeightGreaterNewOne = playersMinWeight > card.getWeight();
                    if (setWeightGreaterNewOne) {
                        playersMinWeight = card.getWeight();
                    }
                }
            } else {
                if (weightNotSetYet) {
                    playersMinWeight = card.getWeight() + 1000;
                }
            }
        }
        player.setMinTrumpWeight(playersMinWeight);
    }

    public void setAttacker(Player player) {
        player.setRole("attacker");
        this.attacker = player;
    }

    public void setDefender(Player player) {
        player.setRole("defender");
        this.defender = player;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }
    public boolean isPlayerWinner(Player player, Deck deck) {
        boolean isWinner = deck.isEmpty() && player.getPlayerHand().isEmpty();
        if (isWinner) {
            player.setWinner(true);
            setGameOver(true);
            setWinner(player);
        }
        return isWinner;
    }


    public void changeTurn() {
        this.playersQueue.addLast(this.playersQueue.pop());
        if (this.binder == null) {
            setAttacker(this.defender);
        } else {
            this.playersQueue.addLast(this.binder);
            if (this.playersQueue.size() > 1) setAttacker(this.playersQueue.pop());
        }
        setDefender(this.playersQueue.pop());
        this.playersQueue.addFirst(this.attacker);
        setBinder(null);
    }

    public void setBinder(Player player) {
        this.binder = player;
    }
}
