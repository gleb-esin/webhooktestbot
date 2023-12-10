package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class UserEntity {
    @Id
    private Long userId;
    private String name;
    private int wins = 0;
    private int games = 0;

    public UserEntity() {
    }

    public UserEntity(Long playerId, String name) {
        this.userId = playerId;
        this.name = name;
    }
}
