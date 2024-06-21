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

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class CustomData {

    private CustomData() {
    }

    public static NBTCompound read(PacketWrapper<?> wrapper) {
        NBT nbt = wrapper.readNBTRaw();
        if (nbt instanceof NBTCompound) {
            return (NBTCompound) nbt;
        }
        if (nbt instanceof NBTString) {
            // TODO: parse nbt string
        }
        throw new UnsupportedOperationException("Unsupported custom data nbt type: " + nbt.getType());
    }

    public static void write(PacketWrapper<?> wrapper, NBTCompound compound) {
        wrapper.writeNBT(compound);
    }
}
