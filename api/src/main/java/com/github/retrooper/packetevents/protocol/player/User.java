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
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.chat.ChatPosition;
import com.github.retrooper.packetevents.util.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import net.kyori.adventure.text.Component;

public class User {
    private final ChannelAbstract channel;
    private final UserProfile profile;
    public User(ChannelAbstract channel, UserProfile profile) {
        this.channel = channel;
        this.profile = profile;
    }

    public ChannelAbstract getChannel() {
        return channel;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void chat(String message) {
        //Fake an incoming chat packet
        WrapperPlayClientChatMessage chatMessage = new WrapperPlayClientChatMessage(message);
        PacketEvents.getAPI().getServerManager().receivePacket(channel, chatMessage);
    }

    //TODO Support colors
    public void sendMessage(String message) {
        Component component = Component.text(message);
        sendMessage(component);
    }

    public void sendMessage(Component component) {
        sendMessage(component, ChatPosition.CHAT);
    }

    public void sendMessage(Component component, ChatPosition position) {
        WrapperPlayServerChatMessage chatMessage = new WrapperPlayServerChatMessage(component, position);
        PacketEvents.getAPI().getPlayerManager().sendPacket(channel, chatMessage);
    }

    //TODO sendTitle that is cross-version
}
