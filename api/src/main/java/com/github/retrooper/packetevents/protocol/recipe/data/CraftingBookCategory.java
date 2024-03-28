package com.github.retrooper.packetevents.protocol.recipe.data;

public enum CraftingBookCategory {
    BUILDING(0),
    REDSTONE(1),
    EQUIPMENT(2),
    MISC(3);

    private final int id;

    public static CraftingBookCategory byId(int category) {
        for (CraftingBookCategory value : values()) {
            if (value.getId() == category) {
                return value;
            }
        }
        return null;
    }

    CraftingBookCategory(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
