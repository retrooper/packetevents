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

package com.retrooper.packetevents.wrapper.play.client;

import com.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.retrooper.packetevents.protocol.packettype.PacketType;
import com.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientCloseWindow extends PacketWrapper<WrapperPlayClientCloseWindow> {
    private int windowID;

    public WrapperPlayClientCloseWindow(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientCloseWindow(int windowID) {
        super(PacketType.Play.Client.CLOSE_WINDOW);
        this.windowID = windowID;
    }

    @Override
    public void readData() {
        this.windowID = readUnsignedByte();
    }

    @Override
    public void readData(WrapperPlayClientCloseWindow wrapper) {
        this.windowID = wrapper.windowID;
    }

    @Override
    public void writeData() {
        writeByte(this.windowID);
    }

    public int getWindowID() {
        return windowID;
    }

    public void setWindowID(int windowID) {
        this.windowID = windowID;
    }
}
