package com.battleship.game;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Game class
 */
@Getter
public class Game {
    GameLogic gameLogic;
    private final Set<String> alreadyShot = new HashSet<>();

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
     * @return resulting game field
     */
    public GameField shoot(String Id, Integer fieldId){
        if(!Id.equals("robot") && !getOtherPlayer(Id).equals("robot")){
            System.out.println("shoot::" + Id);
            synchronized (alreadyShot) {
                if(alreadyShot.contains(Id))    throw new RuntimeException("multiple.shots");
                alreadyShot.add(Id);
            }
            System.out.println("shoot::" + Id);
            while (alreadyShot.size() != 2 || !alreadyShot.contains(gameLogic.getOtherPlayer(Id))) {
                Thread.onSpinWait();
            }
            System.out.println("shoot::" + Id);
            alreadyShot.clear();
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

    /**
     * Declare themselves ready for the game
     * @param userId Id of the player
     * @return true
     */
    public boolean ready(String userId) {
        return true;
    }
}
