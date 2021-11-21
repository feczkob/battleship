package com.battleship.game;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

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
        Integer field = nextFieldToBeShootAt();
        GRIDSTATE response = game.shoot(Id, field).field[field];
        responses.put(field, response);
    }

    /**
     * Calculate field to be shot at based on previous shots
     * @return next field
     */
    private Integer nextFieldToBeShootAt(){
        AtomicInteger returnField = new AtomicInteger(random.nextInt(100));
        responses.forEach((field, response) -> {
            if(response == GRIDSTATE.HIT){
                ArrayList<Integer> neighbours = GameLogic.getNeighbours(field);
                neighbours.forEach(neighbour -> {
                    if(responses.containsKey(neighbour)) neighbours.remove(neighbour);
                });
                if(!neighbours.isEmpty()) returnField.set(neighbours.get(0));
            }
        });

        while(responses.containsKey(Integer.valueOf(String.valueOf(returnField)))){
            returnField.set(random.nextInt(100));
        }
        return returnField.get();
    }

    /**
     * Get the Id of the Robot
     * @return Id, returns "robot"
     */
    public String getId() {
        return Id;
    }


}
