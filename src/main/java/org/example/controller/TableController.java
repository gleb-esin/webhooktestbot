package org.example.controller;

import lombok.Data;
import org.example.model.Card;
import org.example.model.Player;
import org.example.model.Suit;
import org.example.model.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides control over tables' behavior during round
 */
@Data
public class TableController {
    private Table table;
    private  Suit trump;
    public TableController(Suit trump) {
        table = new Table();
        table.setTrump(trump);
    }

    public void clear() {
        table.getUnbeatenCards().clear();
        table.getBeatenCards().clear();
    }

    public List<Card> getAll() {
        List<Card> allCards = new ArrayList<>(table.getBeatenCards());
        allCards.addAll(table.getUnbeatenCards());
        return allCards;
    }

    public void addCardsToTable(List<Card> playerCards, Player player) {
        if (player.getRole().equals("defender")) {
            Card unbeatenCard;
            for (int i = 0; i < playerCards.size(); i++) {
                unbeatenCard = table.getUnbeatenCards().get(i);
                table.setBeatenCard(unbeatenCard);
                table.setBeatenCard(playerCards.get(i));
                player.getPlayerHand().remove(playerCards.get(i));
            }
            table.getUnbeatenCards().clear();
        } else {
            for (Card c : playerCards) {
                table.setUnbeatenCard(c);
                player.getPlayerHand().remove(c);
            }
        }
    }
}