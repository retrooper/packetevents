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
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.valueproviders.ints.IntProvider;
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
public final class RandomizedIntStateProvider extends AbstractMappedEntity implements BlockStateProvider {

    public static final NbtMapCodec<RandomizedIntStateProvider> MAP_CODEC = new NbtMapCodec<RandomizedIntStateProvider>() {
        @Override
        public RandomizedIntStateProvider decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            BlockStateProvider source = tag.getOrThrow("source", BlockStateProvider.CODEC, wrapper);
            String property = tag.getStringTagValueOrThrow("property");
            IntProvider values = tag.getOrThrow("values", IntProvider.CODEC, wrapper);
            return new RandomizedIntStateProvider(null, source, property, values);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, RandomizedIntStateProvider value) throws NbtCodecException {
            tag.set("source", value.source, BlockStateProvider.CODEC, wrapper);
            tag.setTag("property", new NBTString(value.property));
            tag.set("values", value.values, IntProvider.CODEC, wrapper);
        }
    };

    private final BlockStateProvider source;
    private final String property;
    private final IntProvider values;

    public RandomizedIntStateProvider(BlockStateProvider source, String property, IntProvider values) {
        this(null, source, property, values);
    }

    @ApiStatus.Internal
    public RandomizedIntStateProvider(
            @Nullable TypesBuilderData data,
            BlockStateProvider source,
            String property,
            IntProvider values
    ) {
        super(data);
        this.source = source;
        this.property = property;
        this.values = values;
    }

    public BlockStateProvider getSource() {
        return this.source;
    }

    public String getProperty() {
        return this.property;
    }

    public IntProvider getValues() {
        return this.values;
    }

    @Override
    public BlockStateProviderType<?> getType() {
        return BlockStateProviderTypes.RANDOMIZED_INT;
    }

    @Override
    public RandomizedIntStateProvider copy(@Nullable TypesBuilderData newData) {
        return new RandomizedIntStateProvider(newData, this.source, this.property, this.values);
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (!(obj instanceof RandomizedIntStateProvider)) return false;
        RandomizedIntStateProvider that = (RandomizedIntStateProvider) obj;
        if (!this.source.equals(that.source)) return false;
        if (!this.property.equals(that.property)) return false;
        return this.values.equals(that.values);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.source, this.property, this.values);
    }
}
