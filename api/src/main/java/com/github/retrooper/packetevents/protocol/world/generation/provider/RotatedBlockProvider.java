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
import com.github.retrooper.packetevents.protocol.world.Direction;
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
public final class RotatedBlockProvider extends AbstractMappedEntity implements BlockStateProvider {

    public static final NbtMapCodec<RotatedBlockProvider> MAP_CODEC = new NbtMapCodec<RotatedBlockProvider>() {
        @Override
        public RotatedBlockProvider decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            BlockStateProvider state = tag.getOrThrow("state", BlockStateProvider.CODEC, wrapper);
            Direction direction = tag.getOrNull("direction", Direction.CODEC, wrapper);
            return new RotatedBlockProvider(null, state, direction);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, RotatedBlockProvider value) throws NbtCodecException {
            tag.set("state", value.state, BlockStateProvider.CODEC, wrapper);
            if (value.direction != null) {
                tag.set("direction", value.direction, Direction.CODEC, wrapper);
            }
        }
    };

    private final BlockStateProvider state;
    private final @Nullable Direction direction;

    public RotatedBlockProvider(BlockStateProvider state, @Nullable Direction direction) {
        this(null, state, direction);
    }

    @ApiStatus.Internal
    public RotatedBlockProvider(
            @Nullable TypesBuilderData data,
            BlockStateProvider state, @Nullable Direction direction
    ) {
        super(data);
        this.state = state;
        this.direction = direction;
    }

    public BlockStateProvider getState() {
        return this.state;
    }

    public @Nullable Direction getDirection() {
        return this.direction;
    }

    @Override
    public BlockStateProviderType<?> getType() {
        return BlockStateProviderTypes.ROTATED;
    }

    @Override
    public RotatedBlockProvider copy(@Nullable TypesBuilderData newData) {
        return new RotatedBlockProvider(newData, this.state, this.direction);
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (!(obj instanceof RotatedBlockProvider)) return false;
        RotatedBlockProvider that = (RotatedBlockProvider) obj;
        if (!this.state.equals(that.state)) return false;
        return this.direction == that.direction;
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.state, this.direction);
    }
}
