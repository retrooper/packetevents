package com.github.retrooper.packetevents.protocol.recipe;

public enum RecipeBookAction {

    INIT(0),
    ADD(1),
    REMOVE(2);

    private final int id;

    RecipeBookAction(int id) {
        this.id = id;
    }

    public static RecipeBookAction getById(int readVarInt) {
        for (RecipeBookAction recipeBookAction : values()) {
            if (recipeBookAction.id == readVarInt) {
                return recipeBookAction;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }
}
