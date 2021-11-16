package com.battleship.game;


import lombok.Getter;

import java.util.Arrays;

@Getter
public class GameField {
    volatile GRIDSTATE[] field = new GRIDSTATE[100];

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

    public GameField() {
        Arrays.fill(field, GRIDSTATE.WATER);
    }

    public GameField(GameField gameField){
        this.field = gameField.field;
    }

    public static void main(String[] args) {
        GameField gameField = new GameField();
        System.out.println(gameField);
    }
}
