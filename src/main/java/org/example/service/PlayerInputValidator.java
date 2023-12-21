package org.example.service;

import org.example.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface PlayerInputValidator {

    default List<Integer> parseCardIndexesStringToPlayerHandIndexes(String cardIndexes) {
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

    default boolean validatePlayerHandIndexes(List<Integer> playerHandIndexes, Player player) {
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
