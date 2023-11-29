package org.example.controller;

import lombok.Data;
import org.example.model.Deck;
import org.example.model.Player;

import java.util.Deque;
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


    public void fillUpTheHands(Deque<Player> queue, Player defender, Deck deck) {
        for (Player thrower : queue) {
            fillUpThePlayersHand(thrower, deck);
        }
        fillUpThePlayersHand(defender, deck);
    }
}
