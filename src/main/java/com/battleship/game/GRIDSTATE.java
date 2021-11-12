package com.battleship.game;

public enum GRIDSTATE {
    WATER(0),
    SHIP(1),
    HIT(2),
    MISS(3),
    SUNKEN(4);

    private final int value;
    GRIDSTATE(int value){
        this.value = value;
    }

    GRIDSTATE() {
        this.value = 0;
    }

    public int getValue() {
        return value;
    }
}
