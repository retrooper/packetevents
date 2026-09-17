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

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@NullMarked
public final class NormalNoise {

    public static final NbtCodec<NormalNoise> CODEC = new NbtMapCodec<NormalNoise>() {
        @Override
        public NormalNoise decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            double baseAmplitude = tag.getNumberTagValueOrDefault("base_amplitude", 1d).doubleValue();
            int baseOctave = tag.getNumberTagValueOrThrow("base_octave").intValue();
            int octaveCount = tag.getNumberTagValueOrDefault("octave_count", 1).intValue();
            Normalization normalize = tag.getOr("normalize", Normalization.CODEC, Normalization.ENABLED, wrapper);
            List<Double> amplitudeModifiers = tag.getListOrEmpty("amplitude_modifiers", NbtCodecs.DOUBLE, wrapper);
            return new NormalNoise(baseAmplitude, baseOctave, octaveCount, normalize, amplitudeModifiers);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, NormalNoise value) throws NbtCodecException {
            if (value.baseAmplitude != 1d) {
                tag.setTag("base_amplitude", new NBTDouble(value.baseAmplitude));
            }
            tag.setTag("base_octave", new NBTInt(value.baseOctave));
            if (value.octaveCount != 1) {
                tag.setTag("octave_count", new NBTInt(value.octaveCount));
            }
            if (value.normalize != Normalization.ENABLED) {
                tag.set("normalize", value.normalize, Normalization.CODEC, wrapper);
            }
            if (!value.amplitudeModifiers.isEmpty()) {
                tag.setList("amplitude_modifiers", value.amplitudeModifiers, NbtCodecs.DOUBLE, wrapper);
            }
        }
    }.codec();

    private final double baseAmplitude;
    private final int baseOctave;
    private final int octaveCount;
    private final Normalization normalize;
    private final List<Double> amplitudeModifiers;

    public NormalNoise(double baseAmplitude, int baseOctave) {
        this(baseAmplitude, baseOctave, 1, Normalization.ENABLED, Collections.emptyList());
    }

    public NormalNoise(
            double baseAmplitude, int baseOctave, int octaveCount,
            Normalization normalize, List<Double> amplitudeModifiers
    ) {
        this.baseAmplitude = baseAmplitude;
        this.baseOctave = baseOctave;
        this.octaveCount = octaveCount;
        this.normalize = normalize;
        this.amplitudeModifiers = Collections.unmodifiableList(amplitudeModifiers);
    }

    public double getBaseAmplitude() {
        return this.baseAmplitude;
    }

    public int getBaseOctave() {
        return this.baseOctave;
    }

    public int getOctaveCount() {
        return this.octaveCount;
    }

    public Normalization getNormalize() {
        return this.normalize;
    }

    public List<Double> getAmplitudeModifiers() {
        return this.amplitudeModifiers;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        NormalNoise that = (NormalNoise) obj;
        if (Double.compare(that.baseAmplitude, this.baseAmplitude) != 0) return false;
        if (this.baseOctave != that.baseOctave) return false;
        if (this.octaveCount != that.octaveCount) return false;
        if (this.normalize != that.normalize) return false;
        return this.amplitudeModifiers.equals(that.amplitudeModifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.baseAmplitude, this.baseOctave, this.octaveCount, this.normalize, this.amplitudeModifiers);
    }

    public enum Normalization {

        DISABLED,
        ENABLED,
        LEGACY,
        ;

        public static final NbtCodec<Normalization> CODEC = new NbtCodec<Normalization>() {
            @Override
            public Normalization decode(NBT nbt, PacketWrapper<?> wrapper) throws NbtCodecException {
                if (nbt instanceof NBTNumber) {
                    return ((NBTNumber) nbt).getAsByte() != 0 ? ENABLED : DISABLED;
                }
                String name = nbt.castOrThrow(NBTString.class).getValue();
                if ("legacy".equals(name)) {
                    return LEGACY;
                }
                throw new NbtCodecException("Invalid normalization type: " + name);
            }

            @Override
            public NBT encode(PacketWrapper<?> wrapper, Normalization value) {
                switch (value) {
                    case ENABLED:
                        return new NBTByte(true);
                    case DISABLED:
                        return new NBTByte(false);
                    case LEGACY:
                        return new NBTString("legacy");
                    default:
                        throw new IllegalArgumentException("Unknown normalization: " + value);
                }
            }
        };
    }
}
