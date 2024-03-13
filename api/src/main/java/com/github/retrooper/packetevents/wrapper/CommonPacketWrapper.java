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

package com.github.retrooper.packetevents.wrapper;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;

public abstract class CommonPacketWrapper<T extends CommonPacketWrapper<T>> extends PacketWrapper<T> {

    private final ConnectionState state;

    public CommonPacketWrapper(ConnectionState state, ClientVersion clientVersion, ServerVersion serverVersion, int packetID) {
        super(clientVersion, serverVersion, packetID);
        this.state = state;
    }

    public CommonPacketWrapper(PacketReceiveEvent event) {
        this(event, false);
    }

    public CommonPacketWrapper(PacketReceiveEvent event, boolean readData) {
        super(event, false);
        this.state = event.getConnectionState();
        if (readData) {
            readEvent(event);
        }
    }

    public CommonPacketWrapper(PacketSendEvent event) {
        this(event, false);
    }

    public CommonPacketWrapper(PacketSendEvent event, boolean readData) {
        super(event, false);
        state = event.getConnectionState();
        if (readData) {
            readEvent(event);
        }
    }

    public CommonPacketWrapper(PacketTypeCommon packetType) {
        super(packetType);
        this.state = packetType.getState();
    }

    public static CommonPacketWrapper<?> createUniversalPacketWrapper(ConnectionState state, Object byteBuf) {
        CommonPacketWrapper<?> wrapper = new CommonPacketWrapper(state, ClientVersion.UNKNOWN, PacketEvents.getAPI().getServerManager().getVersion(), -2) {
            @Override
            public CommonPacketWrapper<?> copy(ConnectionState state) {
                return CommonPacketWrapper.createUniversalPacketWrapper(state, byteBuf);
            }
        };
        wrapper.buffer = byteBuf;
        return wrapper;
    }

    public ConnectionState getState() {
        return state;
    }

    @SuppressWarnings("unchecked")
    public T as(ConnectionState state) {
        if (this.state == state) {
            return (T) this;
        }
        return copy(state);
    }

    public abstract T copy(ConnectionState state);
}
