package org.example.move;

import org.example.model.Card;
import org.example.model.Player;
import org.example.network.TelegramBot;
import org.example.service.MessageService;
import org.example.service.PlayerInputValidator;

import java.util.ArrayList;
import java.util.List;

public interface Move extends PlayerInputValidator, MessageService {
    default List<Card> askForCards(Player player, TelegramBot bot) {
        sendMessageTo(player, "Введите порядковые номера карт в Вашей руке через пробел:", bot);
        String cardIndexes = receiveMessageFrom(player, bot);
        List<Integer> cardIndexesList = parseCardIndexesStringToPlayerHandIndexes(cardIndexes);
        boolean correctInput = validatePlayerHandIndexes(cardIndexesList, player);
        while (!correctInput) {
            sendMessageTo(player, "Неверный ввод. Попробуйте ещё раз:", bot);
            cardIndexes = receiveMessageFrom(player, bot);
            cardIndexesList = parseCardIndexesStringToPlayerHandIndexes(cardIndexes);
            correctInput = validatePlayerHandIndexes(cardIndexesList, player);
        }
        List<Card> cards = new ArrayList<>();
        for (Integer cardIndex : cardIndexesList) {
            cards.add(player.getPlayerHand().get(cardIndex - 1));
        }
        return cards;
    }
}
