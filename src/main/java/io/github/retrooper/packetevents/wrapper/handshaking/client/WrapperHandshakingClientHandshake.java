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

package io.github.retrooper.packetevents.wrapper.handshaking.client;

import io.github.retrooper.packetevents.protocol.PacketState;
import io.github.retrooper.packetevents.utils.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperHandshakingClientHandshake extends PacketWrapper {
    private final int protocolVersion;
    private final String serverAddress;
    private final int serverPort;
    private final PacketState nextPacketState;

    public WrapperHandshakingClientHandshake(ClientVersion version, ByteBufAbstract byteBuf) {
        super(version, byteBuf);
        this.protocolVersion = readVarInt();
        this.serverAddress = readString(32767);
        this.serverPort = readUnsignedShort();
        int nextStateIndex = readVarInt();
        this.nextPacketState = PacketState.values()[nextStateIndex];
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public ClientVersion getClientVersion() {
        return ClientVersion.getClientVersion(protocolVersion);
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public PacketState getNextState() {
        return nextPacketState;
    }
}
