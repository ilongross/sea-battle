package com.ilongross.seafight.models;


import com.ilongross.seafight.config.GameProperties;
import com.ilongross.seafight.config.Shot;

import java.util.ArrayList;
import java.util.Random;

public class Map {

    private final GameProperties props;
    private final int mapSize;
    private int[][] battleMap;
    private ArrayList<Ship> ships = new ArrayList<>();

    public Map(GameProperties props) {
        this.props = props;
        this.mapSize = props.getFieldSize();
        this.battleMap = new int[mapSize][mapSize];
    }

    public void initRandomShips() {
        for (int i = 4; i >= 1; i--) {
            int shipSize = i;
            int countOfShips = props.getCountOfShips(shipSize);
            for (int j = 0; j < countOfShips; j++) {
                boolean flag= false;
                while(!flag) {
                    Random rand = new Random();
                    boolean isVertical = rand.nextBoolean();
                    int yStart = rand.nextInt(10);
                    int xStart = rand.nextInt(10);
                    if(battleMap[yStart][xStart] > 0)
                        continue;
                    int[] startCell = {yStart, xStart};
                    Ship ship = new Ship(shipSize, startCell, isVertical);
                    if(checkRandomShip(ship)) {
                        initShipCells(ship);
                        ships.add(ship);
                        flag = true;
                    }
                }
            }
        }
    }

    public void initPlayerShips(ArrayList<Ship> ships) {
        this.ships.clear();
        this.ships = ships;
        for (Ship ship : ships) {
            if(checkPlayerShip(ship, true)) {
                initShipCells(ship);
            }
        }
    }


    private boolean checkRandomShip(Ship ship) {
        boolean result = false;
        if(!ship.isVertical()) {
            int yConst = ship.getShipCells().get(0)[0];
            int xStart = ship.getShipCells().get(0)[1] + 1;
            boolean accessBorder = (xStart + ship.getSize()) < 10;
            if(!accessBorder) {
                return result;
            }
            for (int k = 1; k < ship.getSize(); k++) {
                int[] cell = {yConst, xStart++};
                ship.addCell(cell);
            }
        }

        if(ship.isVertical()) {
            int yStart = ship.getShipCells().get(0)[0] + 1;
            int xConst = ship.getShipCells().get(0)[1];
            boolean accessBorder = (yStart + ship.getSize()) < 10;
            if(!accessBorder) {
                return result;
            }
            for (int k = 1; k < ship.getSize(); k++) {
                int[] cell = {yStart++, xConst};
                ship.addCell(cell);
            }
        }
        boolean checkCellsAround = checkCellsAround(ship);

        if(checkCellsAround)
            result = true;

        return result;
    }
    private boolean checkPlayerShip(Ship ship, boolean isRealPlayer) {
        boolean result = false;
        if(!ship.isVertical()) {
            int yConst = ship.getShipCells().get(0)[0];
            int xStart = ship.getShipCells().get(0)[1];
            boolean accessBorder = (xStart + ship.getSize()) < 10;
//            if(!accessBorder) {
//                return result;
//            }
            for (int k = 1; k < ship.getSize(); k++) {
                int[] cell = {yConst, xStart++};
                if(!isRealPlayer)
                    ship.addCell(cell);
            }
        }

        if(ship.isVertical()) {
            int yStart = ship.getShipCells().get(0)[0];
            int xConst = ship.getShipCells().get(0)[1];
            boolean accessBorder = (yStart + ship.getSize()) < 10;
//            if(!accessBorder) {
//                return result;
//            }
            for (int k = 1; k < ship.getSize(); k++) {
                int[] cell = {yStart++, xConst};
                if(!isRealPlayer)
                    ship.addCell(cell);
            }
        }
        boolean checkCellsAround = checkCellsAround(ship);

        if(checkCellsAround)
            result = true;

        return result;
    }

    private boolean checkCellsAround(Ship ship) {

        for (int[] cell : ship.getShipCells()) {
            for (int i = cell[0]-1; i <= cell[0]+1; i++) {
                if(i < 0 || i > 9)
                    continue;
                for (int j = cell[1]-1; j <= cell[1]+1; j++) {
                    if(j < 0 || j > 9)
                        continue;
                    if(battleMap[i][j] > 1 ){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void initShipCells(Ship ship) {
        for (int[] cell : ship.getShipCells()) {
            for (int i = cell[0]-1; i <= cell[0]+1; i++) {
                if(i < 0 || i > 9)
                    continue;
                for (int j = cell[1]-1; j <= cell[1]+1; j++) {
                    if(j < 0 || j > 9)
                        continue;
                    battleMap[cell[0]][cell[1]] = Shot.DAMAGE;
                }
            }
        }
    }


    public void showBattleMap() {
        System.out.println("    |  0  1  2  3  4  5  6  7  8  9");
        System.out.println("____|_______________________________");
        for (int i = 0; i < mapSize; i++) {
            System.out.print(i + "   | ");
            for (int j = 0; j < mapSize; j++) {
                int value = battleMap[i][j];
                String draw = "";
                switch (value) {
                    case Shot.NULL -> draw = " . ";
                    case Shot.MISS -> draw = " - ";
                    case Shot.DAMAGE -> draw = " x ";
                    case Shot.KILL -> draw = "[o]";
                    case Shot.WIN -> draw = "{W}";
                }
                System.out.print(draw);
            }
            System.out.println();
        }
        System.out.println();
    }

    public void showShips() {
        for (Ship ship : ships) {
            System.out.println(ship);
        }
    }

    public void setCellValueAfterDamage(int[] shot) {
        battleMap[shot[0]][shot[1]] = Shot.NULL;
    }

    public void markCellAfterShot(int[] shot, int result) {
        switch (result) {
            case Shot.MISS -> battleMap[shot[0]][shot[1]] = result;
            case Shot.DAMAGE -> battleMap[shot[0]][shot[1]] = result;
            case Shot.KILL -> battleMap[shot[0]][shot[1]] = result;
            case Shot.WIN -> battleMap[shot[0]][shot[1]] = result;
        }
    }

    public int getMapSize() {
        return mapSize;
    }
    public int[][] getBattleMap() {
        return battleMap;
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }
}
