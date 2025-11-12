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
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.LinkedHashSet;
import java.util.Set;

public final class RecipePropertySet {

    public static final ResourceLocation SMITHING_BASE = ResourceLocation.minecraft("smithing_base");
    public static final ResourceLocation SMITHING_TEMPLATE = ResourceLocation.minecraft("smithing_template");
    public static final ResourceLocation SMITHING_ADDITION = ResourceLocation.minecraft("smithing_addition");
    public static final ResourceLocation FURNACE_INPUT = ResourceLocation.minecraft("furnace_input");
    public static final ResourceLocation BLAST_FURNACE_INPUT = ResourceLocation.minecraft("blast_furnace_input");
    public static final ResourceLocation SMOKER_INPUT = ResourceLocation.minecraft("smoker_input");
    public static final ResourceLocation CAMPFIRE_INPUT = ResourceLocation.minecraft("campfire_input");

    private Set<ItemType> items;

    public RecipePropertySet(Set<ItemType> items) {
        this.items = items;
    }

    public static RecipePropertySet read(PacketWrapper<?> wrapper) {
        LinkedHashSet<ItemType> items = wrapper.readCollection(LinkedHashSet::new,
                ew -> ew.readMappedEntity(ItemTypes.getRegistry()));
        return new RecipePropertySet(items);
    }

    public static void write(PacketWrapper<?> wrapper, RecipePropertySet set) {
        wrapper.writeCollection(set.items, PacketWrapper::writeMappedEntity);
    }

    public Set<ItemType> getItems() {
        return this.items;
    }

    public void setItems(Set<ItemType> items) {
        this.items = items;
    }
}
