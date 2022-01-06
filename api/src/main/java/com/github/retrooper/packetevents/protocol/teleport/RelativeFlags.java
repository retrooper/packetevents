package com.github.retrooper.packetevents.protocol.teleport;

public enum RelativeFlags {
    X(0x01),
    Y(0x02),
    Z(0x04),
    YAW(0x08),
    PITCH(0x10);

    private final byte bit;

    RelativeFlags(int bit) {
        this.bit = (byte)bit;
    }

    public byte getBit() {
        return bit;
    }

    public boolean isSet(byte mask) {
        return (mask & (1 << bit)) != 0;
    }

    public byte set(byte mask, boolean relative) {
        if (relative) {
            return (byte) (mask | bit);
        } else {
            return (byte) (mask & ~bit);
        }
    }
}