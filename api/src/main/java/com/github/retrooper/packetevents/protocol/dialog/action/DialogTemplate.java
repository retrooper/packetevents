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

package com.github.retrooper.packetevents.protocol.dialog.action;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * This uses the same format as command function macros.
 */
@NullMarked
public class DialogTemplate {

    private final String raw;

    public DialogTemplate(String raw) {
        this.raw = raw;
    }

    public static DialogTemplate decode(NBT nbt, PacketWrapper<?> wrapper) {
        return new DialogTemplate(((NBTString) nbt).getValue());
    }

    public static NBT encode(PacketWrapper<?> wrapper, DialogTemplate template) {
        return new NBTString(template.raw);
    }

    public String getRaw() {
        return this.raw;
    }
}
