package com.ilongross.seafight;

import com.ilongross.seafight.config.GameProperties;
import com.ilongross.seafight.models.Game;

public class Main {

    public static GameProperties properties = new GameProperties("src/main/resources/application.properties");

    public static void main(String[] args) {
        Game game = new Game();
        game.initShips();
        game.showMaps();
        game.start();
    }
}
