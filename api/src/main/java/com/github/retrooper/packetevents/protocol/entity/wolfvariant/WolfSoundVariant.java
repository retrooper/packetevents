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

package com.github.retrooper.packetevents.protocol.entity.wolfvariant;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @versions 1.21.5+
 */
@NullMarked
public interface WolfSoundVariant extends MappedEntity, CopyableEntity<WolfSoundVariant>, DeepComparableEntity {

    NbtCodec<WolfSoundVariant> CODEC = new NbtCodec<WolfSoundVariant>() {
        @Override
        public WolfSoundVariant decode(NBT tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_26_1)) {
                WolfSoundSet sounds = WolfSoundSet.CODEC.decode(tag, wrapper);
                return new StaticWolfSoundVariant(sounds, sounds);
            }
            NBTCompound compound = (NBTCompound) tag;
            WolfSoundSet adultSounds = compound.getOrThrow("adult_sounds", WolfSoundSet.CODEC, wrapper);
            WolfSoundSet babySounds = compound.getOrThrow("baby_sounds", WolfSoundSet.CODEC, wrapper);
            return new StaticWolfSoundVariant(adultSounds, babySounds);
        }

        @Override
        public NBT encode(PacketWrapper<?> wrapper, WolfSoundVariant value) throws NbtCodecException {
            if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_26_1)) {
                return WolfSoundSet.CODEC.encode(wrapper, value.getAdultSounds());
            }
            NBTCompound tag = new NBTCompound();
            tag.set("adult_sounds", value.getAdultSounds(), WolfSoundSet.CODEC, wrapper);
            tag.set("baby_sounds", value.getBabySounds(), WolfSoundSet.CODEC, wrapper);
            return tag;
        }
    };

    /**
     * @versions 26.1+
     */
    WolfSoundSet getAdultSounds();

    /**
     * @versions 26.1+
     */
    WolfSoundSet getBabySounds();

    /**
     * @versions 1.21.5-1.21.11
     */
    @Deprecated
    default Sound getAmbientSound() {
        return this.getAdultSounds().getAmbientSound();
    }

    /**
     * @versions 1.21.5-1.21.11
     */
    @Deprecated
    default Sound getDeathSound() {
        return this.getAdultSounds().getDeathSound();
    }

    /**
     * @versions 1.21.5-1.21.11
     */
    @Deprecated
    default Sound getGrowlSound() {
        return this.getAdultSounds().getGrowlSound();
    }

    /**
     * @versions 1.21.5-1.21.11
     */
    @Deprecated
    default Sound getHurtSound() {
        return this.getAdultSounds().getHurtSound();
    }

    /**
     * @versions 1.21.5-1.21.11
     */
    @Deprecated
    default Sound getPantSound() {
        return this.getAdultSounds().getPantSound();
    }

    /**
     * @versions 1.21.5-1.21.11
     */
    @Deprecated
    default Sound getWhineSound() {
        return this.getAdultSounds().getWhineSound();
    }

    static WolfSoundVariant read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntity(WolfSoundVariants.getRegistry());
    }

    static void write(PacketWrapper<?> wrapper, WolfSoundVariant variant) {
        wrapper.writeMappedEntity(variant);
    }

    @Deprecated
    static WolfSoundVariant decode(NBT tag, ClientVersion version, @Nullable TypesBuilderData data) {
        return CODEC.decode(tag, PacketWrapper.createDummyWrapper(version)).copy(data);
    }

    @Deprecated
    static NBT encode(WolfSoundVariant variant, ClientVersion version) {
        return CODEC.encode(PacketWrapper.createDummyWrapper(version), variant);
    }
}
