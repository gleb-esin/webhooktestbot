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
