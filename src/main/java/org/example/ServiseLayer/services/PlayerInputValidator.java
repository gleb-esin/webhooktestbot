package org.example.ServiseLayer.services;

import lombok.AllArgsConstructor;
import org.example.EntityLayer.Card;
import org.example.EntityLayer.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
/**
 *Provides methods for validating player input.
 *  */
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerInputValidator {
    MessageService messageService;

    /**
     * Asks the player for the cards, parse and validate given string and returns the list of cards.
     * If the player input contains 0, returns an empty list.
     *
     * @param  player the player object
     * @return       the list of cards chosen by the player
     */
    public List<Card> askForCards(Player player) {
        if (player.getRole().equals("attacker"))  {
            messageService.sendMessageTo(player, "Введите порядковые номера карт в Вашей руке через пробел:");
        } else {
            messageService.sendMessageTo(player, "Введите порядковые номера карт в Вашей руке через пробел: " +
                    "\n(Если хотите пропустить ход введите 0)");
        }
        String cardIndexes = messageService.receiveMessageFrom(player);
        List<Integer> cardIndexesList = parseCardIndexesStringToPlayerHandIndexes(cardIndexes);
        boolean correctInput = validatePlayerHandIndexes(cardIndexesList, player);
        while (!correctInput) {
            messageService.sendMessageTo(player, "Неверный ввод. Попробуйте ещё раз:");
            cardIndexes = messageService.receiveMessageFrom(player);
            cardIndexesList = parseCardIndexesStringToPlayerHandIndexes(cardIndexes);
            correctInput = validatePlayerHandIndexes(cardIndexesList, player);
        }
        List<Card> cards = new ArrayList<>();
        for (Integer cardIndex : cardIndexesList) {
            cards.add(player.getPlayerHand().get(cardIndex - 1));
        }
        return cards;
    }

    /**
     * Parses a string of card indexes to a list of player hand indexes.
     * If the string contains 0 returns an empty list.
     *
     * @param  cardIndexes  the string containing the card indexes
     * @return              the list of player hand indexes
     */
    private List<Integer> parseCardIndexesStringToPlayerHandIndexes(String cardIndexes) {
        List<Integer> playerHandIndexes = new ArrayList<>();
        if (!cardIndexes.contains("0")) {
            playerHandIndexes = parseNumbers(cardIndexes);
            Collections.sort(playerHandIndexes);
        }
        return playerHandIndexes;
    }

    /**
     * Validates cards indexes of player's hand.
     *
     * @param  playerHandIndexes  the list of player hand indexes
     * @param  player             the player
     * @return                    true if the input is correct, false otherwise
     */

    //TODO: remove playerHandIndexes.isEmpty() and test
    private boolean validatePlayerHandIndexes(List<Integer> playerHandIndexes, Player player) {
        boolean correctInput = true;
        if (playerHandIndexes.isEmpty()) {
            correctInput = true;
        } else if (playerHandIndexes.size() > player.getPlayerHand().size()) {
            correctInput = false;
        } else {
            for (Integer index : playerHandIndexes) {
                if (index > player.getPlayerHand().size()) {
                    correctInput = false;
                    break;
                }
            }
        }
        return correctInput;
    }

    /**
     * Parses a given input string and returns a list of integers.
     *
     * @param  input  the input string to be parsed
     * @return        the list of integers parsed from the input string
     */
    private ArrayList<Integer> parseNumbers(String input) {
        ArrayList<Integer> result = new ArrayList<>();
        Scanner scanner = new Scanner(input);
        scanner.useDelimiter("\\D+");
        while (scanner.hasNextInt()) {
            int number = scanner.nextInt();
            result.add(number);
        }
        scanner.close();
        return result;
    }
}
