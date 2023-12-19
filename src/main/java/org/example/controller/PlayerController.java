package org.example.controller;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.example.model.Card;
import org.example.model.Deck;
import org.example.model.Player;

import java.io.Serializable;
import java.util.*;

/**
 * This class provides control over waitingPlayers' behavior during round
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerController implements Serializable {
    boolean isGameOver = false;
    List<Player> players;
    Player attacker;
    Player defender;
    @Setter
    Player binder;
    @Setter
    Player winner;
    Deque<Player> throwQueue;

    public PlayerController(List<Player> players) {
        this.players = players;
    }

    public void setPlayersTurn() {
        for (Player player : players) {
            setPlayerMinWeight(player);
        }
        Collections.sort(players);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setTurn(i + 1);
        }
        this.throwQueue = new LinkedList<>(players);
        setAttacker(this.throwQueue.pop());
        setDefender(this.throwQueue.pop());
        if (this.throwQueue.size() > 0) {
            throwQueue.forEach(p -> p.setRole("thrower"));
        }
        this.throwQueue.addFirst(this.attacker);
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
            player.setWins(player.getWins() + 1);
        }
        return isWinner;
    }


    public void changeTurn() {
        Player attacker = this.throwQueue.pop();
        attacker.setRole("thrower");
        this.throwQueue.addLast(attacker);
        if (this.binder == null) {
            setAttacker(this.defender);
        } else {
            this.binder.setRole("trower");
            this.throwQueue.addLast(this.binder);
            Player nextAttacker = this.throwQueue.pop();
            setAttacker(nextAttacker);
        }
        Player nextDefender = this.throwQueue.pop();
        setDefender(nextDefender);
        this.throwQueue.addFirst(this.attacker);
        setBinder(null);
    }
}
