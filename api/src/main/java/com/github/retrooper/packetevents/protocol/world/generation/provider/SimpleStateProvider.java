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
import com.github.retrooper.packetevents.protocol.world.states.BlockStateCodec;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
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
public final class SimpleStateProvider extends AbstractMappedEntity implements BlockStateProvider {

    public static final NbtMapCodec<SimpleStateProvider> MAP_CODEC = new NbtMapCodec<SimpleStateProvider>() {
        @Override
        public SimpleStateProvider decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            WrappedBlockState state = tag.getOrThrow("state", BlockStateCodec.CODEC, wrapper);
            return new SimpleStateProvider(null, state);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, SimpleStateProvider value) throws NbtCodecException {
            tag.set("state", value.state, BlockStateCodec.CODEC, wrapper);
        }
    };

    private final WrappedBlockState state;

    public SimpleStateProvider(WrappedBlockState state) {
        this(null, state);
    }

    @ApiStatus.Internal
    public SimpleStateProvider(@Nullable TypesBuilderData data, WrappedBlockState state) {
        super(data);
        this.state = state;
    }

    public WrappedBlockState getState() {
        return this.state;
    }

    @Override
    public BlockStateProviderType<?> getType() {
        return BlockStateProviderTypes.SIMPLE;
    }

    @Override
    public SimpleStateProvider copy(@Nullable TypesBuilderData newData) {
        return new SimpleStateProvider(newData, this.state);
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (!(obj instanceof SimpleStateProvider)) return false;
        SimpleStateProvider that = (SimpleStateProvider) obj;
        return this.state.equals(that.state);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.state);
    }
}
