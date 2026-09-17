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

package com.github.retrooper.packetevents.protocol.world.generation;

import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.generation.provider.SimpleStateProvider;
import com.github.retrooper.packetevents.protocol.world.states.BlockStateCodec;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 26.3+
 */
@NullMarked
public interface BlockStateProvider extends MappedEntity, CopyableEntity<BlockStateProvider>, DeepComparableEntity {

    NbtCodec<BlockStateProvider> TYPED_CODEC = new NbtMapCodec<BlockStateProvider>() {
        @Override
        public BlockStateProvider decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            BlockStateProviderType<?> type = tag.getOrThrow("type", BlockStateProviderType.CODEC, wrapper);
            return type.getCodec().decode(tag, wrapper);
        }

        @Override
        @SuppressWarnings("unchecked") // not unchecked
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, BlockStateProvider value) throws NbtCodecException {
            ((BlockStateProviderType<BlockStateProvider>) value.getType()).getCodec().encode(tag, wrapper, value);
            tag.set("type", value.getType(), BlockStateProviderType.CODEC, wrapper);
        }
    }.codec();
    NbtCodec<BlockStateProvider> DIRECT_CODEC = new NbtCodec<BlockStateProvider>() {
        @Override
        public BlockStateProvider decode(NBT tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            try {
                return TYPED_CODEC.decode(tag, wrapper);
            } catch (NbtCodecException ignored) {
            }
            WrappedBlockState state = BlockStateCodec.FULL_CODEC.decode(tag, wrapper);
            return new SimpleStateProvider(state);
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, BlockStateProvider value) throws NbtCodecException {
            if (value instanceof SimpleStateProvider) {
                WrappedBlockState state = ((SimpleStateProvider) value).getState();
                return BlockStateCodec.FULL_CODEC.encode(wrapper, state);
            }
            return TYPED_CODEC.encode(wrapper, value);
        }
    };
    NbtCodec<BlockStateProvider> CODEC = new NbtCodec<BlockStateProvider>() {
        private @MonotonicNonNull NbtCodec<BlockStateProvider> registryCodec;

        // resolve lazily to fix class loading order
        private NbtCodec<BlockStateProvider> registryCodec() {
            NbtCodec<BlockStateProvider> codec = this.registryCodec;
            if (codec == null) {
                codec = NbtCodecs.forRegistry(BlockStateProviders.getRegistry());
                this.registryCodec = codec;
            }
            return codec;
        }

        @Override
        public BlockStateProvider decode(NBT tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            if (tag instanceof NBTString) {
                return this.registryCodec().decode(tag, wrapper);
            }
            return DIRECT_CODEC.decode(tag, wrapper);
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, BlockStateProvider value) throws NbtCodecException {
            if (value.isRegistered()) {
                return this.registryCodec().encode(wrapper, value);
            }
            return DIRECT_CODEC.encode(wrapper, value);
        }
    };

    BlockStateProviderType<?> getType();
}
