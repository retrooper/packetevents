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

package com.github.retrooper.packetevents.protocol.valueproviders.ints;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.util.WeightedList;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

/**
 * @versions 1.17+
 */
@NullMarked
public final class WeightedListInt implements IntProvider {

    public static final NbtMapCodec<WeightedListInt> MAP_CODEC = new NbtMapCodec<WeightedListInt>() {
        private final NbtCodec<WeightedList<IntProvider>> codec = WeightedList.codec(IntProvider.CODEC);

        @Override
        public WeightedListInt decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            WeightedList<IntProvider> distribution = tag.getOrThrow("distribution", this.codec, wrapper);
            return new WeightedListInt(distribution);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, WeightedListInt value) throws NbtCodecException {
            tag.set("distribution", value.distribution, this.codec, wrapper);
        }
    };

    private final WeightedList<IntProvider> distribution;
    private final int minValue;
    private final int maxValue;

    public WeightedListInt(WeightedList<IntProvider> distribution) {
        this.distribution = distribution;

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (WeightedList.Entry<IntProvider> entry : distribution.getEntries()) {
            IntProvider provider = entry.getValue();
            min = Math.min(min, provider.getMin());
            max = Math.max(max, provider.getMax());
        }
        this.minValue = min;
        this.maxValue = max;
    }

    @Override
    public IntProviderType<?> getType() {
        return IntProviderTypes.WEIGHTED_LIST;
    }

    @Override
    public int getSample(Random random) {
        int totalWeight = 0;
        for (WeightedList.Entry<IntProvider> entry : this.distribution.getEntries()) {
            totalWeight += entry.getWeight();
        }
        if (totalWeight == 0) {
            throw new IllegalStateException("Weighted list has no elements");
        }

        int selection = random.nextInt(totalWeight);
        for (WeightedList.Entry<IntProvider> entry : this.distribution.getEntries()) {
            selection -= entry.getWeight();
            if (selection < 0) {
                return entry.getValue().getSample(random);
            }
        }

        // unreachable
        throw new IllegalStateException(selection + " exceeded total weight " + totalWeight);
    }

    @Override
    public int getMin() {
        return this.minValue;
    }

    @Override
    public int getMax() {
        return this.maxValue;
    }

    public WeightedList<IntProvider> getDistribution() {
        return this.distribution;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        WeightedListInt that = (WeightedListInt) obj;
        return this.distribution.equals(that.distribution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.distribution);
    }
}
