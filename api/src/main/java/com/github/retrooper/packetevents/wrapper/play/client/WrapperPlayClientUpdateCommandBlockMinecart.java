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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientUpdateCommandBlockMinecart extends PacketWrapper<WrapperPlayClientUpdateCommandBlockMinecart> {
  private int entityId;
  private String command;
  private boolean trackOutput;

  public WrapperPlayClientUpdateCommandBlockMinecart(PacketReceiveEvent event) {
    super(event);
  }

  public WrapperPlayClientUpdateCommandBlockMinecart(int entityId, String command, boolean trackOutput) {
    super(PacketType.Play.Client.UPDATE_COMMAND_BLOCK_MINECART);
    this.entityId = entityId;
    this.command = command;
    this.trackOutput = trackOutput;
  }

  @Override
  public void read() {
    this.entityId = readVarInt();
    this.command = readString();
    this.trackOutput = readBoolean();
  }

  @Override
  public void write() {
    writeVarInt(this.entityId);
    writeString(this.command);
    writeBoolean(this.trackOutput);
  }

  @Override
  public void copy(WrapperPlayClientUpdateCommandBlockMinecart wrapper) {
    this.entityId = wrapper.entityId;
    this.command = wrapper.command;
    this.trackOutput = wrapper.trackOutput;
  }

  public int getEntityId() {
    return entityId;
  }

  public void setEntityId(int entityId) {
    this.entityId = entityId;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public boolean isTrackOutput() {
    return trackOutput;
  }

  public void setTrackOutput(boolean trackOutput) {
    this.trackOutput = trackOutput;
  }
}
