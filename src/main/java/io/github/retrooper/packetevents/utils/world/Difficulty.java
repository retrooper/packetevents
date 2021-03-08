package io.github.retrooper.packetevents.utils.world;

public enum Difficulty {
    PEACEFUL,
    EASY,
    NORMAL,
    HARD;

    public String getName() {
        return name().toLowerCase();
    }
}
