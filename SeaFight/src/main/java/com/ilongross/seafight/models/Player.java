package com.ilongross.seafight.models;

import com.ilongross.seafight.config.GameProperties;
import com.ilongross.seafight.config.Shot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {

    private static final String PATH_PROPERTIES = "src/main/resources/application.properties";

    private final Map map = new Map(new GameProperties(PATH_PROPERTIES));
    private final String name;
    private ArrayList<Ship> ships = new ArrayList<>();
    private boolean rightToRepeatShoot = false;
    private long shotCounter = 0;
    private long successShotCounter = 0;

    private int[] previousShot = new int[2];
    int[] nextShot = new int[2];

    ArrayList<int[]> tempCellsForFutureShoots = new ArrayList<>();

    private Ship shipUnderFire = null;

    private Map enemyMap = new Map(new GameProperties(PATH_PROPERTIES));


    public Player(String name) {
        this.name = name;
    }

    public void autoInitShips() {
        map.initRandomShips();
        ships = map.getShips();
    }

    public void initPlayerShips() {
        map.initPlayerShips(ships);

    }

    public void addShip(Ship ship) {
        ships.add(ship);
    }


    public int receiveShot(int[] shot) {

        if(map.getBattleMap()[shot[0]][shot[1]] > Shot.MISS) {
            map.setCellValueAfterDamage(shot);
        }
        return shotToShips(shot);
    }

    public int[] autoShot(int shotResponse) {

        switch (shotResponse) {
            case Shot.MISS -> {
                if(tempCellsForFutureShoots.isEmpty())
                    previousShot = randomShot();
                else
                    previousShot = shotAfterDamage(0);
            }
            case Shot.DAMAGE -> previousShot = shotAfterDamage(1);
            case Shot.KILL -> {
                if(shipUnderFire == null) {
                    shipUnderFire = new Ship();
                    shipUnderFire.setSize(1);
                }
                shipUnderFire.addCell(previousShot);
                markCellsAroundShipAfterKill();
                tempCellsForFutureShoots.clear();
                previousShot = randomShot();
            }
            case Shot.WIN -> System.out.print("");
            default -> previousShot = randomShot();
        }

        return previousShot;
    }

    public int[] shot(int[] playerShot) {
        shotCounter++;
        previousShot = playerShot;
        return playerShot;
    }

    private int[] shotAfterDamage(int previousResponse) {

        Random rand = new Random();

        if(shipUnderFire == null) {
            shipUnderFire = new Ship();
            shipUnderFire.addCell(previousShot);
            shipUnderFire.setSize(1);
            addCellForFutureShots();
        }
        else
        if(shipUnderFire.getSize() == 1) {
            if(previousResponse == 1) {
                shipUnderFire.addCell(previousShot);
                shipUnderFire.setSize(2);
            }
            addCellsForFutureShotsAfterDamage();
        }
        else
        if(shipUnderFire.getSize() > 1) {

            if(previousResponse == 1) {
                shipUnderFire.addCell(previousShot);
            }
            tempCellsForFutureShoots.clear();

            int[] firstCell = getBoundaryCell("first");
            int[] lastCell = getBoundaryCell("last");
            calculateBoundaryCells(firstCell, lastCell);
            shipUnderFire.setSize(shipUnderFire.getShipCells().size());
        }

        nextShot = tempCellsForFutureShoots.get(rand.nextInt(tempCellsForFutureShoots.size()));
        tempCellsForFutureShoots.remove(nextShot);
        previousShot = nextShot;
        return nextShot;
    }

    public void shotResultHandler(int shotResult) {

        switch (shotResult) {
            case Shot.MISS -> System.out.printf("%s miss... previous shot [%d,%d]\n", name, previousShot[0], previousShot[1]);
            case Shot.DAMAGE -> System.out.printf("%s DAMAGE previous shot [%d,%d]\n", name, previousShot[0], previousShot[1]);
            case Shot.KILL -> System.out.printf("%s KILL previous shot [%d,%d]\n", name, previousShot[0], previousShot[1]);
            case Shot.WIN -> System.out.printf("%s WIN previous shot [%d,%d]\n", name, previousShot[0], previousShot[1]);
            default -> System.out.printf("%s's first shot [%d,%d]\n", name, previousShot[0], previousShot[1]);
        }

        if(shotResult < Shot.DAMAGE) {
            setRightToRepeatShoot(false);
        }
        if(shotResult >= Shot.DAMAGE) {
            setRightToRepeatShoot(true);
            successShotCounter++;
        }

    }

    private void markCellsAroundShipAfterKill() {

        int[] firstCell = getBoundaryCell("first");
        int[] lastCell = getBoundaryCell("last");

        for (int i = firstCell[0]-1; i <= lastCell[0]+1; i++) {
            if(i < 0 || i > 9) {
                continue;
            }
            for (int j = firstCell[1]-1; j <= lastCell[1]+1; j++) {
                if(j < 0 || j > 9) {
                    continue;
                }
                if(enemyMap.getBattleMap()[i][j] == Shot.NULL) {
                    int[] tempCell = {i, j};
                    markEnemyMap(tempCell, 1);
                }
            }
        }
        shipUnderFire = null;
        tempCellsForFutureShoots.clear();
    }

    private int[] getBoundaryCell(String bound) {
        int minX = shipUnderFire.getCell(0)[0];
        int minY = shipUnderFire.getCell(0)[1];
        int maxX = shipUnderFire.getCell(shipUnderFire.getSize()-1)[0];
        int maxY = shipUnderFire.getCell(shipUnderFire.getSize()-1)[1];

        for (int[] cell : shipUnderFire.getShipCells()) {
            if(cell[0] <= minX)
                minX = cell[0];
            if(cell[0] >= maxX)
                maxX = cell[0];
            if(cell[1] <= minY)
                minY = cell[1];
            if(cell[1] >= maxY)
                maxY = cell[1];
        }
        int[] result = new int[2];
        if(bound.equals("first"))
            result = new int[]{minX, minY};
        if(bound.equals("last"))
            result = new int[]{maxX, maxY};

        return result;
    }

    private int[] randomShot() {
        Random rand = new Random();
        int[] shot = new int[2];

            boolean approach = false;
            while (!approach) {
                if(!tempCellsForFutureShoots.isEmpty()) {
                    shot = tempCellsForFutureShoots.get(rand.nextInt(tempCellsForFutureShoots.size()));
                    approach = true;
                }
                else {
                    int coord1 = rand.nextInt(10);
                    int coord2 = rand.nextInt(10);
                    if (enemyMap.getBattleMap()[coord1][coord2] == Shot.NULL) {
                        shot[0] = coord1;
                        shot[1] = coord2;
                        shotCounter++;
                        approach = true;
                    }
                }
            }
        return shot;
    }


    private void addCellsForFutureShotsAfterDamage() {

        int[] firstCell = shipUnderFire.getCell(0);
        int[] secondCell = new int[2];
        if(shipUnderFire.getSize() == 1)
            secondCell = firstCell;
        else {
            secondCell = shipUnderFire.getCell(1);

            if (firstCell[0] == secondCell[0]) {
                shipUnderFire.setVertical(false);
            } else if (firstCell[1] == secondCell[1]) {
                shipUnderFire.setVertical(true);
            }
        }

        calculateBoundaryCells(firstCell, secondCell);
    }

    private void calculateBoundaryCells(int[] first, int[] second) {

        if(shipUnderFire.getSize() > 1 && shipUnderFire.isVertical() == false) {
            tempCellsForFutureShoots.clear();
            int xCoord = first[0];
            if(first[1] <= second[1]) {
                if((first[1]-1 >= 0) && (checkEnemyCells(xCoord, first[1]-1) == true))
                    addCellToTemp(xCoord, first[1]-1);
                if((second[1]+1 <= 9) && (checkEnemyCells(xCoord, second[1]+1) == true))
                    addCellToTemp(xCoord,second[1]+1);
            }
            else
            if (first[1] >= second[1]) {
                if(second[1]-1 >= 0 && (checkEnemyCells(xCoord, second[1]-1) == true))
                    addCellToTemp(xCoord, second[1]-1);
                if(first[1]+1 <= 9 && (checkEnemyCells(xCoord, first[1]+1) == true))
                    addCellToTemp(xCoord,first[1]+1);
            }
        }
        else
        if(shipUnderFire.getSize() > 1 && shipUnderFire.isVertical() == true) {
            tempCellsForFutureShoots.clear();
            int yCoord = first[1];
            if(first[0] <= second[0]) {
                if(first[0]-1 >= 0 && (checkEnemyCells(first[0]-1, yCoord) == true))
                    addCellToTemp(first[0]-1, yCoord);
                if(second[0]+1 <= 9 && (checkEnemyCells(second[0]+1, yCoord) == true))
                    addCellToTemp(second[0]+1, yCoord);
            }
            else
            if(first[0] >= second[0]) {
                if(second[0]-1 >= 0 && (checkEnemyCells(second[0]-1, yCoord) == true))
                    addCellToTemp(second[0] - 1, yCoord);
                if(first[0]+1 <= 9 && (checkEnemyCells(first[0]+1, yCoord) == true))
                    addCellToTemp(first[0] + 1, yCoord);
            }
        }
    }

    private void addCellForFutureShots() {
        for (int i = previousShot[0]-1; i <= previousShot[0]+1; i++) {
            if(i < 0 || i > 9) {
                continue;
            }
            for (int j = previousShot[1]-1; j <= previousShot[1]+1; j++) {
                if(j < 0 || j > 9) {
                    continue;
                }
                int[] tempCell = {i, j};

                if( i == previousShot[0]-1 && j == previousShot[1]-1 ||
                        i == previousShot[0]-1 && j == previousShot[1]+1 ||
                        i == previousShot[0]+1 && j == previousShot[1]-1 ||
                        i == previousShot[0]+1 && j == previousShot[1]+1
                )
                    continue;

                if(tempCell[0] == previousShot[0] && tempCell[1] == previousShot[1])
                    continue;

                addCellToTemp(i, j);
            }
        }
    }

    private boolean checkEnemyCells(int x, int y) {
        if(enemyMap.getBattleMap()[x][y] == 0)
            return true;
        return false;
    }


    private int shotToShips(int[] shot) {

        for (Ship ship : ships) {
            for (int[] cell: ship.getShipCells()) {
                if((cell[0] == shot[0]) && (cell[1] == shot[1])) {
                    ship.removeCell(cell);
                    if (ship.isEmpty()) {
                        ships.remove(ship);
                        if(ships.isEmpty()) {
                            return Shot.WIN;
                        }
                        return Shot.KILL;
                    }
                    return Shot.DAMAGE;
                }
            }
        }
        return Shot.MISS;
    }


    public void markEnemyMap(int[] shot, int result) {
        enemyMap.markCellAfterShot(shot, result);
    }


    private void addCellToTemp(int x, int y) {
        int[] cell = {x, y};

        if(enemyMap.getBattleMap()[x][y] > Shot.NULL)
            return;

        tempCellsForFutureShoots.add(cell);
    }



    public boolean isAlive() {
        for (int i = 0; i < map.getMapSize(); i++) {
            for (int j = 0; j < map.getMapSize(); j++) {
                if(map.getBattleMap()[i][j] > Shot.MISS)
                    return true;
            }
        }
        return false;
    }
    private void showCell(String cellName, int[] cell) {
        System.out.printf("%s's %s=[%d,%d]\n", name, cellName, cell[0], cell[1]);
    }
    private void showShipUnderFire(String description) {
        System.out.printf("%s: %s\n", description, shipUnderFire);
    }
    private void showCells(String description, List<int[]> cells) {
        System.out.printf("%s: ", description);
        for (int[] cell : cells) {
            System.out.printf("[%d,%d]", cell[0], cell[1]);
        }
        System.out.println();
    }
    public void showShips() {
        System.out.println(name + "'s  Flotilia:");
        if(ships.isEmpty()) {
            System.out.println("ALL ships is DEAD!\n");
            return;
        }
        map.showShips();
        System.out.println();
    }
    public void showMap() {
        System.out.println("Player: " + name);
        map.showBattleMap();
    }
    public void showEnemyMap() {
        System.out.printf("%s's enemy map:\n", name);
        enemyMap.showBattleMap();
    }


    public String getName() {
        return name;
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }

    public boolean isRightToRepeatShoot() {
        return rightToRepeatShoot;
    }
    public void setRightToRepeatShoot(boolean rightToRepeatShoot) {
        this.rightToRepeatShoot = rightToRepeatShoot;
    }
    public long getShotCounter() {
        return shotCounter;
    }
    public long getSuccessShotCounter() {
        return successShotCounter;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                '}';
    }
}
