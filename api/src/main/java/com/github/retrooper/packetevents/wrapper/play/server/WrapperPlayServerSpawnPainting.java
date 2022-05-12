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
import com.github.retrooper.packetevents.protocol.world.Direction;
import com.github.retrooper.packetevents.protocol.world.PaintingType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;
import java.util.UUID;

// Mostly from MCProtocolLib
public class WrapperPlayServerSpawnPainting extends PacketWrapper<WrapperPlayServerSpawnPainting> {
  private int entityId;
  private Optional<UUID> uuid;
  private Optional<String> title;
  private Optional<PaintingType> type;
  private Vector3i position;
  private Direction direction;

  public WrapperPlayServerSpawnPainting(PacketSendEvent event) {
    super(event);
  }

  public WrapperPlayServerSpawnPainting(int entityId, Optional<String> title, Vector3i position, Direction direction) {
    super(PacketType.Play.Server.SPAWN_PAINTING);
    this.entityId = entityId;
    this.title = title;
    this.position = position;
    this.direction = direction;
  }

  public WrapperPlayServerSpawnPainting(int entityId, Optional<UUID> uuid, Optional<String> title, Vector3i position, Direction direction) {
    super(PacketType.Play.Server.SPAWN_PAINTING);
    this.entityId = entityId;
    this.uuid = uuid;
    this.title = title;
    this.position = position;
    this.direction = direction;
  }

  public WrapperPlayServerSpawnPainting(int entityId, Optional<UUID> uuid, Optional<String> title, Optional<PaintingType> type, Vector3i position, Direction direction) {
    super(PacketType.Play.Server.SPAWN_PAINTING);
    this.entityId = entityId;
    this.uuid = uuid;
    this.title = title;
    this.type = type;
    this.position = position;
    this.direction = direction;
  }

  @Override
  public void read() {
    this.uuid = Optional.empty();
    this.title = Optional.empty();
    this.type = Optional.empty();
    this.entityId = readVarInt();
    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
      this.uuid = Optional.of(readUUID());
    }
    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
      this.type = Optional.of(PaintingType.VALUES[readVarInt()]);
    } else {
      this.title = Optional.of(readString(13));
    }
    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
      this.position = readBlockPosition();
    } else {
      int x = readInt();
      int y = readInt();
      int z = readInt();
      this.position = new Vector3i(x, y, z);
    }
    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
      this.direction = Direction.getByHorizontalIndex(readUnsignedByte());
    } else {
      this.direction = Direction.getByHorizontalIndex(readInt());
    }
  }

  @Override
  public void write() {
    writeVarInt(this.entityId);
    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
      writeUUID(this.uuid.get());
    }
    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
      writeVarInt(this.type.get().ordinal());
    } else {
      writeString(this.title.get(), 13);
    }
    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
      long positionVector = this.position.getSerializedPosition();
      writeLong(positionVector);
    } else {
      writeInt(this.position.x);
      writeShort(this.position.y);
      writeInt(this.position.z);
    }
    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
      writeByte(this.direction.getHorizontalIndex());
    } else {
      writeInt(this.direction.getHorizontalIndex());
    }
  }

  @Override
  public void copy(WrapperPlayServerSpawnPainting wrapper) {
    this.entityId = wrapper.entityId;
    this.uuid = wrapper.uuid;
    this.title = wrapper.title;
    this.type = wrapper.type;
    this.position = wrapper.position;
    this.direction = wrapper.direction;
  }

  public int getEntityId() {
    return entityId;
  }

  public void setEntityId(int entityId) {
    this.entityId = entityId;
  }

  public Optional<UUID> getUuid() {
    return uuid;
  }

  public void setUuid(Optional<UUID> uuid) {
    this.uuid = uuid;
  }

  public Optional<String> getTitle() {
    return title;
  }

  public void setTitle(Optional<String> title) {
    this.title = title;
  }

  public Optional<PaintingType> getType() {
    return type;
  }

  public void setType(Optional<PaintingType> type) {
    this.type = type;
  }

  public Vector3i getPosition() {
    return position;
  }

  public void setPosition(Vector3i position) {
    this.position = position;
  }

  public Direction getDirection() {
    return direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }
}
