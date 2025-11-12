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

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface InputControl {

    static InputControl decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        String typeName = compound.getStringTagValueOrThrow("type");
        InputControlType<?> type = InputControlTypes.getRegistry().getByNameOrThrow(typeName);
        return type.decode(compound, wrapper);
    }

    @SuppressWarnings("unchecked") // not unchecked
    static void encode(NBTCompound compound, PacketWrapper<?> wrapper, InputControl control) {
        compound.set("type", control.getType().getName(), ResourceLocation::encode, wrapper);
        ((InputControlType<? super InputControl>) control.getType()).encode(compound, wrapper, control);
    }

    InputControlType<?> getType();
}
