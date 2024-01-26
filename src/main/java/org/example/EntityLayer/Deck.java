package org.example.EntityLayer;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Lazy
@Scope("prototype")
@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class Deck {
    List<Card> deck;
    Suit trump;

    public Deck() {
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
        Card card = deck.remove(0);
        return card;
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }

    public int getDeckSize() {
        return deck.size();
    }
}
