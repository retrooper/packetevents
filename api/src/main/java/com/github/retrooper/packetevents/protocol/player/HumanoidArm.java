package com.github.retrooper.packetevents.protocol.player;

public enum HumanoidArm {
    LEFT,
    RIGHT;

    public int getLegacyId() {
        return this == RIGHT ? 0 : 1;
    }

    public static final HumanoidArm[] VALUES = values();

    public static HumanoidArm getByLegacyId(int handValue) {
        return handValue == 0 ? RIGHT : LEFT;
    }
}
