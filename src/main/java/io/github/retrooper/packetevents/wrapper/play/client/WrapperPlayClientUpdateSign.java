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

package io.github.retrooper.packetevents.wrapper.play.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.protocol.packettype.PacketType;
import io.github.retrooper.packetevents.protocol.data.world.BlockPosition;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * This message is sent from the client to the server when the “Done” button is pushed after placing a sign.
 */
public class WrapperPlayClientUpdateSign extends PacketWrapper<WrapperPlayClientUpdateSign> {
    private final String[] textLines = new String[4];
    private BlockPosition blockPosition;

    public WrapperPlayClientUpdateSign(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientUpdateSign(BlockPosition blockPosition, String[] textLines) {
        super(PacketType.Play.Client.UPDATE_SIGN.getID());
        this.blockPosition = blockPosition;
        System.arraycopy(textLines, 0, this.textLines, 0, 4);
    }

    @Override
    public void readData() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_8)) {
            this.blockPosition = new BlockPosition(readLong());
        } else {
            int x = readInt();
            int y = readShort();
            int z = readInt();
            this.blockPosition = new BlockPosition(x, y, z);
        }
        for (int i = 0; i < 4; i++) {
            this.textLines[i] = readString(384);
        }
    }

    @Override
    public void readData(WrapperPlayClientUpdateSign wrapper) {
        this.blockPosition = wrapper.blockPosition;
        System.arraycopy(wrapper.textLines, 0, this.textLines, 0, 4);
    }

    @Override
    public void writeData() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_8)) {
            long positionVector = blockPosition.getSerializedPosition();
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
    public BlockPosition getBlockPosition() {
        return blockPosition;
    }

    /**
     * Modify the block location of the sign.
     *
     * @param blockPosition Sign position
     */
    public void setBlockPosition(BlockPosition blockPosition) {
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

    /**
     * Modify the text lines in the sign.
     *
     * @param textLines Sign content
     */
    public void setTextLines(String[] textLines) {
        System.arraycopy(textLines, 0, this.textLines, 0, 4);
    }
}
