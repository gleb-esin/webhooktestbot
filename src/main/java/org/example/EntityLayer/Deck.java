package org.example.EntityLayer;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.*;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class Deck {
    List<Card> deck;
    UUID gameId;
    Suit trump;

    public Deck(UUID gameId) {
        String[] suitArr = {"♠", "♣", "♥", "♦"};
        String trumpSuit = suitArr[(int) (Math.random() * 4)];
        this.trump = new Suit(trumpSuit, true);
        boolean trump;
        String[] valuesArr = {"6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        List<Card> deck = new LinkedList<>();
        for (String suit : suitArr) {
            trump = suit.equals(trumpSuit);
            for (String value : valuesArr) {
                deck.add(new Card(suit, value, trump));
            }
        }
        Collections.shuffle(deck);
        this.deck = deck;
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Card card : this.deck) {
            stringBuilder.append(card).append(" ");
        }
        return stringBuilder.toString();
    }

    public Card getNextCard() {
        return deck.remove(0);
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }
}
