package com.battleship.game;


import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;

/**
 * Game field class containing the 10x10 grids and their states
 */
@Getter
@EqualsAndHashCode
public class GameField {
    volatile GRIDSTATE[] field = new GRIDSTATE[100];

    /**
     * Constructor
     */
    public GameField() {
        Arrays.fill(field, GRIDSTATE.WATER);
    }

    /**
     * Copy constructor
     * @param gameField game field to be copied
     */
    public GameField(GameField gameField){
        this.field = gameField.field;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("__________________________________________\n");
        for (int i = 0; i < field.length; i++) {
            stringBuilder.append(" | ").append(field[i].getValue());
            if(i % 10 == 9) {
                stringBuilder.append(" |\n");
                stringBuilder.append("------------------------------------------\n");
            }
        }
        return stringBuilder.toString();
    }

}
