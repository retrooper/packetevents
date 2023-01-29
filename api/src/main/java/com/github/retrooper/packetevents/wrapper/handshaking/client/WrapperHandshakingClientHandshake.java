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

package com.github.retrooper.packetevents.wrapper.handshaking.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.exception.InvalidHandshakeException;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * This packet is the first packet the client should send.
 * It contains important data such as the client's protocol version.
 */
public class WrapperHandshakingClientHandshake extends PacketWrapper<WrapperHandshakingClientHandshake> {
    private int protocolVersion;
    private ClientVersion clientVersion;
    private String serverAddress;
    private int serverPort;
    private ConnectionState nextConnectionState;

    public WrapperHandshakingClientHandshake(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperHandshakingClientHandshake(int protocolVersion, String serverAddress, int serverPort, ConnectionState nextConnectionState) {
        super(PacketType.Handshaking.Client.HANDSHAKE);
        this.protocolVersion = protocolVersion;
        this.clientVersion = ClientVersion.getById(protocolVersion);
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.nextConnectionState = nextConnectionState;
    }

    @Override
    public void read() {
        // Bots scanning the internet often break the minecraft protocol (they don't even know what minecraft is!)
        // as people love scanning the ipv4 range trying to connect to any service
        try {
            this.protocolVersion = readVarInt();
            this.clientVersion = ClientVersion.getById(protocolVersion);
            this.serverAddress = readString(Short.MAX_VALUE); // Should be 255, but :shrug: someone reported issues
            this.serverPort = readUnsignedShort();
            int nextStateIndex = readVarInt();
            this.nextConnectionState = ConnectionState.getById(nextStateIndex);
        } catch (Exception e) {
            throw new InvalidHandshakeException();
        }
    }

    @Override
    public void write() {
        writeVarInt(protocolVersion);
        writeString(serverAddress, Short.MAX_VALUE); // Should be 255, but spigot changes this
        writeShort(serverPort);
        writeVarInt(nextConnectionState.ordinal());
    }

    @Override
    public void copy(WrapperHandshakingClientHandshake wrapper) {
        this.protocolVersion = wrapper.protocolVersion;
        this.clientVersion = wrapper.clientVersion;
        this.serverAddress = wrapper.serverAddress;
        this.serverPort = wrapper.serverPort;
        this.nextConnectionState = wrapper.nextConnectionState;
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

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
        this.clientVersion = ClientVersion.getById(protocolVersion);
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

    public void setClientVersion(ClientVersion clientVersion) {
        this.clientVersion = clientVersion;
        this.protocolVersion = clientVersion.getProtocolVersion();
    }

    /**
     * Address of the server.
     *
     * @return Server address
     */
    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * Port of the server.
     *
     * @return Server port
     */
    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
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

    public void setNextConnectionState(ConnectionState nextConnectionState) {
        this.nextConnectionState = nextConnectionState;
    }
}
