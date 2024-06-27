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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class StoneCuttingRecipeData implements RecipeData {

    private final String group;
    private final Ingredient ingredient;
    private final ItemStack result;

    public StoneCuttingRecipeData(String group, Ingredient ingredient, ItemStack result) {
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
    }

    public static StoneCuttingRecipeData read(PacketWrapper<?> wrapper) {
        String group = wrapper.readString();
        Ingredient ingredient = Ingredient.read(wrapper);
        ItemStack result = wrapper.readItemStack();
        return new StoneCuttingRecipeData(group, ingredient, result);
    }

    public static void write(PacketWrapper<?> wrapper, StoneCuttingRecipeData data) {
        wrapper.writeString(data.group);
        Ingredient.write(wrapper, data.ingredient);
        wrapper.writeItemStack(data.result);
    }

    public String getGroup() {
        return this.group;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public ItemStack getResult() {
        return this.result;
    }
}
