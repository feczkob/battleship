package com.battleship.game;

import lombok.Getter;

@Getter
public class Game {
    GameLogic gameLogic;

    public Game(String player1, String player2){
        gameLogic = new GameLogic(player1, player2);
    }
    public Game(String player1){
        gameLogic = new GameLogic(player1, "robot");
    }
    public GameField getGameField(String Id){
        return gameLogic.getGameField(Id);
    }

    public GRIDSTATE shoot(String Id, Integer fieldId){
        return gameLogic.shoot(Id, fieldId);
    }

    public String getPlayer2(){
        return gameLogic.getPlayer2();
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameLogic=" + gameLogic +
                '}';
    }
}
