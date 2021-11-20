package com.battleship.game;

import lombok.EqualsAndHashCode;

/**
 * Game state class
 * contains the player's ships and the knowledge of the other player's field
 */
@EqualsAndHashCode
public class GameState {
    volatile GameField opponentGameField;
    volatile Ships myShips;
    volatile String Id;

    /**
     * Constructor
     * @param Id Id of the player
     */
    public GameState(String Id) {
        opponentGameField = new GameField();
        myShips = new Ships();
        this.Id = Id;
    }

//    public GameState(){
//        opponentGameField = new GameField();
//        myShips = new Ships();
//    }
//
//    void setId(String Id){
//        this.Id = Id;
//    }

    /**
     * Get the Id of the player
     * @return Id
     */
    public String getId() {
        return Id;
    }

    /**
     * Shoot at a specific field
     * @param field field to be shot at
     * @return resulting grid state
     */
    public GRIDSTATE shoot(Integer field){
        return myShips.shoot(field);
    }

    @Override
    public String toString() {
        return "GameState{" +
                "opponentGameField=" + opponentGameField +
                ", myShips=" + myShips +
                ", Id='" + Id + '\'' +
                '}';
    }
}
