package io.github.retrooper.packetevents.utils.world;

public enum Dimension {
    NETHER(-1),
    OVERWORLD(0),
    END(1);
    private final int id;
    Dimension(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Dimension getById(int id) {
        return values()[id + 1];
    }
}
