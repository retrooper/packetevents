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
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.biome.BiomeEffects.AdditionsSettings;
import com.github.retrooper.packetevents.protocol.world.biome.BiomeEffects.MoodSettings;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @versions 1.21.11+
 */
@NullMarked
public class AmbientSounds {

    public static final NbtCodec<AmbientSounds> CODEC = new NbtMapCodec<AmbientSounds>() {
        @Override
        public AmbientSounds decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
            Sound sound = compound.getOrNull("loop", Sound.CODEC, wrapper);
            MoodSettings mood = compound.getOrNull("mood", MoodSettings.CODEC, wrapper);
            List<AdditionsSettings> additions = compound.getOr("additions", AdditionsSettings.LIST_CODEC, Collections.emptyList(), wrapper);
            return new AmbientSounds(sound, mood, additions);
        }

        @Override
        public void encode(NBTCompound compound, PacketWrapper<?> wrapper, AmbientSounds value) throws NbtCodecException {
            if (value.loop != null) {
                compound.set("loop", value.loop, Sound.CODEC, wrapper);
            }
            if (value.mood != null) {
                compound.set("mood", value.mood, MoodSettings.CODEC, wrapper);
            }
            if (!value.additions.isEmpty()) {
                compound.set("additions", value.additions, AdditionsSettings.LIST_CODEC, wrapper);
            }
        }
    }.codec();

    public static final AmbientSounds EMPTY = new AmbientSounds(null, null, Collections.emptyList());

    private final @Nullable Sound loop;
    private final @Nullable MoodSettings mood;
    private final List<AdditionsSettings> additions;

    public AmbientSounds(@Nullable Sound loop, @Nullable MoodSettings mood, List<AdditionsSettings> additions) {
        this.loop = loop;
        this.mood = mood;
        this.additions = additions;
    }

    public @Nullable Sound getLoop() {
        return this.loop;
    }

    public @Nullable MoodSettings getMood() {
        return this.mood;
    }

    public List<AdditionsSettings> getAdditions() {
        return this.additions;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AmbientSounds)) return false;
        AmbientSounds that = (AmbientSounds) obj;
        if (!Objects.equals(this.loop, that.loop)) return false;
        if (!Objects.equals(this.mood, that.mood)) return false;
        return this.additions.equals(that.additions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.loop, this.mood, this.additions);
    }
}
