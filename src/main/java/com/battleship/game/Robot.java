package com.battleship.game;

import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Robot class for single player game
 */
@NoArgsConstructor
public class Robot {
    /**
     * List if fields that have been already shot at
     */
    private final Map<Integer, GRIDSTATE> responses = new ConcurrentHashMap<>();
    private final Random random = new Random();
    private final Set<Integer> trash = ConcurrentHashMap.newKeySet();
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
        System.out.println(field + ": " + response);
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
                ArrayList<Integer> toBeRemoved = new ArrayList<>();
                neighbours.forEach(neighbour -> {
                    if(responses.containsKey(neighbour)) toBeRemoved.add(neighbour);
                });
                toBeRemoved.forEach(neighbours::remove);
                if(!neighbours.isEmpty()) returnField.set(neighbours.get(0));
            }
        });

        while(responses.containsKey(returnField.get()) || trash.contains(returnField.get())){
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
