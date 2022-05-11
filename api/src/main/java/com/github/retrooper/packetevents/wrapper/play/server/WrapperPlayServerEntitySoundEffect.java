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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntitySoundEffect extends PacketWrapper<WrapperPlayServerEntitySoundEffect> {
  private int soundId;
  private SoundCategory soundCategory;
  private int entityId;
  private float volume;
  private float pitch;

  public WrapperPlayServerEntitySoundEffect(PacketSendEvent event) {
    super(event);
  }

  public WrapperPlayServerEntitySoundEffect(int soundId, SoundCategory soundCategory, int entityId, float volume, float pitch) {
    super(PacketType.Play.Server.ENTITY_SOUND_EFFECT);
    this.soundId = soundId;
    this.soundCategory = soundCategory;
    this.entityId = entityId;
    this.volume = volume;
    this.pitch = pitch;
  }

  @Override
  public void read() {
    this.soundId = readVarInt();
    this.soundCategory = SoundCategory.VALUES[readVarInt()];
    this.entityId = readVarInt();
    this.volume = readFloat();
    this.pitch = readFloat();
  }

  @Override
  public void write() {
    writeVarInt(this.soundId);
    writeVarInt(this.soundCategory.ordinal());
    writeVarInt(this.entityId);
    writeFloat(this.volume);
    writeFloat(this.pitch);
  }

  @Override
  public void copy(WrapperPlayServerEntitySoundEffect wrapper) {
    this.soundId = wrapper.soundId;
    this.soundCategory = wrapper.soundCategory;
    this.entityId = wrapper.entityId;
    this.volume = wrapper.volume;
    this.pitch = wrapper.pitch;
  }

  public int getSoundId() {
    return soundId;
  }

  public void setSoundId(int soundId) {
    this.soundId = soundId;
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

  @Override
  public String toString() {
    return "WrapperPlayServerEntitySoundEffect{" +
            "soundId=" + soundId +
            ", soundCategory=" + soundCategory +
            ", entityId=" + entityId +
            ", volume=" + volume +
            ", pitch=" + pitch +
            '}';
  }
}
