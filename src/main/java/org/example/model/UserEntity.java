package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserEntity {
    @Id
    private Long userId;
    private String name;

    public UserEntity(Long playerId, String name) {
        this.userId = playerId;
        this.name = name;
    }
}
