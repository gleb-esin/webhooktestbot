package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserEntity {
    @Id
    Long userId;
    String name;
    int games;
    int wins;

    public UserEntity(Long playerId, String name) {
        this.userId = playerId;
        this.name = name;
        this.games = 0;
        this.wins = 0;
    }

    public UserEntity(Player player) {
        this.userId = player.getChatID();
        this.name = player.getName();
        this.games = player.getGames();
        this.wins = player.getWins();
    }

    public UserEntity(Long chatID, String name, int games, int wins) {
        this.userId = chatID;
        this.name = name;
        this.games = games;
        this.wins = wins;

    }

}
