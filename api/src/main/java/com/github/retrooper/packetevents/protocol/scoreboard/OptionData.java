package com.github.retrooper.packetevents.protocol.scoreboard;

import org.jetbrains.annotations.Nullable;

public enum OptionData {
    NONE((byte) 0x00),
    FRIENDLY_FIRE((byte) 0x01),
    FRIENDLY_CAN_SEE_INVISIBLE((byte) 0x02),
    ALL((byte) 0x03);

    private static final OptionData[] VALUES = values();
    private final byte byteValue;

    OptionData(byte value) {
        byteValue = value;
    }

    public byte getByteValue() {
        return byteValue;
    }

    @Nullable
    public static OptionData fromValue(byte value) {
        for (OptionData data : VALUES) {
            if (data.getByteValue() == value) {
                return data;
            }
        }
        return null;
    }
}