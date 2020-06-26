package io.github.retrooper.packetevents.enums;

public enum Direction {
    DOWN, UP, NORTH, SOUTH, WEST, EAST, NORTH_EAST,
    NORTH_WEST, SOUTH_EAST,
    SOUTH_WEST, WEST_NORTH_WEST,
    NORTH_NORTH_WEST, NORTH_NORTH_EAST,
    EAST_NORTH_EAST, EAST_SOUTH_EAST,
    SOUTH_SOUTH_EAST, SOUTH_SOUTH_WEST,
    WEST_SOUTH_WEST, SELF, NULL;

    public static Direction get(final int i) {
        return values()[i];
    }
}
