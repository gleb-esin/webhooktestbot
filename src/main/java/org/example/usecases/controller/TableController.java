package org.example.usecases.controller;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.example.entities.Card;
import org.example.entities.Player;
import org.example.entities.Suit;
import org.example.entities.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides control over tables' behavior during round
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TableController {

    Table table;

    public TableController(Suit trump) {
        this.table = new Table(trump);
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