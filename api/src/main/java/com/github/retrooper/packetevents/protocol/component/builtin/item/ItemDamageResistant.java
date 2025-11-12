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

public class ItemDamageResistant {

    private ResourceLocation typesTagKey;

    public ItemDamageResistant(ResourceLocation typesTagKey) {
        this.typesTagKey = typesTagKey;
    }

    public static ItemDamageResistant read(PacketWrapper<?> wrapper) {
        return new ItemDamageResistant(wrapper.readIdentifier());
    }

    public static void write(PacketWrapper<?> wrapper, ItemDamageResistant resistant) {
        wrapper.writeIdentifier(resistant.typesTagKey);
    }

    public ResourceLocation getTypesTagKey() {
        return this.typesTagKey;
    }

    public void setTypesTagKey(ResourceLocation typesTagKey) {
        this.typesTagKey = typesTagKey;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemDamageResistant)) return false;
        ItemDamageResistant that = (ItemDamageResistant) obj;
        return this.typesTagKey.equals(that.typesTagKey);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.typesTagKey);
    }

    @Override
    public String toString() {
        return "ItemDamageResistant{typesTagKey=" + this.typesTagKey + '}';
    }
}
