package com.github.retrooper.packetevents.protocol.recipe;

import com.github.retrooper.packetevents.protocol.recipe.data.RecipeData;
import org.jetbrains.annotations.NotNull;

// From MCProtocolLib
public class Recipe {
    private final @NotNull RecipeType type;
    private final @NotNull String identifier;
    private final RecipeData data;

    public Recipe(@NotNull RecipeType type, @NotNull String identifier, RecipeData data) {
        this.type = type;
        this.identifier = identifier;
        this.data = data;
    }

    public @NotNull RecipeType getType() {
        return type;
    }

    public @NotNull String getIdentifier() {
        return identifier;
    }

    public @NotNull RecipeData getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "type=" + type +
                ", identifier='" + identifier + '\'' +
                ", data=" + data +
                '}';
    }
}