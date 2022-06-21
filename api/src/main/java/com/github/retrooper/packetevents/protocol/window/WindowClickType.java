package com.github.retrooper.packetevents.protocol.window;

public enum WindowClickType {
    PICKUP,
    QUICK_MOVE,
    SWAP,
    CLONE,
    THROW,
    QUICK_CRAFT,
    PICKUP_ALL;

    private static final WindowClickType[] VALUES = values();

    public static WindowClickType getById(int index) {
        return VALUES[index];
    }
}