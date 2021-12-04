package com.github.retrooper.packetevents.protocol.teleport;

public enum RelativeFlags {
    X(0),
    Y(1),
    Z(2),
    YAW(3),
    PITCH(4);

    int bit;

    RelativeFlags(int bit) {
        this.bit = bit;
    }

    public boolean isSet(int flags) {
        return (flags & (1 << bit)) != 0;
    }

    public int set(boolean relative, int flags) {
        if (relative) {
            return flags | (1 << bit);
        } else {
            return flags & ~(1 << bit);
        }
    }
}