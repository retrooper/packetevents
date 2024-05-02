/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package com.github.retrooper.packetevents.wrapper.configuration.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperConfigServerTransfer extends PacketWrapper<WrapperConfigServerTransfer> {

    private String host;
    private int port;

    public WrapperConfigServerTransfer(PacketSendEvent event) {
        super(event);
    }

    public WrapperConfigServerTransfer(String host, int port) {
        super(PacketType.Configuration.Server.TRANSFER);
        this.host = host;
        this.port = port;
    }

    @Override
    public void read() {
        this.host = this.readString();
        this.port = this.readVarInt();
    }

    @Override
    public void write() {
        this.writeString(this.host);
        this.writeVarInt(this.port);
    }

    @Override
    public void copy(WrapperConfigServerTransfer wrapper) {
        this.host = wrapper.host;
        this.port = wrapper.port;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
