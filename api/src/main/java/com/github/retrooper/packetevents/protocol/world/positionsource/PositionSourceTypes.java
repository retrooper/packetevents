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

package com.github.retrooper.packetevents.protocol.world.positionsource;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.positionsource.builtin.BlockPositionSource;
import com.github.retrooper.packetevents.protocol.world.positionsource.builtin.EntityPositionSource;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.MappingHelper;
import com.github.retrooper.packetevents.util.mappings.TypesBuilder;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.PacketWrapper.Reader;
import com.github.retrooper.packetevents.wrapper.PacketWrapper.Writer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PositionSourceTypes {

    private static final Map<String, PositionSourceType<?>> POS_SOURCE_MAP = new HashMap<>();
    private static final Map<Byte, Map<Integer, PositionSourceType<?>>> POS_SOURCE_ID_MAP = new HashMap<>();
    private static final TypesBuilder TYPES_BUILDER = new TypesBuilder("world/world_position_source_mappings");

    @ApiStatus.Internal
    public static <T extends PositionSource> PositionSourceType<T> define(
            String key,
            Reader<T> reader, Writer<T> writer,
            Decoder<T> decoder, Encoder<T> encoder
    ) {
        TypesBuilderData data = TYPES_BUILDER.define(key);
        PositionSourceType<T> sourceType = new PositionSourceType<T>() {
            @Override
            public T read(PacketWrapper<?> wrapper) {
                return reader.apply(wrapper);
            }

            @Override
            public void write(PacketWrapper<?> wrapper, T source) {
                writer.accept(wrapper, source);
            }

            @Override
            public T decode(NBTCompound compound, ClientVersion version) {
                return decoder.decode(compound, version);
            }

            @Override
            public void encode(T source, ClientVersion version, NBTCompound compound) {
                encoder.encode(source, version, compound);
            }

            @Override
            public ResourceLocation getName() {
                return data.getName();
            }

            @Override
            public int getId(ClientVersion version) {
                return MappingHelper.getId(version, TYPES_BUILDER, data);
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof PositionSourceType<?>) {
                    return this.getName().equals(((PositionSourceType<?>) obj).getName());
                }
                return false;
            }
        };
        MappingHelper.registerMapping(TYPES_BUILDER, POS_SOURCE_MAP, POS_SOURCE_ID_MAP, sourceType);
        return sourceType;
    }

    // with minecraft:key
    @Nullable
    public static PositionSourceType<?> getByName(String name) {
        return POS_SOURCE_MAP.get(name);
    }

    public static PositionSourceType<?> getById(ClientVersion version, int id) {
        int index = TYPES_BUILDER.getDataIndex(version);
        Map<Integer, PositionSourceType<?>> idMap = POS_SOURCE_ID_MAP.get((byte) index);
        return idMap.get(id);
    }

    public static final PositionSourceType<BlockPositionSource> BLOCK = define("block",
            BlockPositionSource::read, BlockPositionSource::write,
            BlockPositionSource::decodeSource, BlockPositionSource::encodeSource);
    public static final PositionSourceType<EntityPositionSource> ENTITY = define("entity",
            EntityPositionSource::read, EntityPositionSource::write,
            EntityPositionSource::decodeSource, EntityPositionSource::encodeSource);

    static {
        TYPES_BUILDER.unloadFileMappings();
    }

    @FunctionalInterface
    public interface Decoder<T> {

        T decode(NBTCompound compound, ClientVersion version);
    }

    @FunctionalInterface
    public interface Encoder<T> {

        void encode(T value, ClientVersion version, NBTCompound compound);
    }
}
