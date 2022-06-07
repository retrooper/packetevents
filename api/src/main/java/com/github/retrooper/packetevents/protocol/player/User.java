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

package com.github.retrooper.packetevents.protocol.player;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.chat.ChatPosition;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.util.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;

public class User {
    private final Object channel;
    private ConnectionState connectionState;
    private ClientVersion clientVersion;
    private final UserProfile profile;
    private int entityId = -1;
    private int minWorldHeight = 0;
    private int totalWorldHeight = 256;
    private List<NBTCompound> worldNBT;

    public User(Object channel,
                ConnectionState connectionState, ClientVersion clientVersion,
                UserProfile profile) {
        this.channel = channel;
        this.connectionState = connectionState;
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
        return connectionState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
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

    public void writePacket(PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().writePacket(channel, wrapper);
    }

    public void flushPackets() {
        ChannelHelper.flush(channel);
    }

    public void closeConnection() {
        ChannelHelper.close(channel);
    }

    public void chat(String message) {
        //Fake an incoming chat packet
        WrapperPlayClientChatMessage chatMessage = new WrapperPlayClientChatMessage(message);
        PacketEvents.getAPI().getProtocolManager().receivePacket(channel, chatMessage);
    }

    public void closeInventory() {
        WrapperPlayServerCloseWindow closeWindow = new WrapperPlayServerCloseWindow(0);
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, closeWindow);
    }

    public void sendMessage(String legacyMessage) {
        Component component = AdventureSerializer.fromLegacyFormat(legacyMessage);
        sendMessage(component);
    }

    public void sendMessage(Component component) {
        sendMessage(component, ChatPosition.CHAT);
    }

    public void sendMessage(Component component, ChatPosition position) {
        WrapperPlayServerSystemChatMessage chatMessage = new WrapperPlayServerSystemChatMessage(component, position);
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, chatMessage);
    }

    public void sendTitle(String legacyTitle, String legacySubtitle,
                          int fadeInTicks, int stayTicks, int fadeOutTicks) {
        Component title = AdventureSerializer.fromLegacyFormat(legacyTitle);
        Component subtitle = AdventureSerializer.fromLegacyFormat(legacySubtitle);
        sendTitle(title, subtitle, fadeInTicks, stayTicks, fadeOutTicks);
    }

    public void sendTitle(Component title, Component subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        boolean modern = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_17);
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
        }
        else {
            animation = new WrapperPlayServerTitle(WrapperPlayServerTitle.
                    TitleAction.SET_TIMES_AND_DISPLAY, (Component)null, null, null,
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

    public void setWorldNBT(NBTList<NBTCompound> worldNBT) {
        this.worldNBT = worldNBT.getTags();
    }

    @Nullable
    public NBTCompound getWorldNBT(String worldName) {
        for (NBTCompound compound : worldNBT) {
            if (compound.getStringTagOrNull("name").getValue().equals(worldName)) {
                return compound;
            }
        }
        return null;
    }
}
