package com.battleship.game;

import java.util.ArrayList;
import java.util.Random;

public class Robot {
    private static final ArrayList<Integer> fields = new ArrayList<>();
    private static final Random random = new Random();
    private static final String Id = "robot";
    public static Integer shoot(){
        Integer field = random.nextInt(100);

        while(fields.contains(field)){
            field = random.nextInt(100);
        }
        fields.add(field);
        return field;
    }

    public Robot() {
    }

    public String getId() {
        return Id;
    }
}
