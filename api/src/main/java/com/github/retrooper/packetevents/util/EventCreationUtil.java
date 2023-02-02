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

package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.simple.*;
import com.github.retrooper.packetevents.exception.InvalidPacketIdException;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;

public class EventCreationUtil {
    public static PacketReceiveEvent createReceiveEvent(Object channel, User user, Object player, Object buffer,
                                                        boolean autoProtocolTranslation) throws PacketProcessException {
        switch (user.getConnectionState()) {
            case HANDSHAKING:
                return new PacketHandshakeReceiveEvent(channel, user, player, buffer, autoProtocolTranslation);
            case STATUS:
                return new PacketStatusReceiveEvent(channel, user, player, buffer, autoProtocolTranslation);
            case LOGIN:
                return new PacketLoginReceiveEvent(channel, user, player, buffer, autoProtocolTranslation);
            case PLAY:
                return new PacketPlayReceiveEvent(channel, user, player, buffer, autoProtocolTranslation);
            default:
                return null;
        }
    }

    public static PacketSendEvent createSendEvent(Object channel, User user, Object player, Object buffer,
                                                  boolean autoProtocolTranslation) throws PacketProcessException{
        try {
            switch (user.getConnectionState()) {
                case HANDSHAKING:
                    return new PacketHandshakeSendEvent(channel, user, player, buffer, autoProtocolTranslation);
                case STATUS:
                    return new PacketStatusSendEvent(channel, user, player, buffer, autoProtocolTranslation);
                case LOGIN:
                    return new PacketLoginSendEvent(channel, user, player, buffer, autoProtocolTranslation);
                case PLAY:
                    return new PacketPlaySendEvent(channel, user, player, buffer, autoProtocolTranslation);
                default:
                    return null;
            }
        } catch (InvalidPacketIdException e) {
            if (user.getConnectionState() != ConnectionState.PLAY) {
                // TODO: This hack may be removed once we verify that we don't miss the transition to the PLAY state https://github.com/retrooper/packetevents/issues/533
                user.setConnectionState(ConnectionState.PLAY);
                e.printStackTrace();
                PacketEvents.getAPI().getLogger().warning("Transitioning to PLAY state, assuming we missed a packet somewhere");
                return createSendEvent(channel, user, player, buffer, autoProtocolTranslation);
            }
            return null;
        }
    }
}
