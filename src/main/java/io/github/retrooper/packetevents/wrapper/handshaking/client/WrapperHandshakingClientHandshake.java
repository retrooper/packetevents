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

/**
 * This packet is the first packet the client should send.
 * It contains useful information such as the client's protocol version.
 */
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

    /**
     * Protocol version of the client.
     * The latest vanilla server won't let clients with a different protocol version join the server.
     * @return Protocol version
     */
    public int getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * {@link ClientVersion} of the client.
     * This enum maps the protocol versions with the name of the release.
     * @return Client version
     */
    public ClientVersion getClientVersion() {
        return ClientVersion.getClientVersion(protocolVersion);
    }

    /**
     * Address of the server.
     * @return Server address
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * Port of the server.
     * @return Server port
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * The next packet state.
     * Should always be {@link PacketState#STATUS} or {@link PacketState#LOGIN} on the vanilla client.
     * @return Next packet state
     */
    public PacketState getNextState() {
        return nextPacketState;
    }
}
