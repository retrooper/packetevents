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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientChatCommandUnsigned extends PacketWrapper<WrapperPlayClientChatCommandUnsigned> {

    private String command;

    public WrapperPlayClientChatCommandUnsigned(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientChatCommandUnsigned(String command) {
        super(PacketType.Play.Client.CHAT_COMMAND_UNSIGNED);
        this.command = command;
    }

    @Override
    public void read() {
        this.command = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)
                ? this.readString() : this.readString(256);
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            this.writeString(this.command);
        } else {
            this.writeString(this.command, 256);
        }
    }

    @Override
    public void copy(WrapperPlayClientChatCommandUnsigned wrapper) {
        this.command = wrapper.command;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
