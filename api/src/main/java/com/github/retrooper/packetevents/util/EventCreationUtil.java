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

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.simple.*;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.protocol.player.User;

public class EventCreationUtil {
    public static PacketReceiveEvent createReceiveEvent(Object channel, User user, Object player, Object buffer,
                                                        boolean autoProtocolTranslation) throws PacketProcessException {
        switch (user.getDecoderState()) {
            case HANDSHAKING:
                return new PacketHandshakeReceiveEvent(channel, user, player, buffer, autoProtocolTranslation);
            case STATUS:
                return new PacketStatusReceiveEvent(channel, user, player, buffer, autoProtocolTranslation);
            case LOGIN:
                return new PacketLoginReceiveEvent(channel, user, player, buffer, autoProtocolTranslation);
            case PLAY:
                return new PacketPlayReceiveEvent(channel, user, player, buffer, autoProtocolTranslation);
            case CONFIGURATION:
                return new PacketConfigReceiveEvent(channel, user, player, buffer, autoProtocolTranslation);
        }
        throw new RuntimeException("Unknown connection state " + user.getDecoderState() + "!");
    }

    public static PacketSendEvent createSendEvent(Object channel, User user, Object player, Object buffer,
                                                  boolean autoProtocolTranslation) throws PacketProcessException{
        switch (user.getEncoderState()) {
            case HANDSHAKING:
                return new PacketHandshakeSendEvent(channel, user, player, buffer, autoProtocolTranslation);
            case STATUS:
                return new PacketStatusSendEvent(channel, user, player, buffer, autoProtocolTranslation);
            case LOGIN:
                return new PacketLoginSendEvent(channel, user, player, buffer, autoProtocolTranslation);
            case PLAY:
                return new PacketPlaySendEvent(channel, user, player, buffer, autoProtocolTranslation);
            case CONFIGURATION:
                return new PacketConfigSendEvent(channel, user, player, buffer, autoProtocolTranslation);
        }
        throw new RuntimeException("Unknown connection state " + user.getEncoderState() + "!");
    }
}
