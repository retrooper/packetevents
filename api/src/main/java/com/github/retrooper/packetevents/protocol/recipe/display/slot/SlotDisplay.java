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

package com.github.retrooper.packetevents.protocol.recipe.display.slot;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public abstract class SlotDisplay<T extends SlotDisplay<?>> {

    protected final SlotDisplayType<T> type;

    public SlotDisplay(SlotDisplayType<T> type) {
        this.type = type;
    }

    public static SlotDisplay<?> read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntity(SlotDisplayTypes.getRegistry()).read(wrapper);
    }

    @SuppressWarnings("unchecked")
    public static <T extends SlotDisplay<?>> void write(
            PacketWrapper<?> wrapper, T display) {
        wrapper.writeMappedEntity(display.getType());
        ((SlotDisplayType<T>) display.getType()).write(wrapper, display);
    }

    public SlotDisplayType<T> getType() {
        return this.type;
    }
}
