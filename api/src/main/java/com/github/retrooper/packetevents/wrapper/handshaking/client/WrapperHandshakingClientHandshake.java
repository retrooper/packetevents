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
    private ConnectionIntention intention;

    public WrapperHandshakingClientHandshake(PacketReceiveEvent event) {
        super(event);
    }

    @Deprecated
    public WrapperHandshakingClientHandshake(int protocolVersion, String serverAddress, int serverPort, ConnectionState nextConnectionState) {
        this(protocolVersion, serverAddress, serverPort, ConnectionIntention.LOGIN);
        this.setNextConnectionState(nextConnectionState);
    }

    public WrapperHandshakingClientHandshake(int protocolVersion, String serverAddress, int serverPort, ConnectionIntention intention) {
        super(PacketType.Handshaking.Client.HANDSHAKE);
        this.protocolVersion = protocolVersion;
        this.clientVersion = ClientVersion.getById(protocolVersion);
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.intention = intention;
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
            this.intention = ConnectionIntention.fromId(nextStateIndex);
        } catch (Exception e) {
            throw new InvalidHandshakeException();
        }
    }

    @Override
    public void write() {
        writeVarInt(protocolVersion);
        writeString(serverAddress, Short.MAX_VALUE); // Should be 255, but spigot changes this
        writeShort(serverPort);
        writeVarInt(intention.getId());
    }

    @Override
    public void copy(WrapperHandshakingClientHandshake wrapper) {
        this.protocolVersion = wrapper.protocolVersion;
        this.clientVersion = wrapper.clientVersion;
        this.serverAddress = wrapper.serverAddress;
        this.serverPort = wrapper.serverPort;
        this.intention = wrapper.intention;
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
        return this.intention.getTargetState();
    }

    /**
     * @deprecated use {@link #setIntention(ConnectionIntention)}
     */
    public void setNextConnectionState(ConnectionState nextConnectionState) {
        switch (nextConnectionState) {
            case LOGIN:
                this.intention = ConnectionIntention.LOGIN;
            case STATUS:
                this.intention = ConnectionIntention.STATUS;
            default:
                throw new IllegalArgumentException("Illegal next connection state: " + nextConnectionState);
        }
    }

    public ConnectionIntention getIntention() {
        return this.intention;
    }

    public void setIntention(ConnectionIntention intention) {
        this.intention = intention;
    }

    public enum ConnectionIntention {

        STATUS(1, ConnectionState.STATUS),
        LOGIN(2, ConnectionState.LOGIN),
        TRANSFER(3, ConnectionState.LOGIN);

        private final int id;
        private final ConnectionState targetState;

        ConnectionIntention(int id, ConnectionState targetState) {
            this.id = id;
            this.targetState = targetState;
        }

        public static ConnectionIntention fromId(int id) {
            switch (id) {
                case 2:
                    return LOGIN;
                case 1:
                    return STATUS;
                case 3:
                    return TRANSFER;
                default:
                    throw new IllegalArgumentException("Illegal connection intention: " + id);
            }
        }

        public int getId() {
            return this.id;
        }

        public ConnectionState getTargetState() {
            return this.targetState;
        }
    }
}
