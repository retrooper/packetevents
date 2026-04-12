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

package com.github.retrooper.packetevents.protocol.entity.wolfvariant;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * @versions 1.21.5+
 */
@NullMarked
public final class WolfSoundSet {

    public static final NbtCodec<WolfSoundSet> CODEC = new NbtMapCodec<WolfSoundSet>() {
        @Override
        public WolfSoundSet decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            Sound ambientSound = tag.getOrThrow("ambient_sound", Sound.CODEC, wrapper);
            Sound deathSound = tag.getOrThrow("death_sound", Sound.CODEC, wrapper);
            Sound growlSound = tag.getOrThrow("growl_sound", Sound.CODEC, wrapper);
            Sound hurtSound = tag.getOrThrow("hurt_sound", Sound.CODEC, wrapper);
            Sound pantSound = tag.getOrThrow("pant_sound", Sound.CODEC, wrapper);
            Sound whineSound = tag.getOrThrow("whine_sound", Sound.CODEC, wrapper);
            Sound stepSound = wrapper.getServerVersion().isOlderThan(ServerVersion.V_26_1) ? Sounds.ENTITY_WOLF_STEP
                    : tag.getOrThrow("step_sound", Sound.CODEC, wrapper);
            return new WolfSoundSet(ambientSound, deathSound, growlSound, hurtSound, pantSound, whineSound, stepSound);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, WolfSoundSet value) throws NbtCodecException {
            tag.set("ambient_sound", value.ambientSound, Sound.CODEC, wrapper);
            tag.set("death_sound", value.deathSound, Sound.CODEC, wrapper);
            tag.set("growl_sound", value.growlSound, Sound.CODEC, wrapper);
            tag.set("hurt_sound", value.hurtSound, Sound.CODEC, wrapper);
            tag.set("pant_sound", value.pantSound, Sound.CODEC, wrapper);
            tag.set("whine_sound", value.whineSound, Sound.CODEC, wrapper);
            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_1)) {
                tag.set("step_sound", value.stepSound, Sound.CODEC, wrapper);
            }
        }
    }.codec();

    private final Sound ambientSound;
    private final Sound deathSound;
    private final Sound growlSound;
    private final Sound hurtSound;
    private final Sound pantSound;
    private final Sound whineSound;
    /**
     * @versions 26.1+
     */
    private final Sound stepSound;

    public WolfSoundSet(
            Sound ambientSound, Sound deathSound, Sound growlSound,
            Sound hurtSound, Sound pantSound, Sound whineSound, Sound stepSound
    ) {
        this.ambientSound = ambientSound;
        this.deathSound = deathSound;
        this.growlSound = growlSound;
        this.hurtSound = hurtSound;
        this.pantSound = pantSound;
        this.whineSound = whineSound;
        this.stepSound = stepSound;
    }

    @ApiStatus.Internal
    public static WolfSoundSet getOrThrow(String soundPrefix, Sound stepSound) {
        VersionedRegistry<Sound> soundRegistry = Sounds.getRegistry();
        Sound ambientSound = soundRegistry.getByNameOrThrow(soundPrefix + "ambient");
        Sound deathSound = soundRegistry.getByNameOrThrow(soundPrefix + "death");
        Sound growlSound = soundRegistry.getByNameOrThrow(soundPrefix + "growl");
        Sound hurtSound = soundRegistry.getByNameOrThrow(soundPrefix + "hurt");
        Sound pantSound = soundRegistry.getByNameOrThrow(soundPrefix + "pant");
        Sound whineSound = soundRegistry.getByNameOrThrow(soundPrefix + "whine");
        return new WolfSoundSet(ambientSound, deathSound, growlSound, hurtSound, pantSound, whineSound, stepSound);
    }

    public Sound getAmbientSound() {
        return this.ambientSound;
    }

    public Sound getDeathSound() {
        return this.deathSound;
    }

    public Sound getGrowlSound() {
        return this.growlSound;
    }

    public Sound getHurtSound() {
        return this.hurtSound;
    }

    public Sound getPantSound() {
        return this.pantSound;
    }

    public Sound getWhineSound() {
        return this.whineSound;
    }

    /**
     * @versions 26.1+
     */
    public Sound getStepSound() {
        return this.stepSound;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WolfSoundSet)) return false;
        WolfSoundSet that = (WolfSoundSet) obj;
        if (!this.ambientSound.equals(that.ambientSound)) return false;
        if (!this.deathSound.equals(that.deathSound)) return false;
        if (!this.growlSound.equals(that.growlSound)) return false;
        if (!this.hurtSound.equals(that.hurtSound)) return false;
        if (!this.pantSound.equals(that.pantSound)) return false;
        if (!this.whineSound.equals(that.whineSound)) return false;
        return this.stepSound.equals(that.stepSound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.ambientSound, this.deathSound, this.growlSound, this.hurtSound, this.pantSound, this.whineSound, this.stepSound);
    }
}
