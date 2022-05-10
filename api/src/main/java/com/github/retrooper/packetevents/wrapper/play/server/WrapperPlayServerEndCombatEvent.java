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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEndCombatEvent extends PacketWrapper<WrapperPlayServerEndCombatEvent> {
  private int duration;
  private int entityId;

  public WrapperPlayServerEndCombatEvent(PacketSendEvent event) {
    super(event);
  }

  public WrapperPlayServerEndCombatEvent(int duration, int entityId) {
    super(PacketType.Play.Server.END_COMBAT_EVENT);
    this.duration = duration;
    this.entityId = entityId;
  }

  @Override
  public void read() {
    this.duration = readVarInt();
    this.entityId = readInt();
  }

  @Override
  public void write() {
    writeVarInt(this.duration);
    writeInt(this.entityId);
  }

  @Override
  public void copy(WrapperPlayServerEndCombatEvent wrapper) {
    this.duration = wrapper.duration;
    this.entityId = wrapper.entityId;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public int getEntityId() {
    return entityId;
  }

  public void setEntityId(int entityId) {
    this.entityId = entityId;
  }
}
