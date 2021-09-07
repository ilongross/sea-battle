package com.ilongross.seafight.models;

import com.ilongross.seafight.Main;
import com.ilongross.seafight.concurrency.ScanInput;
import com.ilongross.seafight.concurrency.ThreadAction;
import com.ilongross.seafight.config.Shot;
import com.ilongross.seafight.game_process.GameSetup;

import java.util.*;

public class Game {

    private final ArrayList<Player> players = new ArrayList<>();
    private boolean gameProcess = true;
    private int gameFlag = 0;
    private long totalShots = 0;
    private int shotResponse = Shot.NULL;
    private GameSetup gameSetup = new GameSetup();




    public Game() {
        Player p1 = new Player(Main.properties.getProperty("player.name.pc"));
        Player p2 = new Player(Main.properties.getProperty("player.name.user"));
        this.players.add(p1);
        this.players.add(p2);
    }

    private String scanPlayerName() {
        System.out.print("Enter your name or nickname: ");
        return ScanInput.scan();
    }

    public void initShips() {
        gameSetup.initShips(players);
    }




    public void start(){

        Random rand = new Random();
        int currentIndex = rand.nextInt(2);

        Player currentPlayer = null;
        Player successPlayerFromPreviousStep = null;

        while(gameProcess) {

            if(currentPlayer == null) {
                successPlayerFromPreviousStep = null;
                currentPlayer = players.get(currentIndex);
            }
            if((successPlayerFromPreviousStep != null) && successPlayerFromPreviousStep.isRightToRepeatShoot()) {
                currentPlayer = successPlayerFromPreviousStep;
                currentPlayer.setRightToRepeatShoot(false);
            }

            int shot[] = new int[2];


            if(currentPlayer.getName().contains("AI")) {
                shot = currentPlayer.autoShot(shotResponse);
            }
            else
                shot = currentPlayer.shot(scanShotCoords(currentPlayer.getName()));

            if(currentIndex == 0)
               shotResponse = players.get(currentIndex + 1).receiveShot(shot);
           if(currentIndex == 1)
               shotResponse = players.get(currentIndex - 1).receiveShot(shot);

            currentPlayer.shotResultHandler(shotResponse);
            currentPlayer.markEnemyMap(shot, shotResponse);

            if(shotResponse == Shot.WIN)
                break;

            if(currentPlayer.isRightToRepeatShoot()) {
                gameProcess = currentPlayer.isAlive();
                successPlayerFromPreviousStep = currentPlayer;
            }
            if (!currentPlayer.isRightToRepeatShoot()) {
                gameProcess = currentPlayer.isAlive();
                successPlayerFromPreviousStep = null;
                if(currentIndex == 0) {
                    currentIndex++;
                }
                else
                    if(currentIndex == 1) {
                        currentIndex--;
                    }
                currentPlayer = players.get(currentIndex);
            }
            gameFlag++;

            ThreadAction.sleep(100);

        }

        ThreadAction.sleep(1000);
        showEnemyMaps();
        ThreadAction.sleep(1000);
        showMaps();
        ThreadAction.sleep(1000);
        showFinalResults();
        ThreadAction.sleep(1000);
        showShips();
    }

    public int[] scanShotCoords(String name) {

        System.out.printf("%s enter coordinates for next shot: ", name);
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


    public void showFinalResults() {
        for (Player p : players) {
            totalShots += p.getShotCounter();
            System.out.printf("%s total shots: %d.\n", p.getName(), p.getShotCounter());
            System.out.printf("%s success shots: %d.\n", p.getName(), p.getSuccessShotCounter());
            ThreadAction.sleep(500);
        }
        ThreadAction.sleep(300);
        System.out.println(totalShots + " total shots.\n");
        System.out.println();
    }

    public void showMaps() {
        for (Player p : players) {
            if(p.getName().contains("AI"))
                continue;
            p.showMap();
        }
    }
    private void showEnemyMaps() {
        for (Player p : players) {
            p.showEnemyMap();
        }
    }
    public void showShips() {
        for (Player p : players) {
            p.showShips();
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    private String descriptionResultAfterShot(int shotResponse) {
        return switch (shotResponse) {
            case 0 -> "... miss";
            case 1 -> "Damage";
            case 2 -> "KILL";
            case 3 -> "LOST";
            default -> "SHOT ERROR";
        };
    }
}
