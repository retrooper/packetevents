package com.github.retrooper.packetevents.protocol.recipe;

public class RecipeId<T> {
    private final T id;

    public RecipeId(T id) {
        this.id = id;
    }

    public T getId() {
        return id;
    }
}
