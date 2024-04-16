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

package com.github.retrooper.packetevents.protocol.item.component.builtin;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class ItemDyeColor {

    private final int rgb;
    private final boolean showInTooltip;

    public ItemDyeColor(int rgb, boolean showInTooltip) {
        this.rgb = rgb;
        this.showInTooltip = showInTooltip;
    }

    public static ItemDyeColor read(PacketWrapper<?> wrapper) {
        int rgb = wrapper.readInt();
        boolean showInTooltip = wrapper.readBoolean();
        return new ItemDyeColor(rgb, showInTooltip);
    }

    public static void write(PacketWrapper<?> wrapper, ItemDyeColor color) {
        wrapper.writeInt(color.rgb);
        wrapper.writeBoolean(color.showInTooltip);
    }
}
