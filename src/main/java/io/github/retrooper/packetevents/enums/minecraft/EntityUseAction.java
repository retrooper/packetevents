package io.github.retrooper.packetevents.enums.minecraft;

public enum EntityUseAction {
    INTERACT, INTERACT_AT, ATTACK, INVALID;

    public static EntityUseAction get(final int i) {
        return values()[i];
    }
}
