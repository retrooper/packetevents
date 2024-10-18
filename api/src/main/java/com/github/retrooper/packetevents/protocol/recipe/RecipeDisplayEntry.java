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

package com.github.retrooper.packetevents.protocol.recipe;

import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.recipe.category.RecipeBookCategories;
import com.github.retrooper.packetevents.protocol.recipe.category.RecipeBookCategory;
import com.github.retrooper.packetevents.protocol.recipe.display.RecipeDisplay;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public final class RecipeDisplayEntry {

    private RecipeDisplayId id;
    private RecipeDisplay<?> display;
    private @Nullable Integer group;
    private RecipeBookCategory category;
    private @Nullable List<MappedEntitySet<ItemType>> ingredients;

    public RecipeDisplayEntry(
            RecipeDisplayId id,
            RecipeDisplay<?> display,
            @Nullable Integer group,
            RecipeBookCategory category,
            @Nullable List<MappedEntitySet<ItemType>> ingredients
    ) {
        this.id = id;
        this.display = display;
        this.group = group;
        this.category = category;
        this.ingredients = ingredients;
    }

    public static RecipeDisplayEntry read(PacketWrapper<?> wrapper) {
        RecipeDisplayId id = RecipeDisplayId.read(wrapper);
        RecipeDisplay<?> display = RecipeDisplay.read(wrapper);
        Integer group = wrapper.readNullableVarInt();
        RecipeBookCategory category = wrapper.readMappedEntity(RecipeBookCategories.getRegistry());
        List<MappedEntitySet<ItemType>> ingredients = wrapper.readOptional(ew -> ew.readList(
                eww -> MappedEntitySet.read(eww, ItemTypes.getRegistry())));
        return new RecipeDisplayEntry(id, display, group, category, ingredients);
    }

    public static void write(PacketWrapper<?> wrapper, RecipeDisplayEntry entry) {
        RecipeDisplayId.write(wrapper, entry.id);
        RecipeDisplay.write(wrapper, entry.display);
        wrapper.writeNullableVarInt(entry.group);
        wrapper.writeMappedEntity(entry.category);
        wrapper.writeOptional(entry.ingredients, (ew, list) ->
                ew.writeList(list, MappedEntitySet::write));
    }

    public RecipeDisplayId getId() {
        return this.id;
    }

    public void setId(RecipeDisplayId id) {
        this.id = id;
    }

    public RecipeDisplay<?> getDisplay() {
        return this.display;
    }

    public void setDisplay(RecipeDisplay<?> display) {
        this.display = display;
    }

    public @Nullable Integer getGroup() {
        return this.group;
    }

    public void setGroup(@Nullable Integer group) {
        this.group = group;
    }

    public RecipeBookCategory getCategory() {
        return this.category;
    }

    public void setCategory(RecipeBookCategory category) {
        this.category = category;
    }

    public @Nullable List<MappedEntitySet<ItemType>> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(@Nullable List<MappedEntitySet<ItemType>> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RecipeDisplayEntry)) return false;
        RecipeDisplayEntry that = (RecipeDisplayEntry) obj;
        if (this.id != that.id) return false;
        if (!this.display.equals(that.display)) return false;
        if (!Objects.equals(this.group, that.group)) return false;
        if (!this.category.equals(that.category)) return false;
        return Objects.equals(this.ingredients, that.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.display, this.group, this.category, this.ingredients);
    }

    @Override
    public String toString() {
        return "RecipeDisplayEntry{id=" + this.id + ", display=" + this.display + ", group=" + this.group + ", category=" + this.category + ", ingredients=" + this.ingredients + '}';
    }
}
