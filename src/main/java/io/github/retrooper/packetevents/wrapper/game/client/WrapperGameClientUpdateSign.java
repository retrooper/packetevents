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

import io.github.retrooper.packetevents.utils.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.github.retrooper.packetevents.utils.wrapper.PacketWrapperUtils;

public class WrapperGameClientUpdateSign extends PacketWrapper {
    //TODO Test the blockposition on as many versions as possible
    private final Vector3i blockPosition;
    private final String[] textLines = new String[4];

    public WrapperGameClientUpdateSign(ClientVersion version, ByteBufAbstract byteBuf) {
        super(version, byteBuf);
        if (version.isNewerThanOrEquals(ClientVersion.v_1_8)) {
            long position = readLong();
            this.blockPosition = PacketWrapperUtils.readVectorFromLong(position);
        } else {
            int x = readInt();
            int y = readShort();
            int z = readInt();
            this.blockPosition = new Vector3i(x, y, z);
        }
        for (int i = 0; i < 4; i++) {
            this.textLines[i] = readString(384);
        }
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public String[] getTextLines() {
        return textLines;
    }
}
