package com.javarush.games.minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private static final int SIDE = 13;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField;
    private final static String MINE = "\uD83D\uDCA3";
    private final static String FLAG = "\uD83D\uDEA9";
    private int countFlags;
    private boolean isGameStopped;
    private int countClosedTiles = SIDE * SIDE;
    private int  score;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.AQUAMARINE, "Game Over", Color.BEIGE, 70);
    }

    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.FIREBRICK, "You Won", Color.DARKKHAKI, 70);
    }

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                setCellValue(x, y, "");

            }

        }
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                boolean isMine = getRandomNumber(10) < 1;
                if (isMine) {
                    countMinesOnField++;
                }
                countFlags = countMinesOnField;
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(x, y, Color.ORANGE);
            }
        }
        countMineNeighbors();
    }
    private void countMineNeighbors(){
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                GameObject gameObject = gameField [y][x];
                if (!gameObject.isMine){
                    for (GameObject neighbor : getNeighbors(gameObject)) {
                        if (neighbor.isMine){
                            gameObject.countMineNeighbors++;
                        }
                    }

            }
            
        }


        }
    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }
    private void openTile(int x, int y) {
        GameObject thisField = gameField[y][x];
        if (!isGameStopped && !thisField.isFlag && !thisField.isOpen){

        if (thisField.isMine) {
            setCellValueEx(x, y, Color.RED, MINE);
            gameOver();
        } else {
            setCellNumber(x, y, thisField.countMineNeighbors);
            thisField.isOpen = true;
            countClosedTiles--;
            setCellColor(x, y, Color.GREEN);
            score= score +5;
            setScore(score);
            if (countClosedTiles == countMinesOnField){
                win();
            }
            if (thisField.countMineNeighbors == 0) {
                setCellValue(x, y, "");
                for (GameObject neighbor : getNeighbors(thisField)) {
                    if (!neighbor.isOpen) {
                        openTile(neighbor.x, neighbor.y);

                    }
                }
            }
            }
        }
    }
    @Override
    public void onMouseLeftClick(int x, int y) {
        if (isGameStopped){
            restart();
        }
        else {
            openTile(x,y);
        }

    }

    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x,y);
    }

    private void markTile(int x, int y){
        GameObject thisField = gameField[y][x];
        if (!thisField.isOpen && countFlags != 0 && !isGameStopped){
            if (!thisField.isFlag){
                thisField.isFlag = true;
                countFlags--;
                setCellValue(x, y, FLAG);
                setCellColor(x, y, Color.YELLOW);
            }
            else {
                thisField.isFlag = false;
                countFlags++;
                setCellValue(x, y, "");
                setCellColor(x, y, Color.ORANGE);
            }


        }
    }
    private void restart(){
        isGameStopped = false;
        countClosedTiles = SIDE * SIDE;
        score = 0;
        setScore(score);
        countMinesOnField = 0;
        createGame();


    }
    private void help (){


    }
}

