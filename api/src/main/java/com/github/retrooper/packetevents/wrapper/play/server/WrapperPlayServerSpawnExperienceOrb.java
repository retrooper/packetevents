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

public class WrapperPlayServerSpawnExperienceOrb extends PacketWrapper<WrapperPlayServerSpawnExperienceOrb> {
  private int entityId;
  private double x;
  private double y;
  private double z;
  private short count;

  public WrapperPlayServerSpawnExperienceOrb(PacketSendEvent event) {
    super(event);
  }

  public WrapperPlayServerSpawnExperienceOrb(int entityId, double x, double y, double z, short count) {
    super(PacketType.Play.Server.SPAWN_EXPERIENCE_ORB);
    this.entityId = entityId;
    this.x = x;
    this.y = y;
    this.z = z;
    this.count = count;
  }

  @Override
  public void read() {
    if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
      this.entityId = readShort();
    } else {
      this.entityId = readVarInt();
    }
    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
      this.x = readDouble();
      this.y = readDouble();
      this.z = readDouble();
    } else {
      this.x = readInt() / 32.0;
      this.y = readInt() / 32.0;
      this.z = readInt() / 32.0;
    }
    this.count = readShort();
  }

  @Override
  public void write() {
    if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
      writeShort(this.entityId);
    } else {
      writeVarInt(this.entityId);
    }
    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
      writeDouble(this.x);
      writeDouble(this.y);
      writeDouble(this.z);
    } else {
      writeInt(floor(this.x * 32.0));
      writeInt(floor(this.y * 32.0));
      writeInt(floor(this.z * 32.0));
    }
    writeShort(this.count);
  }

  @Override
  public void copy(WrapperPlayServerSpawnExperienceOrb wrapper) {
    this.entityId = wrapper.entityId;
    this.x = wrapper.x;
    this.y = wrapper.y;
    this.z = wrapper.z;
    this.count = wrapper.count;
  }

  public int getEntityId() {
    return entityId;
  }

  public void setEntityId(int entityId) {
    this.entityId = entityId;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getZ() {
    return z;
  }

  public void setZ(double z) {
    this.z = z;
  }

  public short getCount() {
    return count;
  }

  public void setCount(short count) {
    this.count = count;
  }

  private int floor(double value) {
    int temp = (int) value;
    return value < (float) temp ? temp - 1 : temp;
  }
}
