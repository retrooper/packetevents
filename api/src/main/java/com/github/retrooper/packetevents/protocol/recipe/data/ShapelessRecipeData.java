package com.github.retrooper.packetevents.protocol.recipe.data;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.recipe.Ingredient;
import org.jetbrains.annotations.NotNull;

public class ShapelessRecipeData implements RecipeData {
    private final @NotNull String group;
    private final @NotNull Ingredient[] ingredients;
    private final ItemStack result;

    public ShapelessRecipeData(@NotNull String group, @NotNull Ingredient[] ingredients, @NotNull ItemStack result) {
        this.group = group;
        this.ingredients = ingredients;
        this.result = result;
    }

    public @NotNull String getGroup() {
        return group;
    }

    public @NotNull Ingredient[] getIngredients() {
        return ingredients;
    }

    public @NotNull ItemStack getResult() {
        return result;
    }
}