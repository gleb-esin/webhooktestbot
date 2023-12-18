package org.example.service;

import org.example.state.Test_ThrowInFool;
import org.example.state.ThrowInFool;

public class GameFactory {

    public void createThrowInFoolGame() {
        new Thread(() -> new ThrowInFool().play()).start();
    }

    public void createTest_ThrowInFoolGame() {
        new Thread(() -> new Test_ThrowInFool().play()).start();
    }

}
