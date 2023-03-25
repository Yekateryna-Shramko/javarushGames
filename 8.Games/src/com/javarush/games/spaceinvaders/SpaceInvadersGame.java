package com.javarush.games.spaceinvaders;

import com.javarush.engine.cell.*;
import com.javarush.games.spaceinvaders.gameobjects.Bullet;
import com.javarush.games.spaceinvaders.gameobjects.EnemyFleet;
import com.javarush.games.spaceinvaders.gameobjects.PlayerShip;
import com.javarush.games.spaceinvaders.gameobjects.Star;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SpaceInvadersGame extends Game {
    public final static int WIDTH = 64;
    public final static int HEIGHT = 64;
    private List<Star> stars;
    private EnemyFleet enemyFleet;
    public static final int COMPLEXITY = 5;
    private List<Bullet> enemyBullets;
    private PlayerShip playerShip;
    private boolean isGameStopped;
    private int animationsCount;
    private List<Bullet> playerBullets;
    private  final static int PLAYER_BULLETS_MAX = 1;
    private int score ;


    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);

        createGame();
    }

    private void createGame() {
        createStars();
        score = 0;
        enemyBullets = new ArrayList<Bullet>();
        playerBullets = new ArrayList<Bullet>();
        enemyFleet = new EnemyFleet();
        playerShip = new PlayerShip();
        animationsCount = 0;
        animationsCount = 0;
        isGameStopped = false;
        drawScene();
        setTurnTimer(40);
    }

    private void drawScene() {
        drawField();
        playerShip.draw(this);
        enemyBullets.forEach(bullet -> bullet.draw(this));
        enemyFleet.draw(this);
        playerBullets.forEach(bullet -> bullet.draw(this));

    }

    private void drawField() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < HEIGHT; x++) {
                setCellValueEx(x, y, Color.DARKBLUE, "");
            }
        }
        for (Star star : stars) {
            star.draw(this);
        }

    }

    private void createStars() {
        stars = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            stars.add(createStar());
        }
    }

    private Star createStar() {
        if (stars.isEmpty()) {
            return new Star(ThreadLocalRandom.current().nextInt(0, WIDTH), ThreadLocalRandom.current().nextInt(0, HEIGHT));
        }
        Star testStar = new Star(ThreadLocalRandom.current().nextInt(0, WIDTH), ThreadLocalRandom.current().nextInt(0, HEIGHT));
        for (Star star : stars) {
            if (star.x == testStar.x && star.y == testStar.y) {
                createStar();
            }
        }
        return testStar;
    }

    public void onTurn(int i) {
        moveSpaceObjects();
        setScore(score);
        check();
        Bullet potentialBullet = enemyFleet.fire(this);
        if (potentialBullet != null){
            enemyBullets.add(potentialBullet);
        }
        drawScene();
    }

    private void moveSpaceObjects() {
        enemyFleet.move();
        enemyBullets.forEach(Bullet::move);
        playerShip.move();
        playerBullets.forEach(Bullet::move);
    }

    private void removeDeadBullets() {
        enemyBullets.removeIf(bullet -> !bullet.isAlive || bullet.y >= HEIGHT - 1);
        playerBullets.removeIf(bullet -> !bullet.isAlive || bullet.y + bullet.height < 0);
    }
    public void setCellValueEx(int x, int y, Color color, String str){
        if ((x < 0 || x >= WIDTH) || (y < 0 || y >= HEIGHT)){
            return;
        }
        super.setCellValueEx(x, y, color, str);
    }

    private void check(){
        playerShip.verifyHit(enemyBullets);
        score = score + enemyFleet.verifyHit(playerBullets);
        enemyFleet.deleteHiddenShips();
        removeDeadBullets();
        if (enemyFleet.getShipsCount() == 0){
            playerShip.win();
            stopGameWithDelay();
        }
        if (enemyFleet.getBottomBorder() >= playerShip.y){
            playerShip.kill();
        }
        if (!playerShip.isAlive){
            stopGameWithDelay();
        }


    }



    private void stopGame(boolean isWin){
        isGameStopped = true;
        stopTurnTimer();
        if (isWin){
            showMessageDialog(Color.BLACK, "You won", Color.GREEN, 50 );
        }
        else {
            showMessageDialog(Color.BLACK, "Game over", Color.RED, 50);
        }
    }

    private void stopGameWithDelay(){
        animationsCount ++;
        if (animationsCount >= 10){
            stopGame(playerShip.isAlive);
        }

    }

    @Override
    public void onKeyPress(Key key){
        if (key == Key.SPACE && isGameStopped){
            createGame();
            return;
        }
        if (key == Key.RIGHT){
            playerShip.setDirection(Direction.RIGHT);
        }
        if (key == Key.LEFT){
            playerShip.setDirection(Direction.LEFT);
        }
        if (key == Key.SPACE){
           Bullet potentialPlayerBullet = playerShip.fire();
           if (potentialPlayerBullet != null && playerBullets.size() < PLAYER_BULLETS_MAX){
               playerBullets.add(potentialPlayerBullet);
           }
        }
    }

    @Override
    public void onKeyReleased(Key key){
        if (key == Key.LEFT && playerShip.getDirection() == Direction.LEFT){
            playerShip.setDirection(Direction.UP);
        }
        if (key == Key.RIGHT && playerShip.getDirection() == Direction.RIGHT){
            playerShip.setDirection(Direction.UP);
        }
    }


}