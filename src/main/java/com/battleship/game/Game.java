package com.battleship.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;

/**
 * Game class
 */
@Getter
@EqualsAndHashCode
public class Game {
    GameLogic gameLogic;
    ArrayList<String> left = new ArrayList<>();

    /**
     * Constructor for multiplayer game
     * @param player1 Id of player1
     * @param player2 Id of player2
     */
    public Game(String player1, String player2){
        gameLogic = new GameLogic(player1, player2);
    }

    /**
     * Constructor for single player mode
     * @param player1 Id of player1
     */
    public Game(String player1){
        gameLogic = new GameLogic(player1, "robot");
    }

    /**
     * Get game field in the perspective of the player
     * @param Id Id of player
     * @return player's game field
     */
    public GameField getGameField(String Id){
        return gameLogic.getGameField(Id);
    }

    /**
     * Get opponent's game field in the perspective of the player
     * @param Id Id of player
     * @return opponent's game field
     */
    public GameField getOpponentGameField(String Id){
        return gameLogic.getOpponentGameField(Id);
    }

    /**
     * Shoot at a specific field
     * @param Id Id of the player
     * @param fieldId Id of the field
     * @return resulting game fields
     */
    public GameField shoot(String Id, Integer fieldId){
        return gameLogic.shoot(Id, fieldId);
    }

    /**
     * Get the Id of the other player
     * @param Id Id of the player
     * @return Id of the other player
     */
    public String getOtherPlayer(String Id) {
        return (gameLogic.getOtherPlayer(Id));
    }

    /**
     * Get if the game is ended i.e. all ships is sunken by some player
     * @return boolean
     */
    public boolean getIsFinished(){
        return gameLogic.getIsFinished();
    }

    /**
     * Get the Id of the winner
     * @return Id
     */
    public String getWinner(){
        return gameLogic.getWinner();
    }

    /**
     * Get new ship positions
     * @param Id Id of the player
     * @return new game field
     */
    public GameField getNewShipPositions(String Id){
        return gameLogic.getNewShipPositions(Id);
    }

    /**
     * Declare themselves ready for the game
     * @param userId Id of the player
     * @return initial game field
     */
    public GameField ready(String userId) {
        return gameLogic.getGameField(userId);
    }

    /**
     * Set the player who left the game
     * @param userId player who left the game
     */
    public void setLeft(String userId) {
        left.add(userId);
    }

    /**
     * Get player(s) who left the game
     * @return player(s)
     */
    public ArrayList<String> getLeft() {
        return left;
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameLogic=" + gameLogic +
                ", left=" + left +
                '}';
    }
}
