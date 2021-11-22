package com.battleship.game;

import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Ships class containing the positions of the player's ships
 */
@EqualsAndHashCode
public class Ships {
    volatile ArrayList<ArrayList<Integer>>   ships;
    private final Random random = new Random();

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
        ArrayList<ArrayList<Integer>> shipsTmp = new ArrayList<>();
        List<Integer> possiblePositions = IntStream.range(1, 100).boxed().collect(Collectors.toList());
        int[] sizes = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2};

        for (int size : sizes) {
            boolean posGood = false;
            Integer pos = null;
            ArrayList<Integer> goodDirs = new ArrayList<>();
            while (!posGood) {
                pos = random.nextInt(100);
                for (int j = 0; j < size; j++) {
                    if (!possiblePositions.contains(pos + j)) break;
                    if (Math.floor((double) pos / 10) != Math.floor((double) (pos + j) / 10)) break;
                    if (j == size - 1) {
                        goodDirs.add(1);
                        posGood = true;
                    }
                }
                for (int j = 0; j < size; j++) {
                    if (!possiblePositions.contains(pos - j)) break;
                    if (Math.floor((double) pos / 10) != Math.floor((double) (pos - j) / 10)) break;
                    if (j == size - 1) {
                        goodDirs.add(-1);
                        posGood = true;
                    }
                }
                for (int j = 0; j < size; j++) {
                    if (!possiblePositions.contains(pos + (j * 10))) break;
                    if (j == size - 1) {
                        goodDirs.add(10);
                        posGood = true;
                    }
                }
                for (int j = 0; j < size; j++) {
                    if (!possiblePositions.contains(pos - (j * 10))) break;
                    if (j == size - 1) {
                        goodDirs.add(-10);
                        posGood = true;
                    }
                }
            }

            ArrayList<Integer> nextShip = new ArrayList<>();
            nextShip.add(pos);
            Collections.shuffle(goodDirs);
            for (int j = 1; j < size; j++) {
                nextShip.add(pos + goodDirs.get(0) * j);
            }
            for (Integer field : nextShip) {
                ArrayList<Integer> wholeNeighbourhood = getWholeNeighbourhood(field);
                for (Integer neighbour : wholeNeighbourhood) {
                    possiblePositions.remove(neighbour);
                }
            }
            shipsTmp.add(nextShip);
        }
        return shipsTmp;
    }

    public static void main(String[] args) {
        Ships ships = new Ships();
        GameField gameField = new GameField();
        GameLogic gameLogic = new GameLogic("1", "2");
//        gameLogic.placeShipsToField(gameField, ships);
//        System.out.println(ships);
//        System.out.println(gameField);
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

    /**
     * Unused function for determining whole neighbourhood
     * @param fieldId fieldId
     * @return neighbours
     */
    private ArrayList<Integer> getWholeNeighbourhood(Integer fieldId){
        ArrayList<Integer> neighbours = new ArrayList<>();
        // up
        if((fieldId - 10) >= 0){
            if((fieldId) % 10 != 0) neighbours.add(fieldId - 11);
            neighbours.add(fieldId - 10);
            if((fieldId) % 10 != 9)  neighbours.add(fieldId - 9);
        }
        // left
        if((fieldId) % 10 != 0) neighbours.add(fieldId - 1);
        // right
        if((fieldId) % 10 != 9) neighbours.add(fieldId + 1);
        // down
        if((fieldId + 10) < 100){
            if((fieldId) % 10 != 0) neighbours.add(fieldId + 9);
            neighbours.add(fieldId + 10);
            if((fieldId) % 10 != 9) neighbours.add(fieldId + 11);
        }
        return neighbours;
    }
}
