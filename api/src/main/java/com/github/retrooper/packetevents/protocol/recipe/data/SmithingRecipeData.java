package com.github.retrooper.packetevents.protocol.recipe.data;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.recipe.Ingredient;
import org.jetbrains.annotations.NotNull;

public class SmithingRecipeData implements RecipeData {
    private final @NotNull Ingredient base;
    private final @NotNull Ingredient addition;
    private final ItemStack result;

    public SmithingRecipeData(@NotNull Ingredient base, @NotNull Ingredient addition, ItemStack result) {
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    @NotNull
    public Ingredient getBase() {
        return base;
    }

    @NotNull
    public Ingredient getAddition() {
        return addition;
    }

    public ItemStack getResult() {
        return result;
    }
}
