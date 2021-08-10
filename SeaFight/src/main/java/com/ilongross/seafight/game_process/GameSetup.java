package com.ilongross.seafight.game_process;

import com.ilongross.seafight.concurrency.ScanInput;
import com.ilongross.seafight.models.Player;
import com.ilongross.seafight.models.Ship;

import java.util.ArrayList;
import java.util.Scanner;

public class GameSetup {


    public void initShips(ArrayList<Player> players) {
        for (Player player : players) {
            if(player.getName().contains("AI"))
                player.autoInitShips();
            else {
                addShips(player);
                player.initPlayerShips();
            }
        }
    }

    private void addShips(Player player) {

        for (int i = 1; i <= 10; i++) {
            if(i == 1) {
                addShip(4, player);
            }
            if(i >= 2 && i <= 3) {
                addShip(3, player);
            }
            if(i >= 4 && i <= 6) {
                addShip(2, player);
            }
            if(i >= 7) {
                addShip(1, player);
            }
        }
    }

    private void addShip(int decks, Player player) {
        player.addShip(enterCells(decks));
    }

    private Ship enterCells(int decks) {
        Ship ship = new Ship();
        ship.setSize(decks);

        System.out.printf("Enter orientation %d-decks ship (H/V): ", decks);
        String orientation = ScanInput.scan();
        if(orientation.equals("H"))
            ship.setVertical(false);
        if(orientation.equals("V"))
            ship.setVertical(true);

        System.out.printf("Enter %d ship cells:\n", decks);
        for (int i = 0; i < decks; i++) {
            ship.addCell(scanToCell());
        }
        return ship;
    }

    private int[] scanToCell() {
        String tmp = "";
        int x = 0, y = 0;
        while(true) {
            tmp = ScanInput.scan();
            if(!tmp.contains(",")) {
                System.out.println("Enter 2 coords correctly with ','");
                continue;
            }
            String[] coordArr = tmp.split(",");
            x = Integer.parseInt(coordArr[0].replaceAll("[^0-9]", ""));
            y = Integer.parseInt(coordArr[1].replaceAll("[^0-9]", ""));
            if(x < 0 || x > 9 || y < 0 || y > 9) {
                System.out.println("Enter 2 coords correctly between 0 and 9");
                continue;
            }
            return new int[]{x, y};
        }
    }


}
