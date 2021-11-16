package com.battleship.game;

import java.util.ArrayList;
import java.util.Arrays;

public class GameLogic {
    private final GameState[] gameStates;
    private boolean isFinished;
    private String winner;

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

    public boolean getIsFinished() {
        return isFinished;
    }

    GameField shoot(String Id, Integer fieldId){
        GRIDSTATE response;
        // opponent's gameField in my perspective
        GameField gameField;
        if(Id.equals(gameStates[0].Id)){
            response = gameStates[1].shoot(fieldId);
            isFinished = gameStates[1].myShips.getIsFinished();
            gameStates[0].opponentGameField.field[fieldId] = response;
            gameField = gameStates[0].opponentGameField;
        } else {
            response = gameStates[0].shoot(fieldId);
            isFinished = gameStates[0].myShips.getIsFinished();
            gameStates[1].opponentGameField.field[fieldId] = response;
            gameField = gameStates[1].opponentGameField;
        }
        if(response == GRIDSTATE.SUNKEN)    changeHitToSunken(gameField);
        if(isFinished) winner = Id;
        return gameField;
    }

    private void changeHitToSunken(GameField gameField) {
        //TODO
    }

    public GameField getGameField(String id) {
        GameField myGameField;
        if(gameStates[0].Id.equals(id)) {
            myGameField = placeShipsToField(new GameField(gameStates[1].opponentGameField), gameStates[0].myShips);
        } else myGameField = placeShipsToField(new GameField(gameStates[0].opponentGameField), gameStates[1].myShips);

        return myGameField;
    }

    GameField myPerspective(String Id){
        GameField myGameField;
        if(Id.equals(gameStates[0].Id)){
            myGameField = new GameField(gameStates[1].opponentGameField);
            return placeShipsToField(myGameField, gameStates[0].myShips);
        } else {
            myGameField = new GameField(gameStates[0].opponentGameField);
            return placeShipsToField(myGameField, gameStates[1].myShips);
        }
    }

    @Override
    public String toString() {
        return "GameLogic{" +
                "gameStates=" + Arrays.toString(gameStates) +
                '}';
    }

    public static void main(String[] args) {
        GameLogic gameLogic = new GameLogic("player1", "player2");
        System.out.println(gameLogic);
    }

    public String getOtherPlayer(String Id) {
        if(gameStates[0].Id.equals(Id)) return gameStates[1].Id;

        return gameStates[0].Id;
    }

    public String getWinner() {
        return winner;
    }
}
