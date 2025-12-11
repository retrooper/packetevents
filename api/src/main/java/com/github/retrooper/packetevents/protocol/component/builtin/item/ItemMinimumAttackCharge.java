/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * @versions 1.21.11+
 */
@NullMarked
public class ItemMinimumAttackCharge {

    private float value;

    public ItemMinimumAttackCharge(float value) {
        this.value = value;
    }

    public static ItemMinimumAttackCharge read(PacketWrapper<?> wrapper) {
        return new ItemMinimumAttackCharge(wrapper.readFloat());
    }

    public static void write(PacketWrapper<?> wrapper, ItemMinimumAttackCharge component) {
        wrapper.writeFloat(component.value);
    }

    public float getValue() {
        return this.value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        ItemMinimumAttackCharge that = (ItemMinimumAttackCharge) obj;
        return Float.compare(that.value, this.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }
}
