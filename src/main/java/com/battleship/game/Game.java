package com.battleship.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game {
    GameLogic gameLogic;
    Integer robotField = null;
    Integer myField = null;

    public Game(String player1, String player2){
        gameLogic = new GameLogic(player1, player2);
    }

    public void play(){
        while(true) {
            while (myField == null || robotField == null) {

            }
            shoot("robot", robotField);
            shoot("me", myField);
            myField = null;
            robotField = null;
            gameLogic.myPerspective("me");
        }

    }

    GRIDSTATE shoot(String Id, Integer fieldId){
        return gameLogic.shoot(Id, fieldId);
    }

    public static void main(String[] args) {
        Robot robot = new Robot();
        Game game = new Game("me", robot.getId());
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        game.play();
        while(true){
            game.robotField = Robot.shoot();
            try {
                game.myField = Integer.valueOf(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
