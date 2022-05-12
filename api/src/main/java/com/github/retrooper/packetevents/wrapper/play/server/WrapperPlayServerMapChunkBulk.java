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

public class WrapperPlayServerMapChunkBulk extends PacketWrapper<WrapperPlayServerMapChunkBulk> {
  private boolean skyLightSent;
  private int chunkColumnCount;
  private int chunkX;
  private int chunkY;
  private int primaryBitmap;
  private byte[] data;

  public WrapperPlayServerMapChunkBulk(PacketSendEvent event) {
    super(event);
  }

  public WrapperPlayServerMapChunkBulk(boolean skyLightSent, int chunkColumnCount, int chunkX, int chunkY, short primaryBitmap, byte[] data) {
    super(PacketType.Play.Server.MAP_CHUNK_BULK);
    this.skyLightSent = skyLightSent;
    this.chunkColumnCount = chunkColumnCount;
    this.chunkX = chunkX;
    this.chunkY = chunkY;
    this.primaryBitmap = primaryBitmap;
    this.data = data;
  }

  @Override
  public void read() {
    this.skyLightSent = readBoolean();
    this.chunkColumnCount = readVarInt();
    this.chunkX = readInt();
    this.chunkY = readInt();
    this.primaryBitmap = readUnsignedShort();
    this.data = readRemainingBytes();
  }

  @Override
  public void write() {
    writeBoolean(skyLightSent);
    writeVarInt(chunkColumnCount);
    writeInt(chunkX);
    writeInt(chunkY);
    writeShort(primaryBitmap);
    writeBytes(data);
  }

  @Override
  public void copy(WrapperPlayServerMapChunkBulk wrapper) {
    this.skyLightSent = wrapper.skyLightSent;
    this.chunkColumnCount = wrapper.chunkColumnCount;
    this.chunkX = wrapper.chunkX;
    this.chunkY = wrapper.chunkY;
    this.primaryBitmap = wrapper.primaryBitmap;
    this.data = wrapper.data;
  }

  public boolean isSkyLightSent() {
    return skyLightSent;
  }

  public void setSkyLightSent(boolean skyLightSent) {
    this.skyLightSent = skyLightSent;
  }

  public int getChunkColumnCount() {
    return chunkColumnCount;
  }

  public void setChunkColumnCount(int chunkColumnCount) {
    this.chunkColumnCount = chunkColumnCount;
  }

  public int getChunkX() {
    return chunkX;
  }

  public void setChunkX(int chunkX) {
    this.chunkX = chunkX;
  }

  public int getChunkY() {
    return chunkY;
  }

  public void setChunkY(int chunkY) {
    this.chunkY = chunkY;
  }

  public int getPrimaryBitmap() {
    return primaryBitmap;
  }

  public void setPrimaryBitmap(int primaryBitmap) {
    this.primaryBitmap = primaryBitmap;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }
}
