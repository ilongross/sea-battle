package com.ilongross.seafight;

import com.ilongross.seafight.models.Game;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.initShips();
        game.showMaps();
        game.start();
    }
}
