package com.javarush.games.game2048;

import com.javarush.engine.cell.*;

import java.util.Arrays;
import java.util.HashMap;

import java.util.Map;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private int score = 0;
    private boolean isGameStopped = false;

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                gameField[y][x] = 0;

            }

        }
        createNewNumber();
        createNewNumber();

    }
    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.PURPLE, "won", Color.BLACK, 50);

    }
    private boolean canUserMove(){
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (gameField[y][x]== 0){
                    return true;
                }

            }
        }
        boolean canMergeHorizontally = canMerge();
        rotateClockwise();
        boolean canMergeVertically = canMerge();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        return (canMergeHorizontally || canMergeVertically);


    }

    private boolean canMerge() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE - 1; x++) {
               int present = gameField[y][x];
               int next = gameField[y][x+1];
               if (next == present){
                   return true;
               }
            }

        }
        return false;
    }

    private void drawScene() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                setCellColoredNumber(x, y, gameField[y][x]);
            }

        }
    }
    private int getMaxTileValue(){
        int maxValue = Integer.MIN_VALUE;
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                maxValue = Math.max(gameField[y][x], maxValue);
            }
        }
        return maxValue;
    }

    private void createNewNumber() {
        if (getMaxTileValue() == 2048){
            win();
            return;
        }
        Cell coordinate = getNewCoordinate();
        if (gameField[coordinate.getY()][coordinate.getX()] == 0) {
            gameField[coordinate.getY()][coordinate.getX()] = getRandomNumber(10) < 9 ? 2 : 4;
        } else createNewNumber();
    }

    private Cell getNewCoordinate() {
        Cell coordinate = new Cell();
        coordinate.setX(getRandomNumber(SIDE));
        coordinate.setY(getRandomNumber(SIDE));
        return coordinate;
    }

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();

    }

    private static class Cell {
        private int x;
        private int y;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    private Color getColorByValue(int value) {
        Map<Integer, Color> colorMap = new HashMap<>();
        colorMap.put(0, Color.WHITE);
        colorMap.put(2, Color.RED);
        colorMap.put(4, Color.BISQUE);
        colorMap.put(8, Color.GRAY);
        colorMap.put(16, Color.AQUA);
        colorMap.put(32, Color.TAN);
        colorMap.put(64, Color.BLACK);
        colorMap.put(128, Color.CRIMSON);
        colorMap.put(256, Color.DARKSEAGREEN);
        colorMap.put(512, Color.LIGHTCYAN);
        colorMap.put(1024, Color.IVORY);
        colorMap.put(2048, Color.GOLD);

        return colorMap.get(value);


    }

    private void setCellColoredNumber(int x, int y, int value) {
        if (value == 0) {
            setCellValueEx(x, y, getColorByValue(value), "");
        } else setCellValueEx(x, y, getColorByValue(value), String.valueOf(value));

    }

    private boolean compressRow(int[] row) {
        int[] copy = row.clone();
        sort(row);
//        Arrays.sort(Arrays.stream(row).boxed().toArray(), (left, right) -> {
//            if (left.equals(0) && !right.equals(0)) {
//                return 1;
//            }
//            return 0;
//        });
        return !Arrays.equals(row, copy);
    }

    private static void swap(int[] row, int a, int b) {
        int temp = row[a];
        row[a] = row[b];
        row[b] = temp;
    }

    static void sort(int[] row) {
        for (int i = row.length; i >= 1; i--) {
            procedure(row, i);
        }
    }

    private static void procedure(int[] row, int rowLength) {
        for (int i = 0; i < rowLength - 1; i++) {
            int current = row[i];
            int next = row[i + 1];
            if (current == 0 && next != 0) {
                swap(row, i, i + 1);
            }
        }
    }

    private boolean mergeRow(int[] row) {
        int[] copy = row.clone();
        for (int i = 0; i < row.length - 1; i++) {
            int present = row[i];
            int next = row[i + 1];
            if (present != 0 && next == present) {
                merge(row, i, i + 1);
                score = score + row[i];
                setScore(score);
            }
        }
        return !Arrays.equals(copy, row);
    }

    private static void merge(int[] row, int a, int b) {
        row[a] = row[a] * 2;
        row[b] = 0;
    }
    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.RED, "loser", Color.BLACK, 50);
    }

    @Override
    public void onKeyPress(Key key) {
        if ((key == Key.SPACE) && isGameStopped){
            createGame();
            isGameStopped = false;
            score = 0;
            setScore(score);
            drawScene();
            return;
        }
        if (isGameStopped){
            return;
        }
        if (!canUserMove()){
            gameOver();
            return;
        }

        if (key == Key.DOWN) {
            moveDown();
            drawScene();
        }
        if (key == Key.UP) {
            moveUp();
            drawScene();
        }
        if (key == Key.RIGHT) {
            moveRight();
            drawScene();
        }
        if (key == Key.LEFT) {
            moveLeft();
            drawScene();
        }

    }

    private void moveLeft() {
        boolean somethingHappened = false;
        for (int y = 0; y < SIDE; y++) {
            if (compressRow(gameField[y])) {
                somethingHappened = true;
            }
        }
        for (int y = 0; y < SIDE; y++) {
            if (mergeRow(gameField[y])) {
                somethingHappened = true;
            }
        }
        if (somethingHappened) {
            for (int y = 0; y < SIDE; y++) {
                compressRow(gameField[y]);
            }
            createNewNumber();
        }

    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }

    private void rotateClockwise() {
       int [][] copy = new int[SIDE][SIDE];
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                copy [x][SIDE - 1 - y] = gameField [y][x];

            }

        }
        gameField = copy;

    }

    private void printGameField() {
        for (int[] row : gameField) {
            System.out.println(Arrays.toString(row));
        }
    }



    }


