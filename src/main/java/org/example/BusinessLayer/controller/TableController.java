package org.example.BusinessLayer.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.example.EntityLayer.Card;
import org.example.EntityLayer.Player;
import org.example.EntityLayer.Suit;
import org.example.EntityLayer.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides control over tables' behavior during round
 */
@Component
@Lazy
@Scope("prototype")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TableController {
    Table table;

    public void setTrump(Suit trump) {
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

    /**
     * Adds player cards to the desired slot based on the player's role.
     *
     * @param  playerCards  the list of cards to be added to the table
     * @param  player       the player adding the cards to the table
     */
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