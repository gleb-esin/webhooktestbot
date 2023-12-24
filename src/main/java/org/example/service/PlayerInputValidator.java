package org.example.service;

import org.example.model.Card;
import org.example.model.Player;
import org.example.network.TelegramBot;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
public class PlayerInputValidator {

    public List<Card> askForCards(Player player, TelegramBot bot) {
        bot.sendMessageTo(player, "Введите порядковые номера карт в Вашей руке через пробел:");
        String cardIndexes = bot.receiveMessageFrom(player);
        List<Integer> cardIndexesList = parseCardIndexesStringToPlayerHandIndexes(cardIndexes);
        boolean correctInput = validatePlayerHandIndexes(cardIndexesList, player);
        while (!correctInput) {
            bot.sendMessageTo(player, "Неверный ввод. Попробуйте ещё раз:");
            cardIndexes = bot.receiveMessageFrom(player);
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
        String[] cardIndexesArr = cardIndexes.split(" ");
        Pattern pattern = Pattern.compile("^(0|[1-9]\\d*)$");
        for (String s : cardIndexesArr) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                if (Integer.parseInt(s) == 0) {
                    playerHandIndexes.clear();
                    return playerHandIndexes;
                } else {
                    if (!playerHandIndexes.contains(Integer.parseInt(s))) {
                        playerHandIndexes.add(Integer.parseInt(s));
                    }
                }
            }
        }
        Collections.sort(playerHandIndexes);
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
}
