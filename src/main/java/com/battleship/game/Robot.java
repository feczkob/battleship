package com.battleship.game;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Random;

/**
 * Robot class for single player game
 */
@NoArgsConstructor
public class Robot {
    /**
     * List if fields that have been already shot at
     */
    private static final ArrayList<Integer> fields = new ArrayList<>();
    private static final Random random = new Random();
    private static final String Id = "robot";

    /**
     * Shoot at a random field
     * @return Id of the field
     */
    public static Integer shoot(){
        Integer field = random.nextInt(100);

        while(fields.contains(field)){
            field = random.nextInt(100);
        }
        fields.add(field);
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
