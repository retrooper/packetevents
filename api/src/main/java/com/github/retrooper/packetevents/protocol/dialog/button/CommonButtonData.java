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

package com.github.retrooper.packetevents.protocol.dialog.button;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CommonButtonData {

    private final Component label;
    private final @Nullable Component tooltip;
    private final int width;

    public CommonButtonData(Component label, @Nullable Component tooltip, int width) {
        this.label = label;
        this.tooltip = tooltip;
        this.width = width;
    }

    public static CommonButtonData decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        AdventureSerializer serializer = AdventureSerializer.serializer(wrapper);
        Component label = compound.getOrThrow("label", serializer, wrapper);
        Component tooltip = compound.getOrNull("tooltip", serializer, wrapper);
        int width = compound.getNumberTagValueOrDefault("width", 150).intValue();
        return new CommonButtonData(label, tooltip, width);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, CommonButtonData button) {
        AdventureSerializer serializer = AdventureSerializer.serializer(wrapper);
        compound.set("label", button.label, serializer, wrapper);
        if (button.tooltip != null) {
            compound.set("tooltip", button.tooltip, serializer, wrapper);
        }
        if (button.width != 150) {
            compound.setTag("width", new NBTInt(button.width));
        }
    }

    public Component getLabel() {
        return this.label;
    }

    public @Nullable Component getTooltip() {
        return this.tooltip;
    }

    public int getWidth() {
        return this.width;
    }
}
