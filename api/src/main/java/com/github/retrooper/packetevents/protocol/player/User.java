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

package com.github.retrooper.packetevents.protocol.player;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.chat.ChatType;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessageLegacy;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_16;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.world.Dimension;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCloseWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleSubtitle;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleText;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleTimes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSystemChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTitle;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;

public class User {
    private final Object channel;
    private ConnectionState decoderState;
    private ConnectionState encoderState;
    private ClientVersion clientVersion;
    private final UserProfile profile;
    private int entityId = -1;
    private int minWorldHeight = 0;
    private int totalWorldHeight = 256;
    private List<NBTCompound> worldNBT;
    private Dimension dimension = new Dimension(0);

    public User(Object channel,
                ConnectionState connectionState, ClientVersion clientVersion,
                UserProfile profile) {
        this.channel = channel;
        this.decoderState = connectionState;
        this.encoderState = connectionState;
        this.clientVersion = clientVersion;
        this.profile = profile;
    }

    public Object getChannel() {
        return channel;
    }

    public InetSocketAddress getAddress() {
        return (InetSocketAddress) ChannelHelper.remoteAddress(channel);
    }

    public ConnectionState getConnectionState() {
        ConnectionState decoderState = this.decoderState;
        ConnectionState encoderState = this.encoderState;
        if (decoderState != encoderState) {
            throw new IllegalArgumentException("Can't get common connection state: " + decoderState + " != " + encoderState);
        }
        return decoderState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        this.setDecoderState(connectionState);
        this.setEncoderState(connectionState);
    }

    public ConnectionState getDecoderState() {
        return this.decoderState;
    }

    public void setDecoderState(ConnectionState decoderState) {
        this.decoderState = decoderState;
        PacketEvents.getAPI().getLogManager().debug(
                "Transitioned " + this.getName() + "'s decoder into " + decoderState + " state!");
    }

    public ConnectionState getEncoderState() {
        return this.encoderState;
    }

    public void setEncoderState(ConnectionState encoderState) {
        this.encoderState = encoderState;
        PacketEvents.getAPI().getLogManager().debug(
                "Transitioned " + this.getName() + "'s encoder into " + encoderState + " state!");
    }

    public ClientVersion getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(ClientVersion clientVersion) {
        this.clientVersion = clientVersion;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public String getName() {
        return profile.getName();
    }

    public UUID getUUID() {
        return profile.getUUID();
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public void sendPacket(Object buffer) {
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, buffer);
    }

    public void sendPacket(PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, wrapper);
    }

    public void sendPacketSilently(PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().sendPacketSilently(channel, wrapper);
    }

    public void writePacket(PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().writePacket(channel, wrapper);
    }

    public void flushPackets() {
        ChannelHelper.flush(channel);
    }

    public void closeConnection() {
        ChannelHelper.close(channel);
    }

    //Might be tough with the message signing
   /* public void chat(String message) {
        //Fake an incoming chat packet
        WrapperPlayClientChatMessage chatMessage = new WrapperPlayClientChatMessage(message);
        PacketEvents.getAPI().getProtocolManager().receivePacket(channel, chatMessage);
    }*/

