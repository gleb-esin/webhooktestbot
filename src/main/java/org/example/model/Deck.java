package org.example.model;

import lombok.Data;

import java.util.*;

@Data
public class Deck implements Iterator<Card> {
    private List<Card> deck;
    private UUID gameId;
    private int iteratorIndex = 0;
    private Suit trump;


    public Deck(UUID gameId) {
        String[] suitArr = {"♠","♣","♥","♦"};
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
        Card card = this.deck.get(0);
        this.deck.remove(0);
        return card;
    }

    public boolean hasNext() {
        return iteratorIndex < deck.size();
    }

    @Override
    public Card next() {
        Card nextCard = deck.get(iteratorIndex);
        iteratorIndex++;
        return nextCard;
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }
}
