package com.github.retrooper.packetevents.protocol.recipe;

public enum UnlockRecipesType {
    INIT,
    ADD,
    REMOVE;

    private static final UnlockRecipesType[] VALUES = values();

    public static UnlockRecipesType getById(int index) {
        return VALUES[index];
    }
}
