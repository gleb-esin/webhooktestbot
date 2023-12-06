package org.example.controller;

import org.example.model.Card;
import org.example.model.Player;
import org.example.monitor.UpdateMonitor;
import org.example.network.TelegramBot;
import org.example.service.MessageHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface PlayerInputValidator extends MessageHandler, UpdateMonitor {

    default List<Card> askForCards(TelegramBot bot, Player player) {
        List<Card> cards = new ArrayList<>();
        String message;
        if (player.getRole().equals("attacker")) {
            message = player.getName() + ", введите порядковые номера карт в Вашей руке через пробел: ";
        } else {
            message = player.getName() + ", введите порядковые номера карт в Вашей руке через пробел. (Если хотите пропустить ход, напечатайте \"0\"): ";
        }
        //Отправляем запрос клиенту
        sendMessageTo(bot, player, message);
        //Получаем ответ от клиента
        String cardIndexes = getMessage(player.getChatID());
        String[] cardIndexesArr = cardIndexes.split(" ");
        Pattern pattern = Pattern.compile("^(0|[1-9]\\d*)$");
        for (String s : cardIndexesArr) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                if (Integer.parseInt(s) == 0) {
                    cards.clear();
                    return cards;
                } else {
                    boolean correctInput = Integer.parseInt(s) <= player.getPlayerHand().size() || Integer.parseInt(s) == 0;
                    if (correctInput) {
                        cards.add(player.getPlayerHand().get(Integer.parseInt(s) - 1));
                    } else {
                        while (!correctInput) {
                            //посылаем повторный запрос клиенту
                            sendMessageTo(bot, player, player.getName() + message);
                            //Получаем строку от клиента
                            cardIndexes = getMessage(player.getChatID());
                            //Формируем массив с номерами карт
                            cardIndexesArr = cardIndexes.split(" ");
                            //Для каждого элемента массива с номерами карт
                            for (String st : cardIndexesArr) {
                                //парсим строку
                                matcher = pattern.matcher(st);
                                //если матчер нашел цифры
                                if (matcher.find()) {
                                    //если это ноль, то выходим из цикла
                                    if (Integer.parseInt(st) == 0) {
                                        cards.clear();
                                        return cards;
                                    } else {
                                        //если это не ноль, то проверяем корректность ввода
                                        correctInput = Integer.parseInt(st) <= player.getPlayerHand().size();
                                        //если корректно
                                        if (correctInput) {
                                            //добавляем карту в список
                                            cards.add(player.getPlayerHand().get(Integer.parseInt(st) - 1));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Collections.sort(cards);
        return cards;
    }
}
