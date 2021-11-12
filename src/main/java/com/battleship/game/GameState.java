package com.battleship.game;

public class GameState {
    GameField opponentGameField;
    Ships myShips;
    String Id;

    public GameState(String Id) {
        opponentGameField = new GameField();
        myShips = new Ships();
        this.Id = Id;
    }

    public Ships getMyShips() {
        return myShips;
    }

    public String getId() {
        return Id;
    }

    public GRIDSTATE shoot(Integer field){
        return myShips.shoot(field);
    }
}
