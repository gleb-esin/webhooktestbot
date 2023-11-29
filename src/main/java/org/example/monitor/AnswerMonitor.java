package org.example.monitor;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class AnswerMonitor {
    public static ConcurrentHashMap<Long, LinkedList<SendMessage>> answers = new ConcurrentHashMap<>();

    public static void add(Long chatId, SendMessage answer) {
        if (answers.containsKey(chatId)) {
            answers.get(chatId).add(answer);
        } else {
            LinkedList<SendMessage> list = new LinkedList<>();
            list.add(answer);
            answers.put(chatId, list);
        }
    }

    public static SendMessage get(Long chatId) {
        return answers.get(chatId).poll();
    }
}
