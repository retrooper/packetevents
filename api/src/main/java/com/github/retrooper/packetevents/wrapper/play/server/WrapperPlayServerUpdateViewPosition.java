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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerUpdateViewPosition extends PacketWrapper<WrapperPlayServerUpdateViewPosition> {
  private int chunkX;
  private int chunkZ;

  public WrapperPlayServerUpdateViewPosition(PacketSendEvent event) {
    super(event);
  }

  public WrapperPlayServerUpdateViewPosition(int chunkX, int chunkZ) {
    super(PacketType.Play.Server.UPDATE_VIEW_POSITION);
    this.chunkX = chunkX;
    this.chunkZ = chunkZ;
  }

  @Override
  public void read() {
    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
      this.chunkX = readVarInt();
      this.chunkZ = readVarInt();
    } else {
      this.chunkX = readInt();
      this.chunkZ = readInt();
    }
  }

  @Override
  public void write() {
    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
      writeVarInt(this.chunkX);
      writeVarInt(this.chunkZ);
    } else {
      writeInt(this.chunkX);
      writeInt(this.chunkZ);
    }
  }

  @Override
  public void copy(WrapperPlayServerUpdateViewPosition wrapper) {
    this.chunkX = wrapper.chunkX;
    this.chunkZ = wrapper.chunkZ;
  }

  public int getChunkX() {
    return chunkX;
  }

  public void setChunkX(int chunkX) {
    this.chunkX = chunkX;
  }

  public int getChunkZ() {
    return chunkZ;
  }

  public void setChunkZ(int chunkZ) {
    this.chunkZ = chunkZ;
  }

  @Override
  public String toString() {
    return "WrapperPlayServerUpdateViewPosition{" +
            "chunkX=" + chunkX +
            ", chunkZ=" + chunkZ +
            '}';
  }
}
