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
 * A response to the ping packet.
 * The vanilla server doesn't seem to send the Ping packet.
 * Most likely added as a replacement to the removed Window Confirmation packet.
 *
 * @see io.github.retrooper.packetevents.wrapper.game.server.WrapperGameServerPing
 */
public class WrapperGameClientPong extends PacketWrapper {
    private final int id;

    public WrapperGameClientPong(ClientVersion version, ByteBufAbstract byteBuf) {
        super(version, byteBuf);
        this.id = readInt();
    }

    /**
     * ID of the last sent Ping packet.
     *
     * @return ID
     */
    public int getID() {
        return id;
    }
}
