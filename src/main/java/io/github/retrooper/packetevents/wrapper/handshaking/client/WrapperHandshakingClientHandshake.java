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

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.protocol.ConnectionState;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * This packet is the first packet the client should send.
 * It contains important data such as the client's protocol version.
 */
public class WrapperHandshakingClientHandshake extends PacketWrapper {
    private final int protocolVersion;
    private final ClientVersion clientVersion;
    private final String serverAddress;
    private final int serverPort;
    private final ConnectionState nextConnectionState;

    public WrapperHandshakingClientHandshake(PacketReceiveEvent event) {
        super(event);
        this.protocolVersion = readVarInt();
        this.clientVersion = ClientVersion.getClientVersion(protocolVersion);
        this.serverAddress = readString();
        this.serverPort = readUnsignedShort();
        int nextStateIndex = readVarInt();
        this.nextConnectionState = ConnectionState.values()[nextStateIndex];
    }

    /**
     * Protocol version of the client.
     * The latest vanilla server won't let clients with a different protocol version join the server.
     *
     * @return Protocol version
     */
    public int getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * {@link ClientVersion} of the client.
     * This enum maps the protocol versions with the name of the release.
     *
     * @return Client version
     */
    public ClientVersion getClientVersion() {
        return clientVersion;
    }

    /**
     * Address of the server.
     *
     * @return Server address
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * Port of the server.
     *
     * @return Server port
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * The next connection state.
     * Should always be {@link ConnectionState#STATUS} or {@link ConnectionState#LOGIN} on the vanilla client.
     *
     * @return Next connection state
     */
    public ConnectionState getNextConnectionState() {
        return nextConnectionState;
    }
}
