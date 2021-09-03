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

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import io.github.retrooper.packetevents.utils.PacketWrapperUtil;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.github.retrooper.packetevents.wrapper.SendablePacketWrapper;

/**
 * This message is sent from the client to the server when the “Done” button is pushed after placing a sign.
 */
public class WrapperGameClientUpdateSign extends SendablePacketWrapper<WrapperGameClientUpdateSign> {
    private Vector3i blockPosition;
    private final String[] textLines = new String[4];

    public WrapperGameClientUpdateSign(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperGameClientUpdateSign(ClientVersion clientVersion, Vector3i blockPosition, String[] textLines) {
        super(PacketType.Game.Client.UPDATE_SIGN.getPacketID(clientVersion), clientVersion);
        this.blockPosition = blockPosition;
        System.arraycopy(textLines, 0, this.textLines, 0, 4);
    }

    @Override
    public void readData() {
        if (clientVersion.isNewerThanOrEquals(ClientVersion.v_1_8)) {
            long position = readLong();
            this.blockPosition = PacketWrapperUtil.readVectorFromLong(position);
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

    @Override
    public void readData(WrapperGameClientUpdateSign wrapper) {
        this.blockPosition = wrapper.blockPosition;
        System.arraycopy(wrapper.textLines, 0, this.textLines, 0, 4);
    }

    @Override
    public void writeData() {
        if (clientVersion.isNewerThanOrEquals(ClientVersion.v_1_8)) {
            long positionVector = PacketWrapperUtil.generateLongFromVector(blockPosition);
            writeLong(positionVector);
        } else {
            writeInt(blockPosition.x);
            writeShort(blockPosition.y);
            writeInt(blockPosition.z);
        }
        for (int i = 0; i < 4; i++) {
            writeString(textLines[i], 384);
        }
    }

    /**
     * Block location of the sign.
     *
     * @return Sign position
     */
    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    /**
     * The text lines in the sign.
     *
     * @return Sign content
     */
    public String[] getTextLines() {
        return textLines;
    }

    public void setTextLines(String[] textLines) {
        System.arraycopy(textLines, 0, this.textLines, 0, 4);
    }
}
