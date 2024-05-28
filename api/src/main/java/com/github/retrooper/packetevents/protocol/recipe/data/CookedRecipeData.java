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
import com.github.retrooper.packetevents.protocol.recipe.CookingCategory;
import com.github.retrooper.packetevents.protocol.recipe.Ingredient;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class CookedRecipeData implements RecipeData {

    private final String group;
    private final CookingCategory category;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final float experience;
    private final int cookingTime;

    @Deprecated
    public CookedRecipeData(String group, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        this(group, CookingCategory.MISC, ingredient, result, experience, cookingTime);
    }

    public CookedRecipeData(
            String group, CookingCategory category, Ingredient ingredient,
            ItemStack result, float experience, int cookingTime
    ) {
        this.group = group;
        this.category = category;
        this.ingredient = ingredient;
        this.result = result;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    public static CookedRecipeData read(PacketWrapper<?> wrapper) {
        String group = wrapper.readString();
        CookingCategory category = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3) ?
                wrapper.readEnum(CookingCategory.values()) : CookingCategory.MISC;
        Ingredient ingredient = Ingredient.read(wrapper);
        ItemStack result = wrapper.readItemStack();
        float experience = wrapper.readFloat();
        int cookingTime = wrapper.readVarInt();
        return new CookedRecipeData(group, category, ingredient, result, experience, cookingTime);
    }

    public static void write(PacketWrapper<?> wrapper, CookedRecipeData data) {
        wrapper.writeString(data.group);
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
            wrapper.writeEnum(data.category);
        }
        Ingredient.write(wrapper, data.ingredient);
        wrapper.writeItemStack(data.result);
        wrapper.writeFloat(data.experience);
        wrapper.writeVarInt(data.cookingTime);
    }

    public String getGroup() {
        return this.group;
    }

    public CookingCategory getCategory() {
        return this.category;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public float getExperience() {
        return this.experience;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }
}
