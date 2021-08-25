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

package io.github.retrooper.packetevents.wrapper.game.server;

import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * This packet is currently used by mods to synchronize the client with the server.
 * It is not used by the vanilla server.
 * Most likely added as a replacement to the removed Window Confirmation packet.
 * @see io.github.retrooper.packetevents.wrapper.game.client.WrapperGameClientPong
 */
public class WrapperGameServerPing extends PacketWrapper {
    private final int id;
    public WrapperGameServerPing(ByteBufAbstract byteBuf) {
        super(byteBuf);
        this.id = readInt();
    }

    public int getID() {
        return id;
    }
}
