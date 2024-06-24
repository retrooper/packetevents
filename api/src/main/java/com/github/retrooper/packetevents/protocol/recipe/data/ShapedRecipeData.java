/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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
import org.jetbrains.annotations.Nullable;

public class ShapedRecipeData implements RecipeData {
    private final int width;
    private final int height;
    private final @NotNull String group;
    private CraftingBookCategory category;
    private final @NotNull Ingredient[] ingredients;
    private final ItemStack result;

    public ShapedRecipeData(final int width, final int height, final @NotNull String group, final @NotNull CraftingBookCategory category, final @NotNull Ingredient[] ingredients, final ItemStack result) {
        this.width = width;
        this.height = height;
        this.group = group;
        this.category = category;
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

    public @Nullable CraftingBookCategory getCategory() {
        return this.category;
    }

    public @NotNull Ingredient[] getIngredients() {
        return this.ingredients;
    }

    public ItemStack getResult() {
        return this.result;
    }
}