package io.github.retrooper.packetevents.utils.world;

import org.jetbrains.annotations.Nullable;

public enum LevelType {
    DEFAULT, FLAT, LARGE_BIOMES("largeBiomes"), AMPLIFIED, CUSTOMIZED, DEBUG_ALL_BLOCK_STATES, DEFAULT_1_1;
    private final String name;
    LevelType() {
        this.name = name().toLowerCase();
    }

    LevelType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    @Nullable
    public static LevelType getByName(String name) {
        for (LevelType type : values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
