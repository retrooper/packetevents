package com.github.retrooper.packetevents.protocol.entity;

public enum EntityInteractAction {
    INTERACT,
    ATTACK,
    INTERACT_AT;

    private static final EntityInteractAction[] VALUES = values();

    public static EntityInteractAction getById(int index) {
        return VALUES[index];
    }
}