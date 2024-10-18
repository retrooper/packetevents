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

package com.github.retrooper.packetevents.protocol.recipe;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public final class RecipeDisplayId {

    private final int id;

    public RecipeDisplayId(int id) {
        this.id = id;
    }

    public static RecipeDisplayId read(PacketWrapper<?> wrapper) {
        int id = wrapper.readVarInt();
        return new RecipeDisplayId(id);
    }

    public static void write(PacketWrapper<?> wrapper, RecipeDisplayId id) {
        wrapper.writeVarInt(id.id);
    }

    public int getId() {
        return this.id;
    }
}
