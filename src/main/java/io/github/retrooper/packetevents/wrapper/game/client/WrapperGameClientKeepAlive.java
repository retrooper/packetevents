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

package io.github.retrooper.packetevents.wrapper.game.client;

import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * This is the server-bound keep-alive packet.
 * The server will frequently send out a (client-bound) keep-alive, each containing a random ID.
 * The client is expected to respond with a (server-bound) keep-alive, containing the same ID that the server sent out.
 */
public class WrapperGameClientKeepAlive extends PacketWrapper {
    private final long id;

    public WrapperGameClientKeepAlive(ClientVersion version, ByteBufAbstract byteBuf) {
        super(version, byteBuf);
        if (version.isNewerThanOrEquals(ClientVersion.v_1_12)) {
            this.id = readLong();
        } else if (version.isNewerThanOrEquals(ClientVersion.v_1_8)) {
            this.id = readVarInt();
        } else {
            this.id = readInt();
        }
    }

    /**
     * Keep-Alive ID.
     *
     * @return ID
     */
    public long getID() {
        return id;
    }
}
