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

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.recipe.CraftingCategory;
import com.github.retrooper.packetevents.protocol.recipe.Ingredient;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class ShapelessRecipeData implements RecipeData {

    private final String group;
    private final CraftingCategory category;
    private final Ingredient[] ingredients;
    private final ItemStack result;

    @Deprecated
    public ShapelessRecipeData(String group, Ingredient[] ingredients, ItemStack result) {
        this(group, CraftingCategory.MISC, ingredients, result);
    }

    public ShapelessRecipeData(String group, CraftingCategory category, Ingredient[] ingredients, ItemStack result) {
        this.group = group;
        this.category = category;
        this.ingredients = ingredients;
        this.result = result;
    }

    public static ShapelessRecipeData read(PacketWrapper<?> wrapper) {
        String group = wrapper.readString();
        CraftingCategory category = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3)
                ? wrapper.readEnum(CraftingCategory.values()) : CraftingCategory.MISC;
        Ingredient[] ingredients = wrapper.readArray(Ingredient::read, Ingredient.class);
        ItemStack result = wrapper.readItemStack();
        return new ShapelessRecipeData(group, category, ingredients, result);
    }

    public static void write(PacketWrapper<?> wrapper, ShapelessRecipeData data) {
        wrapper.writeString(data.group);
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
            wrapper.writeEnum(data.category);
        }
        wrapper.writeArray(data.ingredients, Ingredient::write);
        wrapper.writeItemStack(data.result);
    }

    public String getGroup() {
        return this.group;
    }

    public CraftingCategory getCategory() {
        return this.category;
    }

    public Ingredient[] getIngredients() {
        return this.ingredients;
    }

    public ItemStack getResult() {
        return this.result;
    }
}
