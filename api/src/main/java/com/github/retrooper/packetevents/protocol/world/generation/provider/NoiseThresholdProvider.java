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
public final class NoiseThresholdProvider extends AbstractMappedEntity implements BlockStateProvider {

    public static final NbtMapCodec<NoiseThresholdProvider> MAP_CODEC = new NbtMapCodec<NoiseThresholdProvider>() {
        @Override
        public NoiseThresholdProvider decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            long seed = tag.getNumberTagValueOrThrow("seed").longValue();
            NormalNoise noise = tag.getOrThrow("noise", NormalNoise.CODEC, wrapper);
            float scale = tag.getNumberTagValueOrThrow("scale").floatValue();
            float threshold = tag.getNumberTagValueOrThrow("threshold").floatValue();
            float highChance = tag.getNumberTagValueOrThrow("high_chance").floatValue();
            WrappedBlockState defaultState = tag.getOrThrow("default_state", BlockStateCodec.CODEC, wrapper);
            List<WrappedBlockState> lowStates = tag.getListOrThrow("low_states", BlockStateCodec.CODEC, wrapper);
            List<WrappedBlockState> highStates = tag.getListOrThrow("high_states", BlockStateCodec.CODEC, wrapper);
            return new NoiseThresholdProvider(null, seed, noise, scale, threshold, highChance,
                    defaultState, lowStates, highStates);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, NoiseThresholdProvider value) throws NbtCodecException {
            tag.setTag("seed", new NBTLong(value.seed));
            tag.set("noise", value.noise, NormalNoise.CODEC, wrapper);
            tag.setTag("scale", new NBTFloat(value.scale));
            tag.setTag("threshold", new NBTFloat(value.threshold));
            tag.setTag("high_chance", new NBTFloat(value.highChance));
            tag.set("default_state", value.defaultState, BlockStateCodec.CODEC, wrapper);
            tag.setList("low_states", value.lowStates, BlockStateCodec.CODEC, wrapper);
            tag.setList("high_states", value.highStates, BlockStateCodec.CODEC, wrapper);
        }
    };

    private final long seed;
    private final NormalNoise noise;
    private final float scale;
    private final float threshold;
    private final float highChance;
    private final WrappedBlockState defaultState;
    private final List<WrappedBlockState> lowStates;
    private final List<WrappedBlockState> highStates;

    public NoiseThresholdProvider(
            long seed, NormalNoise noise, float scale, float threshold, float highChance,
            WrappedBlockState defaultState, List<WrappedBlockState> lowStates, List<WrappedBlockState> highStates
    ) {
        this(null, seed, noise, scale, threshold, highChance, defaultState, lowStates, highStates);
    }

    @ApiStatus.Internal
    public NoiseThresholdProvider(
            @Nullable TypesBuilderData data, long seed, NormalNoise noise, float scale,
            float threshold, float highChance, WrappedBlockState defaultState,
            List<WrappedBlockState> lowStates, List<WrappedBlockState> highStates
    ) {
        super(data);
        this.seed = seed;
        this.noise = noise;
        this.scale = scale;
        this.threshold = threshold;
        this.highChance = highChance;
        this.defaultState = defaultState;
        this.lowStates = Collections.unmodifiableList(lowStates);
        this.highStates = Collections.unmodifiableList(highStates);
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

    public float getThreshold() {
        return this.threshold;
    }

    public float getHighChance() {
        return this.highChance;
    }

    public WrappedBlockState getDefaultState() {
        return this.defaultState;
    }

    public List<WrappedBlockState> getLowStates() {
        return this.lowStates;
    }

    public List<WrappedBlockState> getHighStates() {
        return this.highStates;
    }

    @Override
    public BlockStateProviderType<?> getType() {
        return BlockStateProviderTypes.NOISE_THRESHOLD;
    }

    @Override
    public NoiseThresholdProvider copy(@Nullable TypesBuilderData newData) {
        return new NoiseThresholdProvider(newData, this.seed, this.noise, this.scale,
                this.threshold, this.highChance, this.defaultState, this.lowStates, this.highStates);
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (!(obj instanceof NoiseThresholdProvider)) return false;
        NoiseThresholdProvider that = (NoiseThresholdProvider) obj;
        if (this.seed != that.seed) return false;
        if (Float.compare(that.scale, this.scale) != 0) return false;
        if (Float.compare(that.threshold, this.threshold) != 0) return false;
        if (Float.compare(that.highChance, this.highChance) != 0) return false;
        if (!this.noise.equals(that.noise)) return false;
        if (!this.defaultState.equals(that.defaultState)) return false;
        if (!this.lowStates.equals(that.lowStates)) return false;
        return this.highStates.equals(that.highStates);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.seed, this.noise, this.scale, this.threshold,
                this.highChance, this.defaultState, this.lowStates, this.highStates);
    }
}
