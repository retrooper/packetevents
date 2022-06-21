package com.github.retrooper.packetevents.protocol.entity;

public enum InteractAction {
    INTERACT,
    ATTACK,
    INTERACT_AT;

    private static final InteractAction[] VALUES = values();

    public static InteractAction getById(int index) {
        return VALUES[index];
    }
}