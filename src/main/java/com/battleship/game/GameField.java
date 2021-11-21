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
    volatile GRIDSTATE[] field;

    /**
     * Constructor
     */
    public GameField() {
        field = new GRIDSTATE[100];
        Arrays.fill(field, GRIDSTATE.WATER);
    }

    /**
     * Constructor for deep copy
     * @param that instance to be copied
     */
    GameField(GRIDSTATE[] that){
        field = new GRIDSTATE[that.length];
        System.arraycopy(that, 0, field, 0, that.length);
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
