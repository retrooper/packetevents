/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.sound.SoundCategory;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.concurrent.ThreadLocalRandom;

public class WrapperPlayServerEntitySoundEffect extends PacketWrapper<WrapperPlayServerEntitySoundEffect> {

    private Sound sound;
    private SoundCategory soundCategory;
    private int entityId;
    private float volume;
    private float pitch;
    private long seed;

    public WrapperPlayServerEntitySoundEffect(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntitySoundEffect(int soundId, SoundCategory soundCategory, int entityId, float volume, float pitch) {
        this(Sounds.getById(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), soundId),
                soundCategory, entityId, volume, pitch);
    }

    public WrapperPlayServerEntitySoundEffect(Sound sound, SoundCategory soundCategory, int entityId, float volume, float pitch) {
        this(sound, soundCategory, entityId, volume, pitch, ThreadLocalRandom.current().nextLong());
    }

    public WrapperPlayServerEntitySoundEffect(Sound sound, SoundCategory soundCategory, int entityId, float volume, float pitch, long seed) {
        super(PacketType.Play.Server.ENTITY_SOUND_EFFECT);
        this.sound = sound;
        this.soundCategory = soundCategory;
        this.entityId = entityId;
        this.volume = volume;
        this.pitch = pitch;
        this.seed = seed;
    }

    @Override
    public void read() {
        this.sound = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3) ? Sound.read(this)
                : Sounds.getById(this.serverVersion.toClientVersion(), this.readVarInt());
        this.soundCategory = SoundCategory.fromId(readVarInt());
        this.entityId = readVarInt();
        this.volume = readFloat();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_10)) {
            pitch = readFloat();
        }
        else {
            pitch = readUnsignedByte() / 63.5F;
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            this.seed = this.readLong();
        }
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
            Sound.write(this, this.sound);
        } else {
            this.writeVarInt(this.sound.getId(this.serverVersion.toClientVersion()));
        }
        writeVarInt(this.soundCategory.ordinal());
        writeVarInt(this.entityId);
        writeFloat(this.volume);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_10)) {
            writeFloat(pitch);
        }
        else {
            writeByte((int) (pitch * 63.5F));
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            this.writeLong(this.seed);
        }
    }

    @Override
    public void copy(WrapperPlayServerEntitySoundEffect wrapper) {
        this.sound = wrapper.sound;
        this.soundCategory = wrapper.soundCategory;
        this.entityId = wrapper.entityId;
        this.volume = wrapper.volume;
        this.pitch = wrapper.pitch;
        this.seed = wrapper.seed;
    }

    public Sound getSound() {
        return this.sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    @Deprecated
    public int getSoundId() {
        return this.getSound().getId(this.serverVersion.toClientVersion());
    }

    @Deprecated
    public void setSoundId(int soundId) {
        this.setSound(Sounds.getById(this.serverVersion.toClientVersion(), soundId));
    }

    public SoundCategory getSoundCategory() {
        return soundCategory;
    }

    public void setSoundCategory(SoundCategory soundCategory) {
        this.soundCategory = soundCategory;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
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

    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }
}
