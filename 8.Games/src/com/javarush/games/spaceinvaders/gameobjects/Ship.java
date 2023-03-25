package com.javarush.games.spaceinvaders.gameobjects;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.javarush.engine.cell.Game;



public class Ship extends GameObject{
    public boolean isAlive = true;
    private List <int[][]> frames;
    private boolean loopAnimation = false;

    private int frameIndex;
    public Ship(double x, double y) {
        super(x, y);
    }

    public void setStaticView(int[][] viewFrame){
        setMatrix(viewFrame);
        frames = new ArrayList<>();
        frames.add(viewFrame);
        frameIndex = 0;
    }
    public boolean isVisible(){
        return isAlive || frameIndex < frames.size();
    }

    public Bullet fire(){
        return null;
    }
    public void kill(){
        isAlive = false;
    }
    public void setAnimatedView(boolean isLoopAnimation, int[][]... viewFrames){
        loopAnimation = isLoopAnimation;
        setMatrix(viewFrames[0]);
        frames = Arrays.asList(viewFrames);
        frameIndex = 0;

    }

    public void nextFrame() {
        frameIndex++;

        if (frameIndex >= frames.size()) {
            if (loopAnimation) {
                frameIndex = 0;
            } else {
                return;
            }
        }

        matrix = frames.get(frameIndex);
    }
    
    @Override
    public void draw (Game game){
        super.draw(game);
        nextFrame();
    }

}
