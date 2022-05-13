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
