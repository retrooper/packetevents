package com.github.retrooper.packetevents.protocol.teleport;

import java.util.HashSet;
import java.util.Set;

public enum RelativeFlag {
    X(0x01),
    Y(0x02),
    Z(0x04),
    YAW(0x08),
    PITCH(0x10);

    private final byte bit;

    RelativeFlag(int bit) {
        this.bit = (byte)bit;
    }

    public byte getBit() {
        return bit;
    }

    public boolean isSet(byte mask) {
        return (mask & bit) != 0;
    }

    public byte set(byte mask, boolean relative) {
        if (relative) {
            return (byte) (mask | bit);
        } else {
            return (byte) (mask & ~bit);
        }
    }

    public static Set<RelativeFlag> getRelativeFlagsByMask(byte mask) {
        Set<RelativeFlag> relativeFlags = new HashSet<>();
        for (RelativeFlag relativeFlag : values()) {
            if (relativeFlag.isSet(mask)) {
                relativeFlags.add(relativeFlag);
            }
        }
        return relativeFlags;
    }

    public static byte getMaskByRelativeFlags(Set<RelativeFlag> sections) {
        byte mask = 0;
        for (RelativeFlag relativeFlag : sections) {
            mask |= relativeFlag.bit;
        }
        return mask;
    }
}