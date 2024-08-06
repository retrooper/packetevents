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

package com.github.retrooper.packetevents.manager;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.mappings.SynchronizedRegistriesHandler;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerRegistryData;
import com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerLoginSuccess;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRespawn;

import java.net.InetSocketAddress;

public class InternalPacketListener extends PacketListenerAbstract {

    public InternalPacketListener() {
        this(PacketListenerPriority.LOWEST);
    }

    public InternalPacketListener(PacketListenerPriority priority) {
        super(priority);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        User user = event.getUser();
        if (event.getPacketType() == PacketType.Login.Server.LOGIN_SUCCESS) {
            Object channel = event.getChannel();
            //Process outgoing login success packet
            WrapperLoginServerLoginSuccess loginSuccess = new WrapperLoginServerLoginSuccess(event);
            UserProfile profile = loginSuccess.getUserProfile();

            //Update user profile
            user.getProfile().setUUID(profile.getUUID());
            user.getProfile().setName(profile.getName());
            //Texture properties are passed in login success on 1.19
            user.getProfile().setTextureProperties(profile.getTextureProperties());

            //Map username with channel
            synchronized (channel) {
                ProtocolManager.CHANNELS.put(profile.getUUID(), channel);
            }

            PacketEvents.getAPI().getLogManager().debug("Mapped player UUID with their channel.");

            // Switch the user's connection state to new state, but the variable event.getConnectionState() remains LOGIN
            // We switch user state immediately to remain in sync with vanilla, allowing you to encode packets immediately
            boolean proxy = PacketEvents.getAPI().getInjector().isProxy();
            if (proxy ? event.getUser().getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_2)
                    : event.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
                user.setEncoderState(ConnectionState.CONFIGURATION);
            } else {
                user.setConnectionState(ConnectionState.PLAY);
            }
        }

        // The server sends dimension information in configuration phase, since 1.20.2
        else if (event.getPacketType() == PacketType.Configuration.Server.REGISTRY_DATA) {
            WrapperConfigServerRegistryData packet = new WrapperConfigServerRegistryData(event);

            if (packet.getElements() != null) { // 1.20.2 to 1.20.5
                SynchronizedRegistriesHandler.handleRegistry(user, packet.getServerVersion().toClientVersion(),
                        packet.getRegistryKey(), packet.getElements());
            }
            if (packet.getRegistryData() != null) { // since 1.20.5
                SynchronizedRegistriesHandler.handleLegacyRegistries(user, packet.getServerVersion()
                        .toClientVersion(), packet.getRegistryData());
            }
        }

        // The server sends registry info in login packet for 1.16 to 1.20.1
        else if (event.getPacketType() == PacketType.Play.Server.JOIN_GAME) {
            WrapperPlayServerJoinGame joinGame = new WrapperPlayServerJoinGame(event);
            user.setEntityId(joinGame.getEntityId());

            if (joinGame.getDimensionCodec() != null) { // 1.16 to 1.20.1
                SynchronizedRegistriesHandler.handleLegacyRegistries(user, joinGame.getServerVersion().toClientVersion(),
                        joinGame.getDimensionCodec());
            }

            user.setDimensionType(joinGame.getDimensionType());
        }

        // Respawn is used to switch dimensions
        else if (event.getPacketType() == PacketType.Play.Server.RESPAWN) {
            WrapperPlayServerRespawn packet = new WrapperPlayServerRespawn(event);
            user.setDimensionType(packet.getDimensionType());
        } else if (event.getPacketType() == PacketType.Play.Server.CONFIGURATION_START) {
            user.setEncoderState(ConnectionState.CONFIGURATION);
        } else if (event.getPacketType() == PacketType.Configuration.Server.CONFIGURATION_END) {
            user.setEncoderState(ConnectionState.PLAY);
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        User user = event.getUser();
        if (event.getPacketType() == PacketType.Handshaking.Client.HANDSHAKE) {
            Object channel = event.getChannel();
            InetSocketAddress address = event.getSocketAddress();
            WrapperHandshakingClientHandshake handshake = new WrapperHandshakingClientHandshake(event);
            ConnectionState nextState = handshake.getNextConnectionState();
            ClientVersion clientVersion = handshake.getClientVersion();
            //Update client version for this event call(and user)
            user.setClientVersion(clientVersion);
            PacketEvents.getAPI().getLogManager().debug("Processed " + address.getHostString() + ":" + address.getPort() + "'s client version. Client Version: " + clientVersion.getReleaseName());
            //Transition into LOGIN or STATUS connection state immediately, to remain in sync with vanilla
            user.setConnectionState(nextState);
        } else if (event.getPacketType() == PacketType.Login.Client.LOGIN_SUCCESS_ACK) {
            user.setDecoderState(ConnectionState.CONFIGURATION);
        } else if (event.getPacketType() == PacketType.Play.Client.CONFIGURATION_ACK) {
            user.setDecoderState(ConnectionState.CONFIGURATION);
        } else if (event.getPacketType() == PacketType.Configuration.Client.CONFIGURATION_END_ACK) {
            user.setDecoderState(ConnectionState.PLAY);
        }
    }
}
