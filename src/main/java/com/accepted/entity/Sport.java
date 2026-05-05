package com.accepted.entity;

public enum Sport {
    FOOTBALL(1),
    BASKETBALL(2);

    private final int value;

    Sport(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Sport fromValue(int value) {
        for (Sport sport : Sport.values()) {
            if (sport.value == value) {
                return sport;
            }
        }
        throw new IllegalArgumentException("Unknown sport value: " + value);
    }
}
