/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world.attributes;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.biome.BiomeEffects.MusicSettings;
import com.github.retrooper.packetevents.util.RandomWeightedList;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @versions 1.21.11+
 */
@NullMarked
public class BackgroundMusic {

    public static final NbtCodec<BackgroundMusic> CODEC = new NbtMapCodec<BackgroundMusic>() {
        @Override
        public BackgroundMusic decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
            MusicSettings defaultt = compound.getOrNull("default", MusicSettings.CODEC, wrapper);
            MusicSettings creative = compound.getOrNull("creative", MusicSettings.CODEC, wrapper);
            MusicSettings underwater = compound.getOrNull("underwater", MusicSettings.CODEC, wrapper);
            return new BackgroundMusic(defaultt, creative, underwater);
        }

        @Override
        public void encode(NBTCompound compound, PacketWrapper<?> wrapper, BackgroundMusic value) throws NbtCodecException {
            if (value.defaultMusic != null) {
                compound.set("default", value.defaultMusic, MusicSettings.CODEC, wrapper);
            }
            if (value.creativeMusic != null) {
                compound.set("creative", value.creativeMusic, MusicSettings.CODEC, wrapper);
            }
            if (value.underwaterMusic != null) {
                compound.set("underwater", value.underwaterMusic, MusicSettings.CODEC, wrapper);
            }
        }
    }.codec();

    public static final BackgroundMusic EMPTY = new BackgroundMusic(null, null, null);

    private final @Nullable MusicSettings defaultMusic;
    private final @Nullable MusicSettings creativeMusic;
    private final @Nullable MusicSettings underwaterMusic;

    public BackgroundMusic(
            @Nullable MusicSettings defaultMusic,
            @Nullable MusicSettings creativeMusic,
            @Nullable MusicSettings underwaterMusic
    ) {
        this.defaultMusic = defaultMusic;
        this.creativeMusic = creativeMusic;
        this.underwaterMusic = underwaterMusic;
    }

    @ApiStatus.Internal
    public RandomWeightedList<MusicSettings> asList() {
        List<RandomWeightedList.Entry<MusicSettings>> list = new ArrayList<>(3);
        if (this.defaultMusic != null) {
            list.add(new RandomWeightedList.Entry<>(this.defaultMusic, 0));
        }
        if (this.creativeMusic != null) {
            list.add(new RandomWeightedList.Entry<>(this.creativeMusic, 0));
        }
        if (this.underwaterMusic != null) {
            list.add(new RandomWeightedList.Entry<>(this.underwaterMusic, 0));
        }
        return new RandomWeightedList<>(list);
    }

    public @Nullable MusicSettings getDefaultMusic() {
        return this.defaultMusic;
    }

    public @Nullable MusicSettings getCreativeMusic() {
        return this.creativeMusic;
    }

    public @Nullable MusicSettings getUnderwaterMusic() {
        return this.underwaterMusic;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BackgroundMusic)) return false;
        BackgroundMusic that = (BackgroundMusic) obj;
        if (!Objects.equals(this.defaultMusic, that.defaultMusic)) return false;
        if (!Objects.equals(this.creativeMusic, that.creativeMusic)) return false;
        return Objects.equals(this.underwaterMusic, that.underwaterMusic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.defaultMusic, this.creativeMusic, this.underwaterMusic);
    }
}
