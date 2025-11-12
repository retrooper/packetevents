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

package com.github.retrooper.packetevents.protocol.component.predicates;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

@ApiStatus.Experimental
public class NbtComponentPredicate implements IComponentPredicate {

    private final NBT tag;

    public NbtComponentPredicate(NBT tag) {
        this.tag = tag;
    }

    public static NbtComponentPredicate read(PacketWrapper<?> wrapper) {
        NBT tag = wrapper.readNBTRaw();
        return new NbtComponentPredicate(tag);
    }

    public static void write(PacketWrapper<?> wrapper, NbtComponentPredicate predicate) {
        wrapper.writeNBTRaw(predicate.tag);
    }

    public NBT getTag() {
        return this.tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NbtComponentPredicate)) return false;
        NbtComponentPredicate that = (NbtComponentPredicate) obj;
        return this.tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.tag);
    }
}
