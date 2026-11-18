/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package com.github.retrooper.packetevents.manager;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import org.jetbrains.annotations.ApiStatus;

/**
 * Keeps the raw client-side state used by client-versioned handlers separate from the normal server state.
 */
@ApiStatus.Internal
public class ClientVersionInternalListener extends PacketListenerAbstract {

    public ClientVersionInternalListener() {
        super(PacketListenerPriority.LOWEST);
    }

    @Override
    public boolean useClientVersion() {
        return true;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        User user = event.getUser();
        if (event.getPacketType() == PacketType.Handshaking.Client.HANDSHAKE) {
            WrapperHandshakingClientHandshake packet = new WrapperHandshakingClientHandshake(event);
            user.setClientVersion(packet.getClientVersion());
            ConnectionState state = packet.getNextConnectionState();
            user.setClientDecoderState(state);
            user.setClientEncoderState(state);
        } else if (event.getPacketType() == PacketType.Login.Client.LOGIN_SUCCESS_ACK) {
            user.setClientDecoderState(ConnectionState.CONFIGURATION);
        } else if (event.getPacketType() == PacketType.Play.Client.CONFIGURATION_ACK) {
            user.setClientDecoderState(ConnectionState.CONFIGURATION);
        } else if (event.getPacketType() == PacketType.Configuration.Client.CONFIGURATION_END_ACK) {
            user.setClientDecoderState(ConnectionState.PLAY);
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        User user = event.getUser();
        if (event.getPacketType() == PacketType.Login.Server.LOGIN_SUCCESS) {
            if (event.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
                user.setClientEncoderState(ConnectionState.CONFIGURATION);
            } else {
                user.setClientEncoderState(ConnectionState.PLAY);
                user.setClientDecoderState(ConnectionState.PLAY);
            }
        } else if (event.getPacketType() == PacketType.Play.Server.CONFIGURATION_START) {
            user.setClientEncoderState(ConnectionState.CONFIGURATION);
        } else if (event.getPacketType() == PacketType.Configuration.Server.CONFIGURATION_END) {
            user.setClientEncoderState(ConnectionState.PLAY);
        }
    }
}
