/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

public class WrapperPlayServerCloseWindow extends PacketWrapper<WrapperPlayServerCloseWindow> {

    private int windowId;

    public WrapperPlayServerCloseWindow(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerCloseWindow() {
        this(0);
    }

    public WrapperPlayServerCloseWindow(int id) {
        super(PacketType.Play.Server.CLOSE_WINDOW);
        this.windowId = id;
    }

    @Override
    public void read() {
        this.windowId = this.readContainerId();
    }

    @Override
    public void write() {
        this.writeContainerId(this.windowId);
    }

    @Override
    public void copy(WrapperPlayServerCloseWindow wrapper) {
        this.windowId = wrapper.windowId;
    }

    /**
     * Note: Window ID is ignored by the client on all versions.
     */
    public int getWindowId() {
        return this.windowId;
    }

    /**
     * Note: Window ID is ignored by the client on all versions.
     */
    public void setWindowId(int windowId) {
        this.windowId = windowId;
    }
}
