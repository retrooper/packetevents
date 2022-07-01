package com.github.retrooper.packetevents.protocol.scoreboard;

public enum TeamMode {
    ADD,
    REMOVE,
    CHANGE,
    JOIN,
    LEAVE;

    private static final TeamMode[] VALUES = values();

    public static TeamMode getById(int index) {
        return VALUES[index];
    }
}