package com.battleship.game;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class implementing the logic of the game
 */
@EqualsAndHashCode
public class GameLogic {
    /**
     * Game state object for each player
     */
    private final GameState[] gameStates;

    private volatile boolean isFinished = false;
    private String winner;

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
        response = Id.equals(gameStates[0].Id) ? gameStates[1].shoot(fieldId) : gameStates[0].shoot(fieldId);
        gameField = Id.equals(gameStates[0].Id) ? gameStates[0].opponentGameField : gameStates[1].opponentGameField;
        gameField.field[fieldId] = response;
        isFinished = gameStates[0].myShips.getIsFinished() || gameStates[1].myShips.getIsFinished();
        if(response == GRIDSTATE.SUNKEN)    changeHitToSunken(fieldId, gameField);
        if(isFinished) winner = winner == null ? Id : winner;
        return gameField;
    }

    /**
     * Change HIT positions to SUNKEN if the last field of a ship is hit
     * @param gameField resulting game field
     */
    private void changeHitToSunken(Integer fieldId, GameField gameField) {
        ArrayList<Integer> neighbours = getNeighbours(fieldId);
        for (Integer field: neighbours) {
            if(gameField.field[field] == GRIDSTATE.HIT) {
                gameField.field[field] = GRIDSTATE.SUNKEN;
                changeHitToSunken(field, gameField);
            }
        }
    }

    /**
     * Get neighbourhood of field
     * @param fieldId Id of field
     * @return neighbourhood
     */
    private ArrayList<Integer> getNeighbours(Integer fieldId){
        ArrayList<Integer> neighbours = new ArrayList<>();
        // up
        if(fieldId - 10 >= 0) neighbours.add(fieldId - 10);
        // left
        if((fieldId) % 10 != 0)    neighbours.add(fieldId - 1);
        // right
        if((fieldId) % 10 != 9) neighbours.add(fieldId + 1);
        // down
        if(fieldId + 10 < 100)  neighbours.add(fieldId + 10);
        return neighbours;
    }

    /**
     * Get player's game field with the ships on it
     * @param Id Id of the player
     * @return resulting game field
     */
    public GameField getGameField(String Id) {
        return gameStates[0].Id.equals(Id) ? placeShipsToField(new GameField(gameStates[1].opponentGameField.field), gameStates[0].myShips) :
                placeShipsToField(new GameField(gameStates[0].opponentGameField.field), gameStates[1].myShips);
    }

    /**
     * Get opponent's game field
     * @param Id Id of the player
     * @return resulting game field
     */
    public GameField getOpponentGameField(String Id) {
        return gameStates[0].Id.equals(Id) ? gameStates[0].opponentGameField :
                gameStates[1].opponentGameField;
    }

    /**
     * Get the Id of the other player
     * @param Id Id of the player
     * @return Id of the other player
     */
    public String getOtherPlayer(String Id) {
        return gameStates[0].Id.equals(Id) ? gameStates[1].Id : gameStates[0].Id;
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

}
