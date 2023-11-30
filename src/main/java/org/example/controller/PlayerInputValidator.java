package org.example.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Card;
import org.example.model.Player;
import org.example.network.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@Component
@AllArgsConstructor(onConstructor_ = {@Autowired})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerInputValidator {
    TelegramBot bot;

    public PlayerInputValidator() {
    }

    protected List<Card> askForCards(Player player) {
        List<Card> cards = new ArrayList<>();
        String message;
        if (player.getRole().equals("attacker")) {
            message = player.getName() + ", введите порядковые номера карт в Вашей руке через пробел: ";
        } else {
            message = player.getName() + ", введите порядковые номера карт в Вашей руке через пробел. (Если хотите пропустить ход, напечатайте \"0\"): ";
        }
        //Отправляем запрос клиенту
        bot.sendNotificationTo(player, message);
        //Получаем ответ от клиента
        String cardIndexes = bot.receiveMessageFrom(player);
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
                            bot.sendNotificationTo(player, player.getName() + message);
                            //Получаем строку от клиента
                            cardIndexes = bot.receiveMessageFrom(player);
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
