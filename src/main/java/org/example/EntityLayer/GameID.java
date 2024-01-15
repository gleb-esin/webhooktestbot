package org.example.EntityLayer;

import lombok.Getter;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Lazy
@Scope("prototype")
@Getter
public class GameID {
    UUID gameID = UUID.randomUUID();
}
