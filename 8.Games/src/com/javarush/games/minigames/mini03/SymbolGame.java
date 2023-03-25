package com.javarush.games.minigames.mini03;

import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Color;
import java.awt.*;
import java.util.stream.Stream;


/* 
Простая программа
*/

public class SymbolGame extends Game {
    final String text = "JAVARUSH";
    @Override
    public void initialize(){
        setScreenSize(8, 3);

        for (int i = 0; i < text.length(); i++) {
            setCellValueEx(i, 1, Color.ORANGE, Character.toString(text.charAt(i)));

        }

    }
}
