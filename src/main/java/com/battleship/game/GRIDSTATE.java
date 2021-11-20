package com.battleship.game;

import lombok.Getter;

/**
 * Grid state enum for differentiating the possible states of the fields
 */
@Getter
public enum GRIDSTATE {
    WATER(0),
    SHIP(1),
    HIT(2),
    MISS(3),
    SUNKEN(4);

    private final int value;

    /**
     * Constructor
     * @param value value to be used
     */
    GRIDSTATE(int value){
        this.value = value;
    }

    GRIDSTATE(GRIDSTATE that){
        this(that.value);
    }

}
