package com.github.retrooper.packetevents.protocol.scoreboard;

public enum UpdateScore {
    CREATE_OR_UPDATE_ITEM,
    REMOVE_ITEM;

    private static final UpdateScore[] VALUES = values();

    public static UpdateScore getById(int index) {
        return VALUES[index];
    }
}