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
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTLong;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.generation.BlockStateProvider;
import com.github.retrooper.packetevents.protocol.world.generation.BlockStateProviderType;
import com.github.retrooper.packetevents.protocol.world.generation.BlockStateProviderTypes;
import com.github.retrooper.packetevents.protocol.world.generation.NormalNoise;
import com.github.retrooper.packetevents.protocol.world.states.BlockStateCodec;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public final class NoiseProvider extends AbstractMappedEntity implements BlockStateProvider {

    public static final NbtMapCodec<NoiseProvider> MAP_CODEC = new NbtMapCodec<NoiseProvider>() {
        @Override
        public NoiseProvider decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            long seed = tag.getNumberTagValueOrThrow("seed").longValue();
            NormalNoise noise = tag.getOrThrow("noise", NormalNoise.CODEC, wrapper);
            float scale = tag.getNumberTagValueOrThrow("scale").floatValue();
            List<WrappedBlockState> states = tag.getListOrThrow("states", BlockStateCodec.CODEC, wrapper);
            return new NoiseProvider(null, seed, noise, scale, states);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, NoiseProvider value) throws NbtCodecException {
            tag.setTag("seed", new NBTLong(value.seed));
            tag.set("noise", value.noise, NormalNoise.CODEC, wrapper);
            tag.setTag("scale", new NBTFloat(value.scale));
            tag.setList("states", value.states, BlockStateCodec.CODEC, wrapper);
        }
    };

    private final long seed;
    private final NormalNoise noise;
    private final float scale;
    private final List<WrappedBlockState> states;

    public NoiseProvider(long seed, NormalNoise noise, float scale, List<WrappedBlockState> states) {
        this(null, seed, noise, scale, states);
    }

    @ApiStatus.Internal
    public NoiseProvider(
            @Nullable TypesBuilderData data, long seed,
            NormalNoise noise, float scale, List<WrappedBlockState> states
    ) {
        super(data);
        this.seed = seed;
        this.noise = noise;
        this.scale = scale;
        this.states = Collections.unmodifiableList(states);
    }

    public long getSeed() {
        return this.seed;
    }

    public NormalNoise getNoise() {
        return this.noise;
    }

    public float getScale() {
        return this.scale;
    }

    public List<WrappedBlockState> getStates() {
        return this.states;
    }

    @Override
    public BlockStateProviderType<?> getType() {
        return BlockStateProviderTypes.NOISE;
    }

    @Override
    public NoiseProvider copy(@Nullable TypesBuilderData newData) {
        return new NoiseProvider(newData, this.seed, this.noise, this.scale, this.states);
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (!(obj instanceof NoiseProvider)) return false;
        NoiseProvider that = (NoiseProvider) obj;
        if (this.seed != that.seed) return false;
        if (Float.compare(that.scale, this.scale) != 0) return false;
        if (!this.noise.equals(that.noise)) return false;
        return this.states.equals(that.states);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.seed, this.noise, this.scale, this.states);
    }
}
