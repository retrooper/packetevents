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

public class WrapperPlayClientCloseWindow extends PacketWrapper<WrapperPlayClientCloseWindow> {
    private int windowId;

    public WrapperPlayClientCloseWindow(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientCloseWindow(int windowId) {
        super(PacketType.Play.Client.CLOSE_WINDOW);
        this.windowId = windowId;
    }

    @Override
    public void read() {
        this.windowId = readUnsignedByte();
    }

    @Override
    public void write() {
        writeByte(this.windowId);
    }

    @Override
    public void copy(WrapperPlayClientCloseWindow wrapper) {
        this.windowId = wrapper.windowId;
    }

    public int getWindowId() {
        return windowId;
    }

    public void setWindowId(int windowId) {
        this.windowId = windowId;
    }
}
