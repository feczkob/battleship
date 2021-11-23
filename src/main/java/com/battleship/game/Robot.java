package com.battleship.game;

import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
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

    public Robot(Game game){
        this.game = game;
    }

    /**
     * Shoot at a random field and save the response
     */
    public void shoot(){
        Integer field = nextFieldToBeShootAt();
        GRIDSTATE response = game.shoot(Id, field).field[field];
        switch (response){
            case MISS:
                trash.add(field);
                possibleShots.remove(field);
                removeCertainNeighboursFromPossibleShots(field);
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
                trash.addAll(Ships.getWholeNeighbourhood(field));
                addNeighboursToTrash(field);
        }

        System.out.println(field + ": " + response);
        System.out.println("possibleShots: " + possibleShots);
        System.out.println("hits: " + hits);
        System.out.println("trash: " + trash);
    }

    private void removeCertainNeighboursFromPossibleShots(Integer field) {
        HashSet<Integer> neighbours = new HashSet<>(GameLogic.getNeighbours(field));
        if(hits.stream().anyMatch(neighbours::contains)){
            Optional<Integer> hit = neighbours.stream().filter(hits::contains).findFirst();
            if(hit.isPresent()){
                if(Math.abs(field - hit.get()) == 1){
                    trash.add(field - 10);
                    trash.add(field + 10);
                } else {
                    trash.add(field - 1);
                    trash.add(field + 1);
                }
            }
        }
    }

    private void addNeighboursToTrash(Integer field) {
        ArrayList<Integer> neighbours = GameLogic.getNeighbours(field);
        for (Integer neighbour: neighbours) {
            if(hits.contains(neighbour)){
                hits.remove(neighbour);
                possibleShots.remove(neighbour);
                trash.addAll(Ships.getWholeNeighbourhood(field));
                addNeighboursToTrash(neighbour);
            }
        }
    }

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
        if(firstHit) possibleShots.addAll(neighbours);
    }

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
        Integer returnField = random.nextInt(100);
        if(!possibleShots.isEmpty()) {
            returnField = possibleShots.stream().findFirst().get();
        }
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
