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
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.generation.BlockStateProvider;
import com.github.retrooper.packetevents.protocol.world.generation.BlockStateProviderType;
import com.github.retrooper.packetevents.protocol.world.generation.BlockStateProviderTypes;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
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
public final class RandomBlockProvider extends AbstractMappedEntity implements BlockStateProvider {

    public static final NbtMapCodec<RandomBlockProvider> MAP_CODEC = new NbtMapCodec<RandomBlockProvider>() {
        @Override
        public RandomBlockProvider decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            MappedEntitySet<StateType.Mapped> blocks = MappedEntitySet.decode(
                    tag.getTagOrThrow("blocks"), wrapper, StateTypes.getRegistry());
            return new RandomBlockProvider(null, blocks);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, RandomBlockProvider value) throws NbtCodecException {
            tag.setTag("blocks", MappedEntitySet.encode(wrapper, value.blocks));
        }
    };

    private final MappedEntitySet<StateType.Mapped> blocks;

    public RandomBlockProvider(MappedEntitySet<StateType.Mapped> blocks) {
        this(null, blocks);
    }

    @ApiStatus.Internal
    public RandomBlockProvider(@Nullable TypesBuilderData data, MappedEntitySet<StateType.Mapped> blocks) {
        super(data);
        this.blocks = blocks;
    }

    public MappedEntitySet<StateType.Mapped> getBlocks() {
        return this.blocks;
    }

    @Override
    public BlockStateProviderType<?> getType() {
        return BlockStateProviderTypes.RANDOM_BLOCK;
    }

    @Override
    public RandomBlockProvider copy(@Nullable TypesBuilderData newData) {
        return new RandomBlockProvider(newData, this.blocks);
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (!(obj instanceof RandomBlockProvider)) return false;
        RandomBlockProvider that = (RandomBlockProvider) obj;
        return this.blocks.equals(that.blocks);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.blocks);
    }
}
