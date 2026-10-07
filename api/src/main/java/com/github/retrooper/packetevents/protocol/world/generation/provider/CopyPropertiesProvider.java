/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world.generation.provider;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.generation.BlockStateProvider;
import com.github.retrooper.packetevents.protocol.world.generation.BlockStateProviderType;
import com.github.retrooper.packetevents.protocol.world.generation.BlockStateProviderTypes;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public final class CopyPropertiesProvider extends AbstractMappedEntity implements BlockStateProvider {

    public static final NbtMapCodec<CopyPropertiesProvider> MAP_CODEC = new NbtMapCodec<CopyPropertiesProvider>() {
        @Override
        public CopyPropertiesProvider decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            BlockStateProvider source = tag.getOrThrow("source", BlockStateProvider.CODEC, wrapper);
            return new CopyPropertiesProvider(null, source);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, CopyPropertiesProvider value) throws NbtCodecException {
            tag.set("source", value.source, BlockStateProvider.CODEC, wrapper);
        }
    };

    private final BlockStateProvider source;

    public CopyPropertiesProvider(BlockStateProvider source) {
        this(null, source);
    }

    @ApiStatus.Internal
    public CopyPropertiesProvider(@Nullable TypesBuilderData data, BlockStateProvider source) {
        super(data);
        this.source = source;
    }

    public BlockStateProvider getSource() {
        return this.source;
    }

    @Override
    public BlockStateProviderType<?> getType() {
        return BlockStateProviderTypes.COPY_PROPERTIES;
    }

    @Override
    public CopyPropertiesProvider copy(@Nullable TypesBuilderData newData) {
        return new CopyPropertiesProvider(newData, this.source);
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (!(obj instanceof CopyPropertiesProvider)) return false;
        CopyPropertiesProvider that = (CopyPropertiesProvider) obj;
        return this.source.equals(that.source);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.source);
    }
}
