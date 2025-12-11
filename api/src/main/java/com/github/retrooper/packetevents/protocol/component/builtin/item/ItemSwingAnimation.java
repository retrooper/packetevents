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
public class ItemSwingAnimation {

    private static final Type[] TYPES = Type.values();

    private Type type;
    private int duration;

    public ItemSwingAnimation(Type type, int duration) {
        this.type = type;
        this.duration = duration;
    }

    public static ItemSwingAnimation read(PacketWrapper<?> wrapper) {
        Type type = wrapper.readEnum(TYPES, Type.NONE);
        int duration = wrapper.readVarInt();
        return new ItemSwingAnimation(type, duration);
    }

    public static void write(PacketWrapper<?> wrapper, ItemSwingAnimation component) {
        wrapper.writeEnum(component.type);
        wrapper.writeVarInt(component.duration);
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        ItemSwingAnimation that = (ItemSwingAnimation) obj;
        if (this.duration != that.duration) return false;
        return this.type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.duration);
    }

    public enum Type {
        NONE,
        WHACK,
        STAB,
    }
}
