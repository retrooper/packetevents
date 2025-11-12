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

package com.github.retrooper.packetevents.protocol.world.chunk;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.Nullable;

public enum HeightmapType {

    WORLD_SURFACE_WG("WORLD_SURFACE_WG", false),
    WORLD_SURFACE("WORLD_SURFACE", true),
    OCEAN_FLOOR_WG("OCEAN_FLOOR_WG", false),
    OCEAN_FLOOR("OCEAN_FLOOR", false),
    MOTION_BLOCKING("MOTION_BLOCKING", true),
    MOTION_BLOCKING_NO_LEAVES("MOTION_BLOCKING_NO_LEAVES", true),
    ;

    public static final Index<String, HeightmapType> SERIALIZATION_KEY_INDEX = Index.create(
            HeightmapType.class, HeightmapType::getSerializationKey);

    private final String serializationKey;
    private final boolean client;

    HeightmapType(String serializationKey, boolean client) {
        this.serializationKey = serializationKey;
        this.client = client;
    }

    public static @Nullable HeightmapType getHeightmapType(String serializationKey) {
        return SERIALIZATION_KEY_INDEX.value(serializationKey);
    }

    public static HeightmapType read(PacketWrapper<?> wrapper) {
        return wrapper.readEnum(HeightmapType.class);
    }

    public static void write(PacketWrapper<?> wrapper, HeightmapType type) {
        wrapper.writeEnum(type);
    }

    public String getSerializationKey() {
        return this.serializationKey;
    }

    public boolean isClient() {
        return this.client;
    }
}
