/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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