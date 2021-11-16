package com.battleship.game;

import lombok.Getter;

import java.util.List;

@Getter
public class Game {
    GameLogic gameLogic;
    private volatile Integer counter = 0;

    public Game(String player1, String player2){
        gameLogic = new GameLogic(player1, player2);
    }
    public Game(String player1){
        gameLogic = new GameLogic(player1, "robot");
    }
    public GameField getGameField(String Id){
        return gameLogic.getGameField(Id);
    }

    public GameField shoot(String Id, Integer fieldId){
        //if(!getOtherPlayer(Id).equals("robot") || !Id.equals("robot")) {
        if(!Id.equals("robot") && !getOtherPlayer(Id).equals("robot")){
            synchronized (counter) {
                counter = counter + 1;
            }
            while (counter != 2) {
                Thread.onSpinWait();
            }
        }
        System.out.println("Game::shoot");
        return gameLogic.shoot(Id, fieldId);
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameLogic=" + gameLogic +
                '}';
    }

    public String getOtherPlayer(String Id) {
        return (gameLogic.getOtherPlayer(Id));
    }

    public boolean getIsFinished(){
        return gameLogic.getIsFinished();
    }

    public String getWinner(){
        return gameLogic.getWinner();
    }
}
