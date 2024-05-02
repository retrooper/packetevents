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
import net.kyori.adventure.text.Component;

// TODO: Test on outdated versions
public class WrapperPlayServerOpenWindow extends PacketWrapper<WrapperPlayServerOpenWindow> {
    private int containerId; // All versions
    private int type;
    private Component title;

    public WrapperPlayServerOpenWindow(PacketSendEvent event) {
        super(event);
    }

    // For 1.14+
    public WrapperPlayServerOpenWindow(int containerId, int type, Component title) {
        super(PacketType.Play.Server.OPEN_WINDOW);
        this.containerId = containerId;
        this.type = type;
        this.title = title;
    }

    @Override
    public void read() {
        this.containerId = readVarInt();
        this.type = readVarInt();
        this.title = readComponent();
    }

    @Override
    public void write() {
        writeVarInt(this.containerId);
        writeVarInt(this.type);
        writeComponent(this.title);
    }

    @Override
    public void copy(WrapperPlayServerOpenWindow wrapper) {
        this.containerId = wrapper.containerId;
        this.type = wrapper.type;
        this.title = wrapper.title;
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Component getTitle() {
        return this.title;
    }

    public void setTitle(Component title) {
        this.title = title;
    }

}
