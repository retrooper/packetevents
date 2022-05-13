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

public class WrapperPlayServerSetCompression extends PacketWrapper<WrapperPlayServerSetCompression> {
  private int threshold;

  public WrapperPlayServerSetCompression(PacketSendEvent event) {
    super(event);
  }

  public WrapperPlayServerSetCompression(int threshold) {
    super(PacketType.Play.Server.SET_COMPRESSION);
    this.threshold = threshold;
  }


  @Override
  public void read() {
    this.threshold = readVarInt();
  }

  @Override
  public void write() {
    writeVarInt(threshold);
  }

  @Override
  public void copy(WrapperPlayServerSetCompression wrapper) {
    this.threshold = wrapper.threshold;
  }

  public int getThreshold() {
    return threshold;
  }

  public void setThreshold(int threshold) {
    this.threshold = threshold;
  }
}
