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

package com.github.retrooper.packetevents.event.simple;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;

public class PacketConfigSendEvent extends PacketSendEvent {

    public PacketConfigSendEvent(Object channel, User user,
                                 Object player, Object rawByteBuf,
                                 boolean autoProtocolTranslation) throws PacketProcessException {
        super(channel, user, player, rawByteBuf, autoProtocolTranslation);
    }

    protected PacketConfigSendEvent(int packetId, PacketTypeCommon packetType,
                                    ServerVersion serverVersion,
                                    Object channel,
                                    User user, Object player, Object byteBuf) throws PacketProcessException {
        super(packetId, packetType, serverVersion, channel, user, player, byteBuf);
    }

    @Override
    public PacketConfigSendEvent clone() {
        try {
            Object clonedBuffer = ByteBufHelper.retainedDuplicate(getByteBuf());
            return new PacketConfigSendEvent(getPacketId(), getPacketType(), getServerVersion(),
                    getChannel(), getUser(), getPlayer(), clonedBuffer);
        } catch (PacketProcessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PacketType.Configuration.Server getPacketType() {
        return (PacketType.Configuration.Server) super.getPacketType();
    }
}
