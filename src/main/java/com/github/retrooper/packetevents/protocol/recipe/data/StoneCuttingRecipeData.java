package com.github.retrooper.packetevents.protocol.recipe.data;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.recipe.Ingredient;
import org.jetbrains.annotations.NotNull;

public class StoneCuttingRecipeData implements RecipeData {
    private final @NotNull String group;
    private final @NotNull Ingredient ingredient;
    private final ItemStack result;

    public StoneCuttingRecipeData(@NotNull String group, @NotNull Ingredient ingredient, ItemStack result) {
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
    }

    @NotNull
    public String getGroup() {
        return group;
    }

    @NotNull
    public Ingredient getIngredient() {
        return ingredient;
    }

    @NotNull
    public ItemStack getResult() {
        return result;
    }
}
