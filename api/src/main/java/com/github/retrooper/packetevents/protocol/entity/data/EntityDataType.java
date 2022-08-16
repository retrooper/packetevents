/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.entity.data;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class EntityDataType<T> {
    private final String name;
    private final int[] ids;
    private final Function<PacketWrapper<?>, T> dataDeserializer;
    private final BiConsumer<PacketWrapper<?>, Object> dataSerializer;

    public EntityDataType(String name, int[] ids, Function<PacketWrapper<?>, T> dataDeserializer, BiConsumer<PacketWrapper<?>, Object> dataSerializer) {
        this.name = name;
        this.ids = ids;
        this.dataDeserializer = dataDeserializer;
        this.dataSerializer = dataSerializer;
    }

    public String getName() {
        return name;
    }

    public int getId(ClientVersion version) {
        int index = EntityDataTypes.TYPES_BUILDER.getDataIndex(version);
        return ids[index];
    }

    public Function<PacketWrapper<?>, T> getDataDeserializer() {
        return dataDeserializer;
    }

    public BiConsumer<PacketWrapper<?>, Object> getDataSerializer() {
        return dataSerializer;
    }
}
