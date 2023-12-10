package org.example.service;

import org.example.state.ThrowInFool;

public class GameFactory {

    public void createGame() {
        //fixme DEBUG
        System.out.println("DEBUG: GameFactory starts");
        new Thread(() -> {
            //fixme DEBUG
            System.out.println("DEBUG: GameFactory.createGame()");
            new ThrowInFool().play();
        }).start();
    }
}
