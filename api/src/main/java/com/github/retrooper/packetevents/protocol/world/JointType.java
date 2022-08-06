package com.github.retrooper.packetevents.protocol.world;

import net.kyori.adventure.text.Component;

import java.util.Arrays;
import java.util.Optional;

public enum JointType {
    ROLLABLE("rollable"),
    ALIGNED("aligned");

    private static final JointType[] VALUES = values();

    private final String name;

    JointType(String name) {
        this.name = name;
    }

    public String getSerializedName() {
        return this.name;
    }

    public Component getTranslatableName() {
        return Component.translatable("jigsaw_block.joint." + this.name);
    }

    public static Optional<JointType> byName(String name) {
        return Arrays.stream(VALUES).filter(jointType -> jointType.getSerializedName().equals(name)).findFirst();
    }
}
