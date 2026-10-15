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
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.util.WeightedList;
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

import java.util.List;
import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public final class WeightedStateProvider extends AbstractMappedEntity implements BlockStateProvider {

    public static final NbtMapCodec<WeightedStateProvider> MAP_CODEC = new NbtMapCodec<WeightedStateProvider>() {
        private final NbtCodec<WeightedList<WrappedBlockState>> entriesCodec = WeightedList.codec(BlockStateCodec.CODEC);

        @Override
        public WeightedStateProvider decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            WeightedList<WrappedBlockState> entries = tag.getOrThrow("entries", this.entriesCodec, wrapper);
            return new WeightedStateProvider(null, entries);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, WeightedStateProvider value) throws NbtCodecException {
            tag.set("entries", value.entries, this.entriesCodec, wrapper);
        }
    };

    private final WeightedList<WrappedBlockState> entries;

    public WeightedStateProvider(WeightedList<WrappedBlockState> entries) {
        this(null, entries);
    }

    @ApiStatus.Internal
    public WeightedStateProvider(@Nullable TypesBuilderData data, WeightedList<WrappedBlockState> entries) {
        super(data);
        this.entries = entries;
    }

    public WeightedList<WrappedBlockState> getEntries() {
        return this.entries;
    }

    @Override
    public BlockStateProviderType<?> getType() {
        return BlockStateProviderTypes.WEIGHTED;
    }

    @Override
    public WeightedStateProvider copy(@Nullable TypesBuilderData newData) {
        return new WeightedStateProvider(newData, this.entries);
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (!(obj instanceof WeightedStateProvider)) return false;
        WeightedStateProvider that = (WeightedStateProvider) obj;
        return this.entries.equals(that.entries);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.entries);
    }
}
