package org.example.service;

import org.example.state.ThrowInFool;

public class GameFactory {

    public void createThrowInFoolGame() {
        new Thread(() -> {
            new ThrowInFool().play();
        }).start();
    }
}
