package com.battleship.game;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Ships class containing the positions of the player's ships
 */
@EqualsAndHashCode
public class Ships {
    volatile ArrayList<ArrayList<Integer>>   ships;

    /**
     * Shoot at a specific field
     * @param field field to be shot at
     * @return resulting grid state
     */
    public GRIDSTATE shoot(Integer field){
        for (ArrayList<Integer> a: ships) {
            if(a.contains(field)){
                a.remove(field);
                if(a.isEmpty()) {
                    ships.remove(a);
                    return GRIDSTATE.SUNKEN;
                }
                return GRIDSTATE.HIT;
            }
        }
        return GRIDSTATE.MISS;
    }

    /**
     * Get if the game is finished
     * @return boolean
     */
    public boolean getIsFinished(){
        return ships.isEmpty();
    }

    /**
     * Constructor
     */
    public Ships() {
        ships = new ArrayList<>();
        ships = generateShips();
    }

    /**
     * Function to generate ships at random positions
     * @return positions of ships
     */
    private ArrayList<ArrayList<Integer>> generateShips(){
        //TODO
        ArrayList<ArrayList<Integer>> shipsTmp = new ArrayList<>();

        // 5 pcs 2 sized
        shipsTmp.add(new ArrayList<>(Arrays.asList(0,1)));
//        shipsTmp.add(new ArrayList<>(Arrays.asList(3,4)));
//        shipsTmp.add(new ArrayList<>(Arrays.asList(6,7)));
//        shipsTmp.add(new ArrayList<>(Arrays.asList(20,21)));
//        shipsTmp.add(new ArrayList<>(Arrays.asList(23,24)));
//
//        // 4 pcs 3 sized
//        shipsTmp.add(new ArrayList<>(Arrays.asList(26,27,28)));
//        shipsTmp.add(new ArrayList<>(Arrays.asList(40,41,42)));
//        shipsTmp.add(new ArrayList<>(Arrays.asList(44,45,46)));
//        shipsTmp.add(new ArrayList<>(Arrays.asList(60,61,62)));
//
//        // 2 pcs 4 sized
//        shipsTmp.add(new ArrayList<>(Arrays.asList(64,65,66,67)));
//        shipsTmp.add(new ArrayList<>(Arrays.asList(80,81,82,83)));
//
//        // 1 pcs 5 sized
//        shipsTmp.add(new ArrayList<>(Arrays.asList(59,69,79,89,99)));
        return shipsTmp;
    }

    @Override
    public String toString() {
        return "Ships{" +
                "ships=" + ships +
                '}';
    }

    public void getNewShipPositions() {
        for (ArrayList<Integer> ship: ships) {
            ship.remove(0);
            ship.add(ship.get(ship.size() - 1) + 1);
        }
    }
}
