package com.battleship.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ships {
    ArrayList<ArrayList<Integer>>   ships;

    public GRIDSTATE shoot(Integer field){
        for (ArrayList<Integer> a: ships) {
            if(a.contains(field)){
                a.remove(field);
                if(a.isEmpty()) return GRIDSTATE.SUNKEN;
                return GRIDSTATE.HIT;
            }
        }
        return GRIDSTATE.MISS;
    }

    public Ships() {
        ships = new ArrayList<>();
        ships = generateShips();
    }

    private ArrayList<ArrayList<Integer>> generateShips(){
        //TODO
        ArrayList<ArrayList<Integer>> shipsTmp = new ArrayList<>();

        // 5 pcs 2 sized
        shipsTmp.add(new ArrayList<>(Arrays.asList(0,1)));
        shipsTmp.add(new ArrayList<>(Arrays.asList(3,4)));
        shipsTmp.add(new ArrayList<>(Arrays.asList(6,7)));
        shipsTmp.add(new ArrayList<>(Arrays.asList(20,21)));
        shipsTmp.add(new ArrayList<>(Arrays.asList(23,24)));

        // 7 pcs 3 sized
        shipsTmp.add(new ArrayList<>(Arrays.asList(26,27,28)));
        shipsTmp.add(new ArrayList<>(Arrays.asList(40,41,42)));
        shipsTmp.add(new ArrayList<>(Arrays.asList(44,45,46)));
        shipsTmp.add(new ArrayList<>(Arrays.asList(60,61,62)));
        shipsTmp.add(new ArrayList<>(Arrays.asList(64,65,66)));
        shipsTmp.add(new ArrayList<>(Arrays.asList(80,81,82)));
        shipsTmp.add(new ArrayList<>(Arrays.asList(84,85,86)));

        // 2 pcs 4 sized
        //shipsTmp.add(new ArrayList<>(Arrays.asList(64,65,66,67)));
        return shipsTmp;
    }


}
