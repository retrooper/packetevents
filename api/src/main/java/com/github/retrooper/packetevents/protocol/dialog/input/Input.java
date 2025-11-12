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

package com.github.retrooper.packetevents.protocol.dialog.input;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Input {

    private final String key;
    private final InputControl control;

    public Input(String key, InputControl control) {
        this.key = key;
        this.control = control;
    }

    public static Input decode(NBT nbt, PacketWrapper<?> wrapper) {
        NBTCompound compound = (NBTCompound) nbt;
        String key = compound.getStringTagValueOrThrow("key");
        InputControl control = InputControl.decode(compound, wrapper);
        return new Input(key, control);
    }

    public static NBT encode(PacketWrapper<?> wrapper, Input input) {
        NBTCompound compound = new NBTCompound();
        compound.setTag("key", new NBTString(input.key));
        InputControl.encode(compound, wrapper, input.control);
        return compound;
    }

    public String getKey() {
        return this.key;
    }

    public InputControl getControl() {
        return this.control;
    }
}
