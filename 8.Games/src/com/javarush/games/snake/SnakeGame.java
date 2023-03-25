package com.javarush.games.snake;


import com.javarush.engine.cell.*;

public class SnakeGame extends Game {
    public static final int SIDE = 15;
    public static final int WIDTH = SIDE;
    public static final int HEIGHT = SIDE;
    private Snake snake;
    private int turnDelay;
    private Apple apple;
    private static final int GOAL = 28;
    private boolean isGameStopped;
    private int score;

    @Override
    public void initialize(){
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }
    private void createGame(){
        snake = new Snake(WIDTH/2, HEIGHT/2 );
        turnDelay = 300;
        score = 0;
        setScore(score);
        createNewApple();
        isGameStopped = false;
        drawScene();
        setTurnTimer(turnDelay);


    }
    private void gameOver(){
        isGameStopped = true;
        stopTurnTimer();
        showMessageDialog(Color.BLACK, "Game Over", Color.AQUA, 50);
    }
    private void win(){
        isGameStopped = true;
        stopTurnTimer();
        showMessageDialog(Color.BLACK, "Mяу", Color.AQUA, 50);
    }
    private void drawScene(){
        for (int y = 0; y < WIDTH; y++) {
            for (int x = 0; x < HEIGHT; x++) {
                setCellValueEx(x,y, Color.YELLOW, "");

            }

        }
        snake.draw(this);
        apple.draw(this);
    }

    @Override
    public void onTurn(int step) {
        snake.move(apple);
        if (!apple.isAlive) {
            score += 5;
            createNewApple();
            setScore(score);
            turnDelay -= 10;
            setTurnTimer(turnDelay);
        }
        if (!snake.isAlive){
            gameOver();
        }
        if (snake.getLength() > GOAL){
            win();
        }
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        if (isGameStopped && key == Key.SPACE){
            createGame();
        } else if (key == Key.LEFT) {
            snake.setDirection(Direction.LEFT);
        } else if (key == Key.RIGHT) {
            snake.setDirection(Direction.RIGHT);
        } else if (key == Key.UP) {
            snake.setDirection(Direction.UP);
        } else if (key == Key.DOWN) {
            snake.setDirection(Direction.DOWN);
        }
    }

    private void createNewApple(){
        Apple _apple;
        do {
            _apple = new Apple(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));
        } while (snake.checkCollision(_apple));
        apple = _apple;
    }


}
