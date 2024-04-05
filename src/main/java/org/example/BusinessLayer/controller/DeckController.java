package org.example.BusinessLayer.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.example.EntityLayer.Deck;
import org.example.EntityLayer.Player;
import org.example.EntityLayer.Suit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class provides control over deck's behavior during round
 */
@Component
@Lazy
@Scope("prototype")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeckController {
    @Getter
    Deck deck;
    int MAX_PLAYERS_HAND_SIZE = 6;

    /**
     * Alternately fills up the player's hand up to MAX_PLAYERS_HAND_SIZE with cards from the deck.
     *
     * @param  player   the player whose hand will be filled
     */
    public void fillUpThePlayersHand(Player player) {
        int playerCardGap = MAX_PLAYERS_HAND_SIZE - player.getPlayerHand().size();
        if (playerCardGap > deck.getDeckSize()) playerCardGap = deck.getDeckSize();
        if (!deck.isEmpty()) {
            if (playerCardGap > 0) {
                for (int i = 0; i < playerCardGap; i++) {
                    player.getPlayerHand().add(deck.getNextCard());
                }
            }
        }
    }
    /**
     * Fills up the players hands evenly with cards up to MAX_PLAYERS_HAND_SIZE from the deck when deck size is less than cards needed to players.
     *
     * @param  throwQueueCopy   deque contains attacker, throwers and defender
     */
    private void fillUpTheHandsForTheLastTime(Deque<Player> throwQueueCopy) {
        Player nextPlayer;
        Iterator<Player> iterator = throwQueueCopy.iterator();
        int deckSize = deck.getDeckSize();
        for (int i = 0; i < deckSize; i++) {
            if (!iterator.hasNext()) {
                iterator = throwQueueCopy.iterator();
            }
            nextPlayer = iterator.next();
            nextPlayer.getPlayerHand().add(deck.getNextCard());
        }
    }
    /**
     * Decides how to fill up the players hands depending on whether the deck have enough cards or not.
     *
     * @param  throwQueue   the queue of attacker and throwers
     * @param  defender     defender
     */
    public void fillUpTheHands(Deque<Player> throwQueue, Player defender) {
        Deque<Player> throwQueueCopy = new LinkedList<>(throwQueue);
        throwQueueCopy.addLast(defender);
        int cardsNeeded = 0;
        for (Player p : throwQueueCopy) {
            cardsNeeded += (MAX_PLAYERS_HAND_SIZE - p.getPlayerHand().size());
        }
        boolean isLastDeal = deck.getDeckSize() < cardsNeeded;
        if (isLastDeal) {
            fillUpTheHandsForTheLastTime(throwQueueCopy);
        } else {
            for (Player player : throwQueueCopy) {
                fillUpThePlayersHand(player);
            }
        }
    }
    public Suit getTrump() {
        return deck.getTrump();
    }
}
