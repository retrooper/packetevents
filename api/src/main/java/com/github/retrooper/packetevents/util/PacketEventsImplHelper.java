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

package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PacketEventsImplHelper {
    public static PacketSendEvent handleClientBoundPacket(Object channel, User user, Object player,
                                                          Object buffer, boolean autoProtocolTranslation, boolean runPostTasks) throws PacketProcessException {
        int preProcessIndex = ByteBufHelper.readerIndex(buffer);
        PacketSendEvent packetSendEvent = EventCreationUtil.createSendEvent(channel, user, player, buffer,
                autoProtocolTranslation);
        int processIndex = ByteBufHelper.readerIndex(buffer);
        PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent, () -> {
            ByteBufHelper.readerIndex(buffer, processIndex);
        });
        if (!packetSendEvent.isCancelled()) {
            PacketWrapper<?> wrapper = packetSendEvent.getLastUsedWrapper();
            if (wrapper != null) {
                ByteBufHelper.clear(buffer);
                int packetId = packetSendEvent.getPacketId();
                packetSendEvent.getLastUsedWrapper().writeVarInt(packetId);
                try {
                    packetSendEvent.getLastUsedWrapper().write();
                } catch (Exception e) {
                    throw new PacketProcessException("PacketEvents failed to re-encode an outgoing packet with its packet wrapper. Packet ID: " + packetId
                            + ". Failed to encode " + wrapper.getClass().getSimpleName() + ".", e);
                }
            }
            ByteBufHelper.readerIndex(buffer, preProcessIndex);
        } else {
            //Make the buffer unreadable for the next handlers
            ByteBufHelper.clear(buffer);
        }

        if (runPostTasks) {
            if (packetSendEvent.hasPostTasks()) {
                for (Runnable task : packetSendEvent.getPostTasks()) {
                    task.run();
                }
            }
        }
        return packetSendEvent;
    }

    public static PacketReceiveEvent handleServerBoundPacket(Object channel, User user,
                                                             Object player,
                                                             Object buffer,
                                                             boolean autoProtocolTranslation) throws PacketProcessException {
        int preProcessIndex = ByteBufHelper.readerIndex(buffer);
        PacketReceiveEvent packetReceiveEvent = EventCreationUtil.createReceiveEvent(channel, user, player, buffer,
                autoProtocolTranslation);
        int processIndex = ByteBufHelper.readerIndex(buffer);
        PacketEvents.getAPI().getEventManager().callEvent(packetReceiveEvent, () -> {
            ByteBufHelper.readerIndex(buffer, processIndex);
        });
        if (!packetReceiveEvent.isCancelled()) {
            //Did they ever use a wrapper?
            if (packetReceiveEvent.getLastUsedWrapper() != null) {
                //Rewrite the buffer
                ByteBufHelper.clear(buffer);
                packetReceiveEvent.getLastUsedWrapper().writeVarInt(packetReceiveEvent.getPacketId());
                packetReceiveEvent.getLastUsedWrapper().write();
            }
            //If no wrappers were used, just pass on the original buffer.

            //Correct the reader index, basically what the next handler is expecting.
            ByteBufHelper.readerIndex(buffer, preProcessIndex);
        } else {
            //Cancelling the packet, lets clear the buffer
            ByteBufHelper.clear(buffer);
        }
        if (packetReceiveEvent.hasPostTasks()) {
            for (Runnable task : packetReceiveEvent.getPostTasks()) {
                task.run();
            }
        }
        return packetReceiveEvent;
    }

    public static void handleDisconnection(Object channel, @Nullable UUID uuid) {
        synchronized (channel) {
            User user = ProtocolManager.USERS.get(channel);

            if (user != null) {
                UserDisconnectEvent disconnectEvent = new UserDisconnectEvent(user);
                PacketEvents.getAPI().getEventManager().callEvent(disconnectEvent);
                ProtocolManager.USERS.remove(user.getChannel());
            }

            if (uuid == null) {
                // Only way to be sure of removing a channel
                ProtocolManager.CHANNELS.entrySet().removeIf(pair -> pair.getValue() == channel);
            } else {
                // This is the efficient way that we should prefer
                ProtocolManager.CHANNELS.remove(uuid);
            }
        }
    }
}
