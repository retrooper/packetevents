package io.github.retrooper.packetevents.enums.minecraft;

public enum PlayerDigType {

    START_DESTROY_BLOCK,

    ABORT_DESTROY_BLOCK,

    STOP_DESTROY_BLOCK,

    DROP_ALL_ITEMS,

    DROP_ITEM,

    RELEASE_USE_ITEM,

    SWAP_HELD_ITEMS,

    WRONG_PACKET;

    public static PlayerDigType get(final byte i) {
        return values()[i];
    }
}
