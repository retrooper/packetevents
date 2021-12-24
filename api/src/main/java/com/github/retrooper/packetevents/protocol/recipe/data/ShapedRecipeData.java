package com.github.retrooper.packetevents.protocol.recipe.data;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.recipe.Ingredient;
import org.jetbrains.annotations.NotNull;

public class ShapedRecipeData implements RecipeData {
    private final int width;
    private final int height;
    private final @NotNull String group;
    private final @NotNull Ingredient[] ingredients;
    private final ItemStack result;

    public ShapedRecipeData(final int width, final int height, final @NotNull String group, final @NotNull Ingredient[] ingredients, final ItemStack result) {
        this.width = width;
        this.height = height;
        this.group = group;
        this.ingredients = ingredients;
        this.result = result;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public @NotNull String getGroup() {
        return this.group;
    }

    public @NotNull Ingredient[] getIngredients() {
        return this.ingredients;
    }

    public ItemStack getResult() {
        return this.result;
    }
}