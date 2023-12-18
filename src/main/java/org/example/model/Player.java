package org.example.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Player implements Comparable<Player> {
    final String name;
    final Long chatID;
    final List<Card> playerHand = new ArrayList<>();
    boolean isWinner = false;
    int turn;
    String role;
    Integer minTrumpWeight;
    int wins = 0;
    int games = 0;

    public Player(Long chatID, String name) {
        this.name = name;
        this.chatID = chatID;
    }

    public Player(UserEntity userEntity) {
        this.name = userEntity.getName();
        this.chatID = userEntity.getUserId();
        this.wins = userEntity.getWins();
        this.games = userEntity.getGames();
    }

    public Player(Long chatID, String name, int minTrumpWeight, int wins, int games) {
        this.name = name;
        this.chatID = chatID;
        this.minTrumpWeight = minTrumpWeight;
        this.wins = wins;
        this.games = games;

    }

    @Override
    public String toString() {
        Comparator<Card> weightComparator = (card1, card2) -> Integer.compare(card1.getWeight(), card2.getWeight());
        Collections.sort(playerHand, weightComparator);
        StringBuilder upperString = new StringBuilder();
        StringBuilder bottomString = new StringBuilder();
        upperString.append(this.name).append("\n");
        int cardNumber = 1;
        bottomString.append("   ");
        for (int i = 0; i < playerHand.size(); i++) {
            upperString.append(playerHand.get(i));
            if (playerHand.get(i).getValue().toString().equals("10")) bottomString.append("  ");
            bottomString.append(cardNumber++);
            bottomString.append(" ".repeat(8));
            if (i % 3 == 0) bottomString.append(" ");
            if (i != 0 && i % 5 == 0) {
                upperString.append("\n").append(bottomString).append("\n");
                bottomString = new StringBuilder();
                bottomString.append("   ");
            }

        }
        return upperString.append("\n").append(bottomString).toString();
    }

    @Override
    public int compareTo(Player player) {
        if (this.minTrumpWeight > player.minTrumpWeight) {
            return 1;
        } else return -1;
    }

    public UserEntity toUserEntity() {
        UserEntity userEntity = new UserEntity(this);
        return userEntity;
    }

    public String getStatistics() {
        StringBuilder winsWordForm = new StringBuilder("побед");
        StringBuilder gamesWordForm = new StringBuilder("игр");
        return "У вас " + wins + " " + wordForm(wins, winsWordForm) + " и " + games + " " + wordForm(games, gamesWordForm);
    }

    private String wordForm(int number, StringBuilder word) {
        if (number > 10 && number < 21) return word.toString();
        int lastDigit = number % 10;
        switch (lastDigit) {
            case 1:
                return word.append("а").toString();
            case 2, 4, 3:
                return word.append("ы").toString();
            default:
                return word.toString();
        }
    }

    public String getName() {
        return "<b>" + name + "</b>";
    }
}