    public void closeInventory() {
        WrapperPlayServerCloseWindow closeWindow = new WrapperPlayServerCloseWindow(0);
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, closeWindow);
    }

    public void sendMessage(String legacyMessage) {
        Component component = AdventureSerializer.fromLegacyFormat(legacyMessage);
        sendMessage(component);
    }

    public void sendMessage(Component component) {
        sendMessage(component, ChatTypes.CHAT);
    }

    public void sendMessage(Component component, ChatType type) {
        ServerVersion version = PacketEvents.getAPI().getInjector().isProxy() ? getClientVersion().toServerVersion() :
                PacketEvents.getAPI().getServerManager().getVersion();
        PacketWrapper<?> chatPacket;
        if (version.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            chatPacket = new WrapperPlayServerSystemChatMessage(false, component);
        } else {
            ChatMessage message;
            if (version.isNewerThanOrEquals(ServerVersion.V_1_16)) {
                message = new ChatMessage_v1_16(component, type, new UUID(0L, 0L));
            } else {
                message = new ChatMessageLegacy(component, type);
            }
            chatPacket = new WrapperPlayServerChatMessage(message);
        }
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, chatPacket);
    }

    public void sendTitle(String legacyTitle, String legacySubtitle,
                          int fadeInTicks, int stayTicks, int fadeOutTicks) {
        Component title = AdventureSerializer.fromLegacyFormat(legacyTitle);
        Component subtitle = AdventureSerializer.fromLegacyFormat(legacySubtitle);
        sendTitle(title, subtitle, fadeInTicks, stayTicks, fadeOutTicks);
    }

    public void sendTitle(Component title, Component subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        ClientVersion version = PacketEvents.getAPI().getInjector().isProxy() ? getClientVersion() :
                PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
        boolean modern = version.isNewerThanOrEquals(ClientVersion.V_1_17);
        PacketWrapper<?> animation;
        PacketWrapper<?> setTitle = null;
        PacketWrapper<?> setSubtitle = null;
        if (modern) {
            animation = new WrapperPlayServerSetTitleTimes(fadeInTicks, stayTicks, fadeOutTicks);
            if (title != null) {
                setTitle = new WrapperPlayServerSetTitleText(title);
            }
            if (subtitle != null) {
                setSubtitle = new WrapperPlayServerSetTitleSubtitle(subtitle);
            }
        } else {
            animation = new WrapperPlayServerTitle(WrapperPlayServerTitle.
                    TitleAction.SET_TIMES_AND_DISPLAY, (Component) null, null, null,
                    fadeInTicks, stayTicks, fadeOutTicks);
            if (title != null) {
                setTitle = new WrapperPlayServerTitle(WrapperPlayServerTitle.
                        TitleAction.SET_TITLE, title, null, null,
                        0, 0, 0);
            }
            if (subtitle != null) {
                setSubtitle = new WrapperPlayServerTitle(WrapperPlayServerTitle.
                        TitleAction.SET_SUBTITLE, null, subtitle, null,
                        0, 0, 0);
            }
        }
        sendPacket(animation);
        if (setTitle != null) {
            sendPacket(setTitle);
        }
        if (setSubtitle != null) {
            sendPacket(setSubtitle);
        }
    }

    //TODO sendTitle that is cross-version

    public int getMinWorldHeight() {
        return minWorldHeight;
    }

    public void setMinWorldHeight(int minWorldHeight) {
        this.minWorldHeight = minWorldHeight;
    }

    public int getTotalWorldHeight() {
        return totalWorldHeight;
    }

    public void setTotalWorldHeight(int totalWorldHeight) {
        this.totalWorldHeight = totalWorldHeight;
    }

    public void switchDimensionType(ServerVersion version, Dimension dimension) {
        NBTCompound dimensionData = this.getWorldNBT(dimension);
        if (dimensionData != null) {
            NBTCompound worldNBT = dimensionData.getCompoundTagOrNull("element");
            if (worldNBT != null) {
                this.setMinWorldHeight(worldNBT.getNumberTagOrNull("min_y").getAsInt());
                this.setTotalWorldHeight(worldNBT.getNumberTagOrNull("height").getAsInt());
                return;
            } else {
                this.setDefaultWorldHeights(version, dimension);
            }
        }
        if (version.isOlderThan(ServerVersion.V_1_20_5)
                || this.clientVersion.isOlderThan(ClientVersion.V_1_20_5)) {
            // hide this warning on 1.20.5, as the server does no longer send
            // vanilla datapack dimension type contents
            //
            // this 1.20.5 feature can be fully worked around with by clearing the
            // known packs sent by the client to the server durings config phase
            PacketEvents.getAPI().getLogger().warning(
                    "No data was sent for dimension " + dimensionData + " to " + this.getName());
        }
    }

    public void setDefaultWorldHeights(ServerVersion version, Dimension dimension) {
        boolean extended = version.isNewerThanOrEquals(ServerVersion.V_1_18)
                && "minecraft:overworld".equals(this.getWorldName(dimension));
        this.setDefaultWorldHeights(extended);
    }

    public void setDefaultWorldHeights(boolean extended) {
        this.minWorldHeight = extended ? -64 : 0;
        this.totalWorldHeight = extended ? 256 + 128 : 256;
    }

    public void setWorldNBT(NBTList<NBTCompound> worldNBT) {
        this.worldNBT = worldNBT.getTags();
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    @Nullable
    public NBTCompound getWorldNBT(String worldName) {
        if (worldNBT == null) {
            return null;
        }
        for (NBTCompound compound : worldNBT) {
            if (compound.getStringTagOrNull("name").getValue().equals(worldName)) {
                return compound;
            }
        }
        return null;
    }

    public @Nullable NBTCompound getWorldNBT(int worldId) {
        if (this.worldNBT == null) {
            return null;
        }
        for (NBTCompound element : this.worldNBT) {
            if (element.getNumberTagOrNull("id").getAsInt() == worldId) {
                return element;
            }
        }
        return null;
    }

    public @Nullable NBTCompound getWorldNBT(Dimension dimension) {
        String dimensionName = dimension.getDimensionName();
        if (!dimensionName.isEmpty()) {
            return this.getWorldNBT(dimensionName);
        }
        return this.getWorldNBT(dimension.getId());
    }

    public @Nullable String getWorldName(int worldId) {
        if (this.worldNBT == null) {
            return null;
        }
        for (NBTCompound element : this.worldNBT) {
            if (element.getNumberTagOrNull("id").getAsInt() == worldId) {
                return element.getStringTagValueOrNull("name");
            }
        }
        return null;
    }

    public @Nullable String getWorldName(Dimension dimension) {
        String dimensionName = dimension.getDimensionName();
        return dimensionName.isEmpty() ? this.getWorldName(dimension.getId()) : dimensionName;
    }
}
