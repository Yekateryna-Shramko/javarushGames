package com.javarush.games.snake;

import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;
import com.javarush.engine.cell.*;

public class Snake {
    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final  String BODY_SIGN = "\u26AB";
    public int x;
    public int y;
    public boolean isAlive = true;
    private List<GameObject> snakeParts = new ArrayList<>();
    private Color snakeColor = Color.GREEN;
    private Direction direction = Direction.LEFT;



    public Snake(int x, int y) {
        this.x = x;
        this.y = y;
        snakeParts.add(new GameObject(x, y));
        snakeParts.add(new GameObject(x + 1, y));
        snakeParts.add(new GameObject(x + 2, y));
    }

    public void setDirection(Direction direction) {
        if ((this.direction == Direction.LEFT || this.direction == Direction.RIGHT) && snakeParts.get(0).x == snakeParts.get(1).x) {
            return;
        }
        if ((this.direction == Direction.UP || this.direction == Direction.DOWN) && snakeParts.get(0).y == snakeParts.get(1).y) {
            return;
        }

        if (direction == Direction.UP && this.direction == Direction.DOWN) {
            return;
        } else if (direction == Direction.LEFT && this.direction == Direction.RIGHT) {
            return;
        } else if (direction == Direction.RIGHT && this.direction == Direction.LEFT) {
            return;
        } else if (direction == Direction.DOWN && this.direction == Direction.UP) {
            return;
        }

        this.direction = direction;
    }
    public int getLength(){
        return snakeParts.size();
    }

    public Direction getDirection() {
        return direction;
    }

    public void move (Apple apple){
        GameObject newHead = createNewHead();
        if ((newHead.x < 0 || newHead.x >= SnakeGame.SIDE) || (newHead.y < 0 || newHead.y >= SnakeGame.SIDE)){
            isAlive = false;
            return;
        }
        if (checkCollision(newHead)){
            isAlive = false;
            return;
        }
        snakeParts.add(0,newHead);
        if (newHead.y == apple.y && newHead.x == apple.x) {
            apple.isAlive = false;
        } else {
            removeTail();
        }
    }

    public void draw(Game game){
        Color color = getColor();
        game.setCellValueEx(snakeParts.get(0).x, snakeParts.get(0).y, Color.NONE, HEAD_SIGN, color, 75);
        snakeParts.stream().skip(1).forEach(x -> game.setCellValueEx(x.x, x.y, Color.NONE, BODY_SIGN, color, 75));
    }

    private Color getColor() {
        if (!isAlive) {
            return Color.RED;
        }
        return snakeColor;
    }
    public GameObject createNewHead(){
        int headX = snakeParts.get(0).x;
        int headY = snakeParts.get(0).y;

        switch (direction){
            case UP: return new GameObject( headX , headY - 1 );
            case DOWN: return new GameObject(headX, headY + 1);
            case LEFT: return  new GameObject(headX - 1, headY);
            case RIGHT: return new GameObject(headX + 1, headY);
        }
        throw new IllegalStateException("error in code, check direction enum and switch");
    }

    public void removeTail(){
        snakeParts.remove(snakeParts.size() - 1);
    }

    public boolean checkCollision(GameObject gameObject){
        for (GameObject part: snakeParts) {
            if (gameObject.x == part.x && gameObject.y == part.y){
                return true;
            }
        }
        return false;
    }
}
