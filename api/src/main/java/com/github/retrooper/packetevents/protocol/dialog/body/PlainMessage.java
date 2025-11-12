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

package com.github.retrooper.packetevents.protocol.dialog.body;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PlainMessage {

    private final Component contents;
    private final int width;

    public PlainMessage(Component contents, int width) {
        this.contents = contents;
        this.width = width;
    }

    public static PlainMessage decode(NBT nbt, PacketWrapper<?> wrapper) {
        return decode((NBTCompound) nbt, wrapper);
    }

    public static PlainMessage decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        Component contents = compound.getOrThrow("contents", AdventureSerializer.serializer(wrapper), wrapper);
        int width = compound.getNumberTagValueOrDefault("width", 200).intValue();
        return new PlainMessage(contents, width);
    }

    public static NBT encode(PacketWrapper<?> wrapper, PlainMessage message) {
        NBTCompound compound = new NBTCompound();
        encode(compound, wrapper, message);
        return compound;
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, PlainMessage message) {
        compound.set("contents", message.contents, AdventureSerializer.serializer(wrapper), wrapper);
        if (message.width != 200) {
            compound.setTag("width", new NBTInt(message.width));
        }
    }

    public Component getContents() {
        return this.contents;
    }

    public int getWidth() {
        return this.width;
    }
}
