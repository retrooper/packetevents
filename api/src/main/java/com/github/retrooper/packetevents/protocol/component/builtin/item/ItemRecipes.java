/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;
import java.util.Objects;

public class ItemRecipes {

    private List<ResourceLocation> recipes;

    public ItemRecipes(List<ResourceLocation> recipes) {
        this.recipes = recipes;
    }

    public static ItemRecipes read(PacketWrapper<?> wrapper) {
        List<ResourceLocation> recipes = wrapper.readList(PacketWrapper::readIdentifier);
        return new ItemRecipes(recipes);
    }

    public static void write(PacketWrapper<?> wrapper, ItemRecipes recipes) {
        wrapper.writeList(recipes.recipes, PacketWrapper::writeIdentifier);
    }

    public void addRecipe(ResourceLocation recipeKey) {
        this.recipes.add(recipeKey);
    }

    public void removeRecipe(ResourceLocation recipeKey) {
        this.recipes.remove(recipeKey);
    }

    public List<ResourceLocation> getRecipes() {
        return this.recipes;
    }

    public void setRecipes(List<ResourceLocation> recipes) {
        this.recipes = recipes;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemRecipes)) return false;
        ItemRecipes that = (ItemRecipes) obj;
        return this.recipes.equals(that.recipes);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.recipes);
    }
}
