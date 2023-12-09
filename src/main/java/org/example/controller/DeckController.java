package org.example.controller;

import lombok.Data;
import org.example.model.Card;
import org.example.model.Deck;
import org.example.model.Player;

import java.util.Deque;
import java.util.Iterator;
import java.util.UUID;

/**
 * This class provides control over deck's behavior during round
 */
@Data
public class DeckController {
    private Deck deck;

    public DeckController(UUID gameId) {
        this.deck = new Deck(gameId);
    }

    public void fillUpThePlayersHand(Player player, Deck deck) {
        int playerCardGap = 6 - player.getPlayerHand().size();
        if (playerCardGap > deck.getDeck().size()) playerCardGap = deck.getDeck().size();
        if (!deck.isEmpty()) {
            if (playerCardGap > 0)
                for (int i = 0; i < playerCardGap; i++) {
                    player.getPlayerHand().add(deck.getNextCard());
                }
        }
    }

    public void fillUpTheHandsForTheLastTime(Deque<Player> throwQueue, Player defender, Deck deck) {
        Player nextPlayer;
        Iterator<Player> iterator = throwQueue.descendingIterator();
        throwQueue.addLast(defender);
        for (Card card : deck.getDeck()) {
            nextPlayer = iterator.next();
            nextPlayer.getPlayerHand().add(card);
        }
    }


    public void fillUpTheHands(Deque<Player> throwQueue, Player defender, Deck deck) {
        int cardsNeeded = 0;
        for (Player p : throwQueue) {
            cardsNeeded += (6 - p.getPlayerHand().size());
        }
        boolean isLastDeal = deck.getDeck().size() < cardsNeeded;
        if (isLastDeal) {
            fillUpTheHandsForTheLastTime(throwQueue, defender, deck);
        } else {
            for (Player thrower : throwQueue) {
                fillUpThePlayersHand(thrower, deck);
            }
            fillUpThePlayersHand(defender, deck);
        }
    }
}
