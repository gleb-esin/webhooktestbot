package org.example.controller;

import lombok.Data;
import org.example.model.Card;
import org.example.model.Deck;
import org.example.model.Player;

import java.util.*;

/**
 * This class provides control over deck's behavior during round
 */
@Data
public class DeckController {
    private Deck deck;

    public DeckController(UUID gameId) {
        this.deck = new Deck(gameId);
    }

    public void fillUpThePlayersHand(Player player) {
        int playerCardGap = 6 - player.getPlayerHand().size();
        if (playerCardGap > deck.getDeck().size()) playerCardGap = deck.getDeck().size();
        if (!deck.isEmpty()) {
            if (playerCardGap > 0)
                for (int i = 0; i < playerCardGap; i++) {
                    player.getPlayerHand().add(deck.getNextCard());
                }
        }
    }

    public void fillUpTheHandsForTheLastTime(Deque<Player> throwQueue, Player defender) {
        Player nextPlayer;
        Deque<Player> throwQueueCopy = new LinkedList<>(throwQueue);
        throwQueueCopy.addLast(defender);
        Iterator<Player> iterator = throwQueueCopy.iterator();
        for (Card card : deck.getDeck()) {
            if(!iterator.hasNext()) iterator = throwQueueCopy.iterator();
            nextPlayer = iterator.next();
            nextPlayer.getPlayerHand().add(card);
        }
    }


    public void fillUpTheHands(Deque<Player> throwQueue, Player defender) {
        Deque<Player> throwQueueCopy = new LinkedList<>(throwQueue);
        throwQueueCopy.addLast(defender);
        int cardsNeeded = 0;
        for (Player p : throwQueueCopy) {
            cardsNeeded += (6 - p.getPlayerHand().size());
        }
        boolean isLastDeal = deck.getDeck().size() < cardsNeeded;
        if (isLastDeal) {
            fillUpTheHandsForTheLastTime(throwQueue, defender);
        } else {
            for (Player thrower : throwQueueCopy) {
                fillUpThePlayersHand(thrower);
            }
            fillUpThePlayersHand(defender);
        }
    }
}
