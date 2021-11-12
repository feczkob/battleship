package com.battleship.game;

import java.util.ArrayList;

public class GameLogic {
    GameField gameFieldPlayer1;
    Ships shipsPlayer1;
    GameField gameFieldPlayer2;
    Ships shipsPlayer2;

    public GameLogic(){
        gameFieldPlayer1 = new GameField();
        shipsPlayer1 = new Ships();
        placeShipsToField(gameFieldPlayer1, shipsPlayer1);
        gameFieldPlayer2 = new GameField();
        shipsPlayer2 = new Ships();
        placeShipsToField(gameFieldPlayer2, shipsPlayer2);
    }

    private void placeShipsToField(GameField gameField, Ships ships){
        for (ArrayList<Integer> a: ships.ships) {
            for (Integer pos: a) {
                gameField.field[pos] = GRIDSTATE.SHIP;
            }
        }
    }

    @Override
    public String toString() {
        return "Player1\n" + gameFieldPlayer1 + "\nPlayer2\n" + gameFieldPlayer2;
    }

    public static void main(String[] args) {
        GameLogic gameLogic = new GameLogic();
        System.out.println(gameLogic);
    }
}
