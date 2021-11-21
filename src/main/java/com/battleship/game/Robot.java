package com.battleship.game;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Robot class for single player game
 */
@NoArgsConstructor
public class Robot {
    /**
     * List if fields that have been already shot at
     */
    private final HashMap<Integer, GRIDSTATE> responses = new HashMap<>();
    private final Random random = new Random();
    private final String Id = "robot";
    private Game game = null;

    public Robot(Game game){
        this.game = game;
    }

    /**
     * Shoot at a random field and save the response
     */
    public void shoot(){
        Integer field = nextFieldToBeShotAt();
        GRIDSTATE response = game.shoot(Id, field).field[field];
        responses.put(field, response);
    }

    /**
     * Calculate field to be shot at based on previous shots
     * @return next field
     */
    private Integer nextFieldToBeShotAt(){
        int field = random.nextInt(100);

        while(responses.containsKey(field)){
            field = random.nextInt(100);
        }
        return field;
    }

    /**
     * Get the Id of the Robot
     * @return Id, returns "robot"
     */
    public String getId() {
        return Id;
    }
}
