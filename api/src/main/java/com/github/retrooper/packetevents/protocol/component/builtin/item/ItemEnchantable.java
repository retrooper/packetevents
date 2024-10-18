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

import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class ItemEnchantable {

    private int value;

    public ItemEnchantable(int value) {
        this.value = value;
    }

    public static ItemEnchantable read(PacketWrapper<?> wrapper) {
        int value = wrapper.readVarInt();
        return new ItemEnchantable(value);
    }

    public static void write(PacketWrapper<?> wrapper, ItemEnchantable enchantable) {
        wrapper.writeVarInt(enchantable.value);
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemEnchantable)) return false;
        ItemEnchantable that = (ItemEnchantable) obj;
        return this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    @Override
    public String toString() {
        return "ItemEnchantable{value=" + this.value + '}';
    }
}
