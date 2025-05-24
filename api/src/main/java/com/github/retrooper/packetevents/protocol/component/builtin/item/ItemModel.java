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

import java.util.Objects;

public class ItemModel {

    private ResourceLocation modelLocation;

    public ItemModel(ResourceLocation modelLocation) {
        this.modelLocation = modelLocation;
    }

    public static ItemModel read(PacketWrapper<?> wrapper) {
        return new ItemModel(wrapper.readIdentifier());
    }

    public static void write(PacketWrapper<?> wrapper, ItemModel model) {
        wrapper.writeIdentifier(model.modelLocation);
    }

    public ResourceLocation getModelLocation() {
        return this.modelLocation;
    }

    public void setModelLocation(ResourceLocation modelLocation) {
        this.modelLocation = modelLocation;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemModel)) return false;
        ItemModel itemModel = (ItemModel) obj;
        return this.modelLocation.equals(itemModel.modelLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.modelLocation);
    }

    @Override
    public String toString() {
        return "ItemModel{modelLocation=" + this.modelLocation + '}';
    }
}
