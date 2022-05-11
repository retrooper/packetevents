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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.sound.SoundCategory;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerSoundEffect extends PacketWrapper<WrapperPlayServerSoundEffect> {
    private int soundID;
    private SoundCategory soundCategory;
    private Vector3i effectPosition;
    private float volume;
    private float pitch;
    public WrapperPlayServerSoundEffect(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSoundEffect(int soundID, SoundCategory soundCategory,
                                        Vector3i effectPosition, float volume, float pitch) {
        super(PacketType.Play.Server.SOUND_EFFECT);
        this.soundID= soundID;
        this.soundCategory = soundCategory;
        this.effectPosition = effectPosition;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void read() {
        soundID = readVarInt();
        soundCategory = SoundCategory.VALUES[readVarInt()];
        effectPosition = new Vector3i(readInt(), readInt(), readInt());
        volume = readFloat();
        pitch = readFloat();
    }

    @Override
    public void copy(WrapperPlayServerSoundEffect wrapper) {
        soundID = wrapper.soundID;
        soundCategory = wrapper.soundCategory;
        effectPosition = wrapper.effectPosition;
        volume = wrapper.volume;
        pitch = wrapper.pitch;
    }

    @Override
    public void write() {
        writeVarInt(soundID);
        writeVarInt(soundCategory.ordinal());
        writeInt(effectPosition.x);
        writeInt(effectPosition.y);
        writeInt(effectPosition.z);
        writeFloat(volume);
        writeFloat(pitch);
    }

    public int getSoundId() {
        return soundID;
    }

    public void setSoundId(int soundID) {
        this.soundID = soundID;
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
}
