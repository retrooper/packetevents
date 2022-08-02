/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.sound.SoundCategory;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

public class WrapperPlayServerSoundEffect extends PacketWrapper<WrapperPlayServerSoundEffect> {
    private @Nullable String soundName;
    private int soundId;
    private SoundCategory soundCategory;
    private Vector3i effectPosition;
    private float volume;
    private float pitch;
    private long seed;

    public WrapperPlayServerSoundEffect(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSoundEffect(int soundId, SoundCategory soundCategory,
                                        Vector3i effectPosition, float volume, float pitch) {
        this(soundId, soundCategory, effectPosition, volume, pitch, -1);
    }

    @Deprecated
    public WrapperPlayServerSoundEffect(@Nullable String soundName, SoundCategory soundCategory,
                                        Vector3i effectPosition, float volume, float pitch, long seed) {
        super(PacketType.Play.Server.SOUND_EFFECT);
        this.soundName = soundName;
        this.soundCategory = soundCategory;
        this.effectPosition = effectPosition;
        this.volume = volume;
        this.pitch = pitch;
        this.seed = seed;
    }

    public WrapperPlayServerSoundEffect(int soundId, SoundCategory soundCategory,
                                        Vector3i effectPosition, float volume, float pitch, long seed) {
        super(PacketType.Play.Server.SOUND_EFFECT);
        this.soundId = soundId;
        this.soundCategory = soundCategory;
        this.effectPosition = effectPosition;
        this.volume = volume;
        this.pitch = pitch;
        this.seed = seed;
    }

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            soundId = readVarInt();
            soundCategory = SoundCategory.fromId(readVarInt());
        } else {
            soundName = readString();
        }
        effectPosition = new Vector3i(readInt(), readInt(), readInt());
        volume = readFloat();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            pitch = readFloat();
        } else {
            pitch = readUnsignedByte();
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            this.seed = readLong();
        }
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            writeVarInt(soundId);
            writeVarInt(soundCategory.ordinal());
        } else {
            writeString(soundName);
        }
        writeInt(effectPosition.x);
        writeInt(effectPosition.y);
        writeInt(effectPosition.z);
        writeFloat(volume);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            writeFloat(pitch);
        } else {
            writeByte((byte) pitch);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            writeLong(seed);
        }
    }

    @Override
    public void copy(WrapperPlayServerSoundEffect wrapper) {
        this.soundName = wrapper.soundName;
        soundId = wrapper.soundId;
        soundCategory = wrapper.soundCategory;
        effectPosition = wrapper.effectPosition;
        volume = wrapper.volume;
        pitch = wrapper.pitch;
        seed = wrapper.seed;
    }

    /**
     * On server versions equals or older than 1.8, this is the sound name.
     * On server versions newer than 1.8, this is the {@link #getSoundId()}.
     *
     * @return the sound name
     * @deprecated use {@link #getSoundId()} instead on modern versions
     */
    @Deprecated
    public @Nullable String getSoundName() {
        return soundName;
    }

    /**
     * On server versions equals or older than 1.8, this is the sound name.
     * On server versions newer than 1.8, this is the {@link #setSoundId(int)}.
     *
     * @param soundName the sound name
     * @deprecated use {@link #setSoundId(int)} instead on modern versions
     */
    @Deprecated
    public void setSoundName(@Nullable String soundName) {
        this.soundName = soundName;
    }

    public int getSoundId() {
        return soundId;
    }

    public void setSoundId(int soundID) {
        this.soundId = soundID;
    }

    public SoundCategory getSoundCategory() {
        return soundCategory;
    }

    public void setSoundCategory(SoundCategory soundCategory) {
        this.soundCategory = soundCategory;
    }

    public Vector3i getEffectPosition() {
        return effectPosition;
    }

    public void setEffectPosition(Vector3i effectPosition) {
        this.effectPosition = effectPosition;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * Seeds only exist in server version 1.19+
     *
     * @return the seed
     */
    public long getSeed() {
        return seed;
    }

    /**
     * Seeds only exist in server version 1.19+
     *
     * @param seed the seed to set
     */
    public void setSeed(long seed) {
        this.seed = seed;
    }
}
