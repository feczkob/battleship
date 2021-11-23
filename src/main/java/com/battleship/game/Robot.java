package com.battleship.game;

import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Robot class for single player game
 */
@NoArgsConstructor
public class Robot {
    private final Random random = new Random();
    private final Set<Integer> trash = new HashSet<>();
    private final Set<Integer> hits = new HashSet<>();
    private final Set<Integer> possibleShots = new HashSet<>();
    private final String Id = "robot";
    private Game game = null;

    /**
     * Constructor
     * @param game game in which the robot plays
     */
    public Robot(Game game){
        this.game = game;
    }

    /**
     * Shoot according to previous shots
     */
    public void shoot(){
        Integer field = nextFieldToBeShootAt();
        GRIDSTATE response = game.shoot(Id, field).field[field];
        switch (response){
            case MISS:
                trash.add(field);
                possibleShots.remove(field);
                //removeCertainNeighboursFromPossibleShots(field);
                break;
            case HIT:
                hits.add(field);
                trash.add(field);
                possibleShots.remove(field);
                addNeighboursToPossibleShots(field);
                break;
            case SUNKEN:
                possibleShots.remove(field);
                trash.add(field);
                addNeighboursToTrash(field);
                hits.clear();
        }

//        System.out.println(field + ": " + response);
//        System.out.println("possibleShots: " + possibleShots);
//        System.out.println("hits: " + hits);
//        System.out.println("trash: " + trash);
    }

    private void removeCertainNeighboursFromPossibleShots(Integer field) {
        HashSet<Integer> neighbours = new HashSet<>(GameLogic.getNeighbours(field));
        if(hits.stream().anyMatch(neighbours::contains)){
            Optional<Integer> hit = neighbours.stream().filter(hits::contains).findFirst();
            if(hit.isPresent()){
                if(Math.abs(field - hit.get()) == 1){
                    trash.add(hit.get() - 10);
                    trash.add(hit.get() + 10);
                    possibleShots.remove(field - 10);
                    possibleShots.remove(field + 10);
                } else {
                    trash.add(hit.get() - 1);
                    trash.add(hit.get() + 1);
                    possibleShots.remove(field - 1);
                    possibleShots.remove(field + 1);
                }
            }
        }
    }

    /**
     * Add neighbours to trash
     * @param field field
     */
    private void addNeighboursToTrash(Integer field) {
        ArrayList<Integer> neighbours = GameLogic.getNeighbours(field);
        trash.addAll(Ships.getWholeNeighbourhood(field));
        Ships.getWholeNeighbourhood(field).forEach(possibleShots::remove);

        for (Integer neighbour: neighbours) {
            if(hits.contains(neighbour)){
                hits.remove(field);
                addNeighboursToTrash(neighbour);
            }
        }
        if(hits.stream().noneMatch(neighbours::contains))   hits.remove(field);
    }

    /**
     * Add neighbours of HIT field to possible shots
     * @param field field
     */
    private void addNeighboursToPossibleShots(Integer field) {
        ArrayList<Integer> neighbours = GameLogic.getNeighbours(field);
        boolean firstHit = true;
        for (Integer neighbour: neighbours) {
            if(hits.contains(neighbour)){
                HashSet<Integer> toBeRemoved = new HashSet<>();
                switch (field - neighbour){
                    case 1:
                        // to the left
                        if(!trash.contains(field + 1) && field % 10 != 9) possibleShots.add(field + 1);
                        if(!trash.contains(neighbour - 1) && neighbour % 10 != 0) possibleShots.add(neighbour - 1);
                        toBeRemoved = Stream.of(field + 10, field - 10, neighbour + 10, neighbour - 10)
                                        .collect(Collectors.toCollection(HashSet::new));
                        possibleShots.removeAll(toBeRemoved);
                        break;
                    case -1:
                        // to the right
                        if(!trash.contains(neighbour + 1) && neighbour % 10 != 9) possibleShots.add(neighbour + 1);
                        if(!trash.contains(field - 1) && field % 10 != 0) possibleShots.add(field - 1);
                        toBeRemoved = Stream.of(field + 10, field - 10, neighbour + 10, neighbour - 10)
                                .collect(Collectors.toCollection(HashSet::new));
                        possibleShots.removeAll(toBeRemoved);
                        break;
                    case 10:
                        // to down
                        if(!trash.contains(neighbour - 10) && neighbour - 10 >= 0) possibleShots.add(neighbour - 10);
                        if(!trash.contains(field + 10) && field + 10 < 100) possibleShots.add(field + 10);
                        deleteLeftRightNeighboursFromPossibleShots(field, neighbour, toBeRemoved);
                        possibleShots.removeAll(toBeRemoved);
                        break;
                    case -10:
                        // to up
                        if(!trash.contains(field - 10) && field - 10 >= 0) possibleShots.add(field - 10);
                        if(!trash.contains(neighbour + 10) && neighbour + 10 < 100) possibleShots.add(neighbour + 10);
                        deleteLeftRightNeighboursFromPossibleShots(field, neighbour, toBeRemoved);
                        possibleShots.removeAll(toBeRemoved);
                        break;
                }
                firstHit = false;
            }
        }
        if(firstHit) {
            for (Integer neighbour: neighbours) {
                if(!trash.contains(neighbour))  possibleShots.add(neighbour);
            }
        }
    }

    /**
     * Delete neighbours if ship is vertical
     * @param field ship field
     * @param neighbour neighbour
     * @param toBeRemoved to be removed
     */
    private void deleteLeftRightNeighboursFromPossibleShots(Integer field, Integer neighbour, HashSet<Integer> toBeRemoved) {
        if(field % 10 != 0){
            toBeRemoved.add(field - 1);
            toBeRemoved.add(neighbour - 1);
        }
        if (field % 10 != 9) {
            toBeRemoved.add(field + 1);
            toBeRemoved.add(neighbour + 1);
        }
    }

    /**
     * Calculate field to be shot at based on previous shots
     * @return next field
     */
    private Integer nextFieldToBeShootAt(){
        int returnField = random.nextInt(100);
        if(!possibleShots.isEmpty()) {
            returnField = possibleShots.stream().findFirst().get();
        }
        while(trash.contains(returnField))  returnField = random.nextInt(100);
        return returnField;
    }

    /**
     * Get the Id of the Robot
     * @return Id, returns "robot"
     */
    public String getId() {
        return Id;
    }


}
