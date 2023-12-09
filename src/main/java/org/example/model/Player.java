package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@Slf4j
public class Player implements Comparable<Player> {
    private final String name;
    private final Long chatID;
    private List<Card> playerHand = new ArrayList<>();
    private boolean isWinner = false;
    private int turn;
    private String role;
    private Integer minTrumpWeight;

    public Player(Long chatID, String name) {
        this.name = "<b>" + name + "</b>";
        this.chatID = chatID;
    }

    @Override
    public String toString() {
        Comparator<Card> weightComparator = (card1, card2) -> Integer.compare(card1.getWeight(), card2.getWeight());
        Collections.sort(playerHand, weightComparator);
        StringBuilder upperString = new StringBuilder();
        StringBuilder bottomString = new StringBuilder();
        upperString.append(this.name).append("\n");
        int cardNumber = 1;
        for (int i = 0; i < playerHand.size(); i++) {
            bottomString.append(" ".repeat(7));
            upperString.append(playerHand.get(i));
            if (i % 3 == 1) bottomString.append(" ");
            if (playerHand.get(i).getValue().equals("10")) bottomString.append("  ");
            bottomString.append(cardNumber++);
        }
        return upperString.append("\n").append(bottomString).toString();
    }

    @Override
    public int compareTo(Player player) {
        if (this.minTrumpWeight > player.minTrumpWeight) return 1;
        if (this.minTrumpWeight < player.minTrumpWeight) return -1;
        return 0;
    }
}
