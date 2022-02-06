package com.github.retrooper.packetevents.protocol.recipe.data;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.recipe.Ingredient;
import org.jetbrains.annotations.NotNull;

public class CookedRecipeData implements RecipeData {
    private final @NotNull String group;
    private final @NotNull Ingredient ingredient;
    private final ItemStack result;
    private final float experience;
    private final int cookingTime;

    public CookedRecipeData(@NotNull String group, @NotNull Ingredient ingredient, @NotNull ItemStack result, float experience, int cookingTime) {
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
        this.experience = experience;
        this.cookingTime = cookingTime;
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

    public float getExperience() {
        return experience;
    }

    public int getCookingTime() {
        return cookingTime;
    }
}