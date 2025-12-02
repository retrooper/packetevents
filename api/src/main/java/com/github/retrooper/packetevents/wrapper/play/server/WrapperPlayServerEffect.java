/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * Mojang name: ClientboundLevelEventPacket
 */
public class WrapperPlayServerEffect extends PacketWrapper<WrapperPlayServerEffect> {

    private int type;
    private Vector3i position;
    private int data;
    private boolean globalEvent;

    public WrapperPlayServerEffect(PacketSendEvent type) {
        super(type);
    }

    public WrapperPlayServerEffect(int type, Vector3i position, int data, boolean globalEvent) {
        super(PacketType.Play.Server.EFFECT);
        this.type = type;
        this.position = position;
        this.data = data;
        this.globalEvent = globalEvent;
    }

    @Override
    public void read() {
        this.type = this.readInt();
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.position = this.readBlockPosition();
        } else {
            this.position = new Vector3i(this.readInt(), this.readByte() & 0xFF, this.readInt());
        }
        this.data = this.readInt();
        this.globalEvent = this.readBoolean();
    }

    @Override
    public void write() {
        this.writeInt(this.type);
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.writeBlockPosition(this.position);
        } else {
            this.writeInt(this.position.x);
            this.writeByte(this.position.y & 0xFF);
            this.writeInt(this.position.z);
        }
        this.writeInt(this.data);
        this.writeBoolean(this.globalEvent);
    }

    @Override
    public void copy(WrapperPlayServerEffect wrapper) {
        this.type = wrapper.type;
        this.position = wrapper.position;
        this.data = wrapper.data;
        this.globalEvent = wrapper.globalEvent;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Vector3i getPosition() {
        return this.position;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    public int getData() {
        return this.data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public boolean isGlobalEvent() {
        return this.globalEvent;
    }

    public void setGlobalEvent(boolean globalEvent) {
        this.globalEvent = globalEvent;
    }
}
