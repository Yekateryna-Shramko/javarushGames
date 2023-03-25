package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;


import java.util.ArrayList;
import java.util.List;

public class EnemyFleet {

    private static final int ROWS_COUNT = 3;
    private static final int COLUMNS_COUNT = 10;
    private static final int STEP = ShapeMatrix.ENEMY.length + 1;
    private List<EnemyShip> ships;
    private Direction direction = Direction.RIGHT;


    private void createShips(){
        ships = new ArrayList<>();
        ships.add(new Boss((double) STEP * COLUMNS_COUNT / 2 - (double)ShapeMatrix.BOSS_ANIMATION_FIRST.length / 2 - 1 , 5));
        for (int y = 0; y < ROWS_COUNT; y++) {
            for (int x = 0; x < COLUMNS_COUNT; x++) {
                ships.add(new EnemyShip(x * STEP, y * STEP + 12 ));
            }
        }
        }
        private double getSpeed (){ return Math.min(2.0, 3.0 / ships.size());
    }

    public double getBottomBorder(){
        double maxY = Integer.MIN_VALUE;
        for (EnemyShip ship: ships) {
            maxY = Math.max(maxY, ship.y + ship.height);
        }
        return maxY;
    }

    public int getShipsCount(){
        return ships.size();
    }

    public void move() {
        if (ships.isEmpty()) {
            return;
        }

        Direction currentDirection = direction;
        if (direction == Direction.LEFT && getLeftBorder() < 0) {
            direction = Direction.RIGHT;
            currentDirection = Direction.DOWN;
        } else if (direction == Direction.RIGHT && getRightBorder() > SpaceInvadersGame.WIDTH) {
            direction = Direction.LEFT;
            currentDirection = Direction.DOWN;
        }

        double speed = getSpeed();
        for (EnemyShip ship : ships) {
            ship.move(currentDirection, speed);
        }
    }


    public EnemyFleet() {
        createShips();
    }
    public void draw(Game game){
        for (EnemyShip e: ships) {
            e.draw(game);
        }
    }
    private double getRightBorder(){
        double maxX = Integer.MIN_VALUE;
        for (EnemyShip e: ships) {
            maxX = Math.max(maxX, (e.x + e.width));
        }
        return maxX;
    }
    private double getLeftBorder(){
        double minX = Integer.MAX_VALUE;
        for (EnemyShip e: ships) {
            minX = Math.min(minX, e.x);
        }
        return minX;
    }

    public Bullet fire (Game game){
        if (ships.isEmpty()){
            return null;
        }
        int Chance = game.getRandomNumber(100/SpaceInvadersGame.COMPLEXITY);
        if (Chance > 0){
            return null;
        }
        int shipThatShoots = game.getRandomNumber(ships.size());
        return ships.get(shipThatShoots).fire();
    }

    public int verifyHit(List<Bullet> bullets) {
        if (bullets.isEmpty()){
            return 0;
        }
        int score = 0;
        for (EnemyShip ship : ships) {
            if (ship.isAlive) {
                for (Bullet bullet : bullets) {
                    if (bullet.isAlive) {
                        if (ship.isCollision(bullet)) {
                           bullet.kill();
                           ship.kill();
                           score = score + ship.score;
                        }
                    }
                }
            }
        }
        return score;
    }

    public void deleteHiddenShips(){
        ships.removeIf(enemyShip -> !enemyShip.isVisible());
    }


}

