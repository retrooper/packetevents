package com.github.retrooper.packetevents.protocol.scoreboard;

public enum TeamMode {
    CREATE,
    REMOVE,
    UPDATE,
    ADD_ENTITIES,
    REMOVE_ENTITIES;

    private static final TeamMode[] VALUES = values();

    public static TeamMode getById(int index) {
        return VALUES[index];
    }
}