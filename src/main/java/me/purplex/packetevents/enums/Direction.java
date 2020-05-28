package me.purplex.packetevents.enums;

public enum Direction {
    DOWN, UP, NORTH, SOUTH, WEST, EAST;

    public static Direction get(final byte i) {
        return values()[i];
    }
}
