package com.battleship.game;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class implementing the logic of the game
 */
public class GameLogic {
    /**
     * Game state object for each player
     */
    private final GameState[] gameStates;

    private volatile boolean isFinished;
    private volatile String winner;

    /**
     * Constructor
     * @param player1 Id of player1
     * @param player2 Id of player2
     */
    public GameLogic(String player1, String player2){
        gameStates = new GameState[2];
        gameStates[0] = new GameState(player1);
        gameStates[1] = new GameState(player2);
    }

    /**
     * Place ships into the game field
     * @param gameField game field without ships
     * @param ships coordinates of ships
     * @return game field with ships
     */
    private GameField placeShipsToField(GameField gameField, Ships ships){
        for (ArrayList<Integer> a: ships.ships) {
            for (Integer pos: a) {
                gameField.field[pos] = GRIDSTATE.SHIP;
            }
        }
        return gameField;
    }

    /**
     * Get if the game is finished
     * @return boolean
     */
    public boolean getIsFinished() {
        return isFinished;
    }

    /**
     * Shoot at a specific field
     * @param Id Id of the player who makes the shoot
     * @param fieldId Id of their target
     * @return resulting game field
     */
    GameField shoot(String Id, Integer fieldId){
        GRIDSTATE response;
        // opponent's gameField in my perspective
        GameField gameField;
        if(Id.equals(gameStates[0].Id)){
            response = gameStates[1].shoot(fieldId);
            isFinished = gameStates[1].myShips.getIsFinished(isFinished);
            gameStates[0].opponentGameField.field[fieldId] = response;
            gameField = gameStates[0].opponentGameField;
        } else {
            response = gameStates[0].shoot(fieldId);
            isFinished = gameStates[0].myShips.getIsFinished(isFinished);
            gameStates[1].opponentGameField.field[fieldId] = response;
            gameField = gameStates[1].opponentGameField;
        }
        if(response == GRIDSTATE.SUNKEN)    changeHitToSunken(gameField);
        if(isFinished) winner = Id;
        return gameField;
    }

    /**
     * Change HIT positions to SUNKEN if the last field of a ship is hit
     * @param gameField resulting game field
     */
    private void changeHitToSunken(GameField gameField) {
        //TODO
    }

    /**
     * Get player's game field with the ships on it
     * @param Id Id of the player
     * @return resulting game field
     */
    public GameField getGameField(String Id) {
        GameField myGameField;
        if(gameStates[0].Id.equals(Id)) {
            myGameField = placeShipsToField(new GameField(gameStates[1].opponentGameField), gameStates[0].myShips);
        } else myGameField = placeShipsToField(new GameField(gameStates[0].opponentGameField), gameStates[1].myShips);

        return myGameField;
    }

    /**
     * Get opponent's game field
     * @param Id Id of the player
     * @return resulting game field
     */
    public GameField getOpponentGameField(String Id) {
        if(gameStates[0].Id.equals(Id))     return new GameField(gameStates[0].opponentGameField);

        return new GameField(gameStates[1].opponentGameField);
    }

    /**
     * Get the Id of the other player
     * @param Id Id of the player
     * @return Id of the other player
     */
    public String getOtherPlayer(String Id) {
        if(gameStates[0].Id.equals(Id)) return gameStates[1].Id;
        return gameStates[0].Id;
    }

    /**
     * Get the Id of the winner
     * @return Id
     */
    public String getWinner() {
        return winner;
    }

    @Override
    public String toString() {
        return "GameLogic{" +
                "gameStates=" + Arrays.toString(gameStates) +
                '}';
    }

    /**
     * Unused function for game field with ships on it
     * @param Id Id of the player
     * @return resulting game field
     */
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

}
