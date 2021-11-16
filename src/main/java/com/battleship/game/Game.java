package com.battleship.game;

import lombok.Getter;

/**
 * Game class
 */
@Getter
public class Game {
    GameLogic gameLogic;
    private volatile Integer counter = 0;

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
     * @return game field
     */
    public GameField getGameField(String Id){
        return gameLogic.getGameField(Id);
    }

    /**
     * Shoot at a specific field
     * @param Id Id of the player
     * @param fieldId Id of the field
     * @return resulting game field
     */
    public GameField shoot(String Id, Integer fieldId){
        if(!Id.equals("robot") && !getOtherPlayer(Id).equals("robot")){
            synchronized (counter) {
                counter = counter + 1;
            }
            while (counter != 2) {
                Thread.onSpinWait();
            }
            counter = 0;
        }
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

    @Override
    public String toString() {
        return "Game{" +
                "gameLogic=" + gameLogic +
                '}';
    }
}
