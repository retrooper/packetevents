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

package com.github.retrooper.packetevents.injector;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerLoginSuccess;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRespawn;

import java.net.InetSocketAddress;

public class InternalPacketListener implements PacketListener {
    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Login.Server.LOGIN_SUCCESS) {
            Object channel = event.getChannel();
            User user = event.getUser();
            //Process outgoing login success packet
            WrapperLoginServerLoginSuccess loginSuccess = new WrapperLoginServerLoginSuccess(event);
            UserProfile profile = loginSuccess.getUser();

            //Update user profile
            user.getProfile().setUUID(profile.getUUID());
            user.getProfile().setName(profile.getName());

            //Map username with channel
            ProtocolManager.CHANNELS.put(profile.getName(), channel);
            PacketEvents.getAPI().getLogManager().debug("Mapped player username with their channel.");

            //Update connection state(injectors might do some adjustments when we transition into PLAY state)
            //This also updates it for the user instance
            event.getPostTasks().add(() -> {
                PacketEvents.getAPI().getInjector().changeConnectionState(channel, ConnectionState.PLAY);
                PacketEvents.getAPI().getLogManager().debug("Transitioned user " + profile.getName() + " into the PLAY state!");
            });
        }

        // Join game can be used to update world height, and sets dimension data
        if (event.getPacketType() == PacketType.Play.Server.JOIN_GAME) {
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
                return; // Fixed world height, no tags are sent to the client
            }

            WrapperPlayServerJoinGame joinGame = new WrapperPlayServerJoinGame(event);

            // Store world height
            NBTList<NBTCompound> list = joinGame.getDimensionCodec().getCompoundTagOrNull("minecraft:dimension_type").getCompoundListTagOrNull("value");
            event.getUser().setWorldNBT(list);

            // Update world height
            NBTCompound worldNBT = event.getUser().getWorldNBT(joinGame.getDimension().getType().getName()).getCompoundTagOrNull("element");
            event.getUser().setMinWorldHeight(worldNBT.getNumberTagOrNull("min_y").getAsInt());
            event.getUser().setTotalWorldHeight(worldNBT.getNumberTagOrNull("height").getAsInt());
        }

        // Respawn is used to switch dimensions
        if (event.getPacketType() == PacketType.Play.Server.RESPAWN) {
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
                return; // Fixed world height, no tags are sent to the client
            }

            WrapperPlayServerRespawn respawn = new WrapperPlayServerRespawn(event);

            NBTCompound worldNBT = event.getUser().getWorldNBT(respawn.getDimension().getType().getName()).getCompoundTagOrNull("element"); // This is 1.17+, it always sends the world name
            event.getUser().setMinWorldHeight(worldNBT.getNumberTagOrNull("min_y").getAsInt());
            event.getUser().setTotalWorldHeight(worldNBT.getNumberTagOrNull("height").getAsInt());
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Handshaking.Client.HANDSHAKE) {
            User user = event.getUser();
            Object channel = event.getChannel();
            InetSocketAddress address = event.getSocketAddress();
            WrapperHandshakingClientHandshake handshake = new WrapperHandshakingClientHandshake(event);
            ConnectionState nextState = handshake.getNextConnectionState();
            ClientVersion clientVersion = handshake.getClientVersion();

            //Update client version for this event call(and user)
            user.setClientVersion(clientVersion);
            PacketEvents.getAPI().getLogManager().debug("Processed " + address.getHostString() + ":" + address.getPort() + "'s client version. Client Version: " + clientVersion.getReleaseName());
            event.getPostTasks().add(() -> {
                //Transition into the LOGIN OR STATUS connection state
                PacketEvents.getAPI().getInjector().changeConnectionState(channel, nextState);
                PacketEvents.getAPI().getLogManager().debug("Transitioned " + address.getHostString() + ":" + address.getPort() + " into the " + nextState + " state!");
            });
        }
    }
}