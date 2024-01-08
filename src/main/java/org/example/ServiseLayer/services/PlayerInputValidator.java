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
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerInputValidator {
    MessageService messageService;

    public List<Card> askForCards(Player player) {
        messageService.sendMessageTo(player, "Введите порядковые номера карт в Вашей руке через пробел:");
        String cardIndexes = messageService.receiveMessageFrom(player);
        List<Integer> cardIndexesList = parseCardIndexesStringToPlayerHandIndexes(cardIndexes);
        boolean correctInput = (!cardIndexesList.isEmpty() && validatePlayerHandIndexes(cardIndexesList, player));
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

    private List<Integer> parseCardIndexesStringToPlayerHandIndexes(String cardIndexes) {
        List<Integer> playerHandIndexes = new ArrayList<>();
        if (!cardIndexes.contains("0")) {
            playerHandIndexes = parseNumbers(cardIndexes);
            Collections.sort(playerHandIndexes);
        }
        return playerHandIndexes;
    }

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

    private ArrayList<Integer> parseNumbers(String input) {
        ArrayList<Integer> result = new ArrayList<>();
        Scanner scanner = new Scanner(input);

        // Используем регулярное выражение для поиска чисел
        scanner.useDelimiter("\\D+");

        while (scanner.hasNextInt()) {
            // Обработка каждого числа
            int number = scanner.nextInt();
            result.add(number);
        }

        scanner.close();
        return result;
    }
}
