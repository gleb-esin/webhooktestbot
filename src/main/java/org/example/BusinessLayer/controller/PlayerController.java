package org.example.BusinessLayer.controller;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.EntityLayer.Card;
import org.example.EntityLayer.Deck;
import org.example.EntityLayer.Player;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class provides control over players' behavior during round
 */
@Component
@Scope("prototype")
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerController {
    @Setter
    boolean isGameOver = false;
    @Setter
    List<Player> players;
    Player attacker;
    Player defender;
    @Setter
    Player binder;
    @Setter
    Player winner;
    LinkedList<Player> throwQueue;

    /**
     * Set the turn for each player based on players' minTrumpWeight.
     */
    public void setPlayersTurn() {
        for (Player player : players) {
            setPlayerMinWeight(player);
        }
        Collections.sort(players);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setTurn(i + 1);
        }
        formQueue(players);
    }

    /**
     * Forms a queue of players and sets game roles.
     *
     * @param  players   the list of players to form the queue
     */
    private void formQueue(List<Player> players) {
        this.throwQueue = new LinkedList<>(players);
        setAttacker(this.throwQueue.pop());
        setDefender(this.throwQueue.pop());
        if (!this.throwQueue.isEmpty()) {
            throwQueue.forEach(p -> p.setRole("thrower"));
        }
        this.throwQueue.addFirst(this.attacker);
    }

    /**
     * Sets the weight of minimal value trump card. If trump card is not found, add 1000 to minimum non trump card  weight.
     *
     * @param  player   the player whose minimum weight needs to be set
     */
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
            } else if (thisCardIsTrump) {
                if (setWeightGreaterNewOne) {
                    playersMinWeight = card.getWeight();
                }
            } else if (weightNotSetYet) {
                playersMinWeight = card.getWeight() + 1000;
            }
        }
        player.setMinTrumpWeight(playersMinWeight);
    }

    /**
     * Sets player as an attacker.
     *
     * @param  player   the player to set as the attacker
     */
    public void setAttacker(Player player) {
        player.setRole("attacker");
        this.attacker = player;
    }

    /**
     * Sets the player as a defender.
     *
     * @param  player  the player to set as a defender
     */
    public void setDefender(Player player) {
        player.setRole("defender");
        this.defender = player;
    }

    /**
     * Checks if the player is the winner based on the remaining cards in the player's hand and in the deck.
     * Set game state, winner and increment players wins in case of a win.
     *
     * @param  player  the player to check for winning status
     * @param  deck    the deck to check for empty status
     * @return         true if the player is the winner, false otherwise
     */
    public boolean isPlayerWinner(Player player, Deck deck) {
        boolean isWinner = deck.isEmpty() && player.getPlayerHand().isEmpty();
        if (isWinner) {
            setGameOver(true);
            setWinner(player);
            player.setWins(player.getWins() + 1);
        }
        return isWinner;
    }

    /**
     * Changes the  players' turn in the game.
     */
    public void changeTurn() {
        Player attacker = this.throwQueue.pop();
        attacker.setRole("thrower");
        this.throwQueue.addLast(attacker);
        if (this.binder == null) {
            setAttacker(this.defender);
            Player nextDefender = this.throwQueue.pop();
            setDefender(nextDefender);
            this.throwQueue.addFirst(this.attacker);
        } else if (throwQueue.size() > 1) {
            this.binder.setRole("thrower");
            this.throwQueue.addLast(this.binder);
            Player nextAttacker = this.throwQueue.pop();
            setAttacker(nextAttacker);
            Player nextDefender = this.throwQueue.pop();
            setDefender(nextDefender);
            this.throwQueue.addFirst(this.attacker);
        } else {
            setAttacker(attacker);
            setDefender(defender);
        }
        setBinder(null);
    }

    public void setThrower(Player player) {
        player.setRole("thrower");
        throwQueue.add(player);
    }
}
