package org.example.BusinessLayer.controller;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.EntityLayer.Card;
import org.example.EntityLayer.Deck;
import org.example.EntityLayer.Player;

import java.util.*;

/**
 * This class provides control over players' behavior during round
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerController {
    boolean isGameOver = false;
    final List<Player> players;
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
        if (!this.throwQueue.isEmpty()) {
            throwQueue.forEach(p -> p.setRole("thrower"));
        }
        this.throwQueue.addFirst(this.attacker);
    }

    private void setPlayerMinWeight(Player player) {
        int playersMinWeight = 0;
        boolean thisCardIsTrump;
        boolean weightNotSetYet;
        boolean setWeightGreaterNewOne;
        for (Card card : player.getPlayerHand()) {
            weightNotSetYet = playersMinWeight == 0;
            setWeightGreaterNewOne = playersMinWeight > card.getWeight();
            thisCardIsTrump = card.getSuit().isTrump();

            if (thisCardIsTrump && weightNotSetYet) {
                playersMinWeight = card.getWeight();
            } else if (thisCardIsTrump && !weightNotSetYet) {
                if (setWeightGreaterNewOne) {
                    playersMinWeight = card.getWeight();
                }
            } else if (!thisCardIsTrump && weightNotSetYet) {
                playersMinWeight = card.getWeight()+1000;
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
        this.binder.setRole("thrower");
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
