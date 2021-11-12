package com.battleship.game;

import java.util.ArrayList;

public class GameLogic {
    GameState[] gameStates;

    public GameLogic(String player1, String player2){
        gameStates = new GameState[2];
        gameStates[0] = new GameState(player1);
        gameStates[1] = new GameState(player2);
    }

    private GameField placeShipsToField(GameField gameField, Ships ships){
        for (ArrayList<Integer> a: ships.ships) {
            for (Integer pos: a) {
                gameField.field[pos] = GRIDSTATE.SHIP;
            }
        }
        return gameField;
    }

    GRIDSTATE shoot(String Id, Integer fieldId){
        GRIDSTATE response;
        if(Id.equals(gameStates[0].Id)){
            response = gameStates[1].shoot(fieldId);
            gameStates[0].opponentGameField.field[fieldId] = response;
        } else {
            response = gameStates[0].shoot(fieldId);
            gameStates[1].opponentGameField.field[fieldId] = response;
        }
        return response;
    }

    String myPerspective(String Id){
        StringBuilder stringBuilder = new StringBuilder();
        GameField myGameField;
        if(Id.equals(gameStates[0].Id)){
            stringBuilder.append(gameStates[0].opponentGameField);
            myGameField = new GameField(gameStates[1].opponentGameField);
            stringBuilder.append(placeShipsToField(myGameField, gameStates[0].myShips));
        } else {
            stringBuilder.append(gameStates[1].opponentGameField);
            myGameField = new GameField(gameStates[0].opponentGameField);
            stringBuilder.append(placeShipsToField(myGameField, gameStates[1].myShips));
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "";
    }

    public static void main(String[] args) {
        GameLogic gameLogic = new GameLogic("player1", "player2");
        System.out.println(gameLogic);
    }
}
