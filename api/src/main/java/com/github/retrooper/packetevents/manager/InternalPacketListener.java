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
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerRegistryData;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerRegistryData.RegistryElement;
import com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerLoginSuccess;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRespawn;

import java.net.InetSocketAddress;
import java.util.List;

public class InternalPacketListener extends PacketListenerAbstract {

    private static final ResourceLocation DIMENSION_TYPE_REGISTRY_KEY =
            ResourceLocation.minecraft("dimension_type");

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

        // The server sends dimension information in configuration phase >= 1.20.2
        else if (event.getPacketType() == PacketType.Configuration.Server.REGISTRY_DATA) {
            WrapperConfigServerRegistryData registryData = new WrapperConfigServerRegistryData(event);

            // Store world data
            NBTCompound registryDataTag = registryData.getRegistryData();
            NBTList<NBTCompound> list = null;
            if (registryDataTag != null) { // <1.20.5
                //Handle dimension type
                NBTCompound dimensionTypes = registryDataTag
                        .getCompoundTagOrNull(DIMENSION_TYPE_REGISTRY_KEY.toString());
                if (dimensionTypes == null) {
                    dimensionTypes = registryDataTag
                            .getCompoundTagOrNull(DIMENSION_TYPE_REGISTRY_KEY.getKey());
                }
                if (dimensionTypes != null) {
                    list = dimensionTypes.getCompoundListTagOrNull("value");
                }
                if (list == null) {
                    list = new NBTList<>(NBTType.COMPOUND);
                    PacketEvents.getAPI().getLogger().warning("Can't find dimension type registry in registry data, "
                            + "this may cause issues; available registries: " + registryDataTag.getTags().keySet());
                }
            } else if (DIMENSION_TYPE_REGISTRY_KEY.equals(registryData.getRegistryKey())) { // >=1.20.5
                // remap to legacy format
                list = new NBTList<>(NBTType.COMPOUND);
                List<RegistryElement> elements = registryData.getElements();
                if (elements != null) {
                    int i = 0;
                    for (RegistryElement element : elements) {
                        NBTCompound tag = new NBTCompound();
                        tag.setTag("name", new NBTString(element.getId().toString()));
                        tag.setTag("id", new NBTInt(i++));
                        if (element.getData() != null) { // may be null because of known packs not being sent
                            tag.setTag("element", element.getData());
                        }
                        list.addTag(tag);
                    }
                }
            }
            if (list != null) {
                user.setWorldNBT(list);
            }
        }

        // The server sends dimension information in login packet for >= 1.17 and < 1.20.2
        else if (event.getPacketType() == PacketType.Play.Server.JOIN_GAME) {
            WrapperPlayServerJoinGame joinGame = new WrapperPlayServerJoinGame(event);
            user.setEntityId(joinGame.getEntityId());
            user.setDimension(joinGame.getDimension());
            if (event.getServerVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
                return; // Fixed world height, no tags are sent to the client
            }

            // Store world data
            NBTCompound dimensionCodec = joinGame.getDimensionCodec();
            if (dimensionCodec != null) {
                NBTList<NBTCompound> types = null;
                NBTCompound dimensionTypes = dimensionCodec
                        .getCompoundTagOrNull(DIMENSION_TYPE_REGISTRY_KEY.toString());
                if (dimensionTypes == null) {
                    dimensionTypes = dimensionCodec
                            .getCompoundTagOrNull(DIMENSION_TYPE_REGISTRY_KEY.getKey());
                }
                if (dimensionTypes != null) { // account for dimension type registry somehow going missing
                    types = dimensionTypes.getCompoundListTagOrNull("value");
                }
                if (types == null) {
                    types = new NBTList<>(NBTType.COMPOUND);
                    PacketEvents.getAPI().getLogger().warning("Can't find dimension type registry in join packet, "
                            + "this may cause issues; available registries: " + dimensionCodec.getTags().keySet());
                }
                user.setWorldNBT(types);
            }

            // Update world height
            user.switchDimensionType(joinGame.getServerVersion(), joinGame.getDimension());
        }

        // Respawn is used to switch dimensions
        else if (event.getPacketType() == PacketType.Play.Server.RESPAWN) {
            WrapperPlayServerRespawn respawn = new WrapperPlayServerRespawn(event);
            user.setDimension(respawn.getDimension());
            if (event.getServerVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
                return; // Fixed world height, no tags are sent to the client
            }

            user.switchDimensionType(respawn.getServerVersion(), respawn.getDimension());
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
