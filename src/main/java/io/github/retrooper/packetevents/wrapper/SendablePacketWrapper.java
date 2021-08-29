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

package io.github.retrooper.packetevents.wrapper;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufUtil;

public abstract class SendablePacketWrapper extends PacketWrapper {
    protected final int packetID;
    protected final int protocolVersion;

    public SendablePacketWrapper(int packetID) {
        super(ClientVersion.UNKNOWN, PacketEvents.get().getServerManager().getVersion(), ByteBufUtil.buffer());
        this.packetID = packetID;
        this.protocolVersion = getServerVersion().getProtocolVersion();
        writeVarInt(packetID);
    }

    public SendablePacketWrapper(int protocolVersion, int packetID) {
        super(ClientVersion.UNKNOWN, PacketEvents.get().getServerManager().getVersion(), ByteBufUtil.buffer());
        this.packetID = packetID;
        this.protocolVersion = protocolVersion;
        writeVarInt(packetID);
    }

    public SendablePacketWrapper(ByteBufAbstract byteBuf, int protocolVersion, int packetID) {
        super(ClientVersion.UNKNOWN, PacketEvents.get().getServerManager().getVersion(), byteBuf);
        this.packetID = packetID;
        this.protocolVersion = protocolVersion;
        writeVarInt(packetID);

    }

    public SendablePacketWrapper(ByteBufAbstract byteBuf, int packetID) {
        super(ClientVersion.UNKNOWN, PacketEvents.get().getServerManager().getVersion(), byteBuf);
        this.packetID = packetID;
        this.protocolVersion = getServerVersion().getProtocolVersion();
        writeVarInt(packetID);
    }

    //Super constructor, this is when they DON'T want to use the wrapper for sending
    public SendablePacketWrapper(PacketSendEvent event) {
        super(event);
        this.packetID = -1;
        this.protocolVersion = -1;
    }

    public abstract void createPacket();
}
