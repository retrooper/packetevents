/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package io.github.retrooper.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.factory.nettystom.NettystomPacketEventsBuilder;
import io.github.retrooper.packetevents.injector.NettystomConnectionInitializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Convenience facade for embedding packetevents in a nettystom server.
 *
 * <p>Because nettystom handles framing, compression and encryption inside
 * {@code PlayerSocketConnection} (not as Netty pipeline handlers), the recommended
 * integration is to call {@link #handleServerBound(Channel, ByteBuf)} /
 * {@link #handleClientBound(Channel, ByteBuf)} from your server at the points where the
 * buffer contains a single, plaintext packet. See the module README for details.
 */
public final class NettystomPacketEvents {

    private NettystomPacketEvents() {
    }

    /**
     * Builds and registers the global {@link PacketEventsAPI} instance for this server.
     * You still need to call {@code PacketEvents.getAPI().load()} and {@code init()}.
     */
    public static PacketEventsAPI<Object> create(String id) {
        PacketEventsAPI<Object> api = NettystomPacketEventsBuilder.build(id);
        PacketEvents.setAPI(api);
        return api;
    }

    /**
     * Builds and registers the global {@link PacketEventsAPI} instance for this server.
     */
    public static PacketEventsAPI<Object> create(String id, PacketEventsSettings settings) {
        PacketEventsAPI<Object> api = NettystomPacketEventsBuilder.build(id, settings);
        PacketEvents.setAPI(api);
        return api;
    }

    /**
     * Registers a new connection with packetevents. Call this once a channel is opened.
     *
     * @return the created {@link User}, or {@code null} if the connection was rejected.
     */
    public static @Nullable User onConnect(Channel channel, ConnectionState state, ClientVersion clientVersion) {
        return NettystomConnectionInitializer.initChannel(channel, state, clientVersion);
    }

    /**
     * Notifies packetevents that a connection has been closed.
     */
    public static void onDisconnect(Channel channel, @Nullable UUID uuid) {
        PacketEventsImplHelper.handleDisconnection(channel, uuid);
    }

    /**
     * Associates a nettystom player with an already-registered connection.
     */
    public static void setPlayer(Channel channel, Object player) {
        PacketEvents.getAPI().getInjector().setPlayer(channel, player);
    }

    /**
     * Processes an inbound (server-bound) plaintext packet buffer through packetevents.
     *
     * @return the fired {@link PacketReceiveEvent} (check {@link PacketReceiveEvent#isCancelled()}),
     * or {@code null} if there was no user for the channel or the buffer was empty.
     */
    public static @Nullable PacketReceiveEvent handleServerBound(Channel channel, ByteBuf buffer) throws Exception {
        User user = PacketEvents.getAPI().getProtocolManager().getUser(channel);
        if (user == null) {
            return null;
        }
        return PacketEventsImplHelper.handleServerBoundPacket(channel, user, null, buffer, false);
    }

    /**
     * Processes an outbound (client-bound) plaintext packet buffer through packetevents.
     *
     * @return the fired {@link PacketSendEvent} (check {@link PacketSendEvent#isCancelled()}),
     * or {@code null} if there was no user for the channel or the buffer was empty.
     */
    public static @Nullable PacketSendEvent handleClientBound(Channel channel, ByteBuf buffer) throws Exception {
        User user = PacketEvents.getAPI().getProtocolManager().getUser(channel);
        if (user == null) {
            return null;
        }
        return PacketEventsImplHelper.handleClientBoundPacket(channel, user, null, buffer, false);
    }
}
