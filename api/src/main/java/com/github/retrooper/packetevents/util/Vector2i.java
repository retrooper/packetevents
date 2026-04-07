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

package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public final class Vector2i {

    private final int x;
    private final int z;

    public Vector2i(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public static Vector2i read(PacketWrapper<?> wrapper) {
        return fromLong(wrapper.readLong());
    }

    public static void write(PacketWrapper<?> wrapper, Vector2i vec) {
        wrapper.writeLong(vec.asLong());
    }

    public static Vector2i fromLong(long l) {
        return new Vector2i((int) l, (int) (l >> 32));
    }

    public long asLong() {
        return ((long) this.x & 0xFFFFFFFFL) | (((long) this.z & 0xFFFFFFFFL) << 32);
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector2i)) return false;
        Vector2i vector2i = (Vector2i) obj;
        if (this.x != vector2i.x) return false;
        return this.z == vector2i.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.z);
    }
}
