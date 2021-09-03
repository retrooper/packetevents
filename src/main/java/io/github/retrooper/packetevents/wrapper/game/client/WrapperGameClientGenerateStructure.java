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
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * This packet is sent when Generate is pressed on the Jigsaw Block interface.
 */
public class WrapperGameClientGenerateStructure extends PacketWrapper<WrapperGameClientGenerateStructure> {
    private Vector3i blockPosition;
    private int levels;
    private boolean keepJigsaws;

    public WrapperGameClientGenerateStructure(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperGameClientGenerateStructure(ClientVersion clientVersion, Vector3i blockPosition, int levels, boolean keepJigsaws) {
        super(PacketType.Game.Client.GENERATE_STRUCTURE.getPacketID(clientVersion), clientVersion);
        this.blockPosition = blockPosition;
        this.levels = levels;
        this.keepJigsaws = keepJigsaws;
    }

    @Override
    public void readData() {
        long vectorLong = readLong();
        this.blockPosition = PacketWrapperUtil.readVectorFromLong(vectorLong);
        this.levels = readVarInt();
        this.keepJigsaws = readBoolean();
    }

    @Override
    public void readData(WrapperGameClientGenerateStructure wrapper) {
        this.blockPosition = wrapper.blockPosition;
        this.levels = wrapper.levels;
        this.keepJigsaws = wrapper.keepJigsaws;
    }

    @Override
    public void writeData() {
        writeLong(PacketWrapperUtil.generateLongFromVector(this.blockPosition));
        writeVarInt(this.levels);
        writeBoolean(this.keepJigsaws);
    }

    /**
     * The block location of the entity.
     *
     * @return Block location
     */
    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    /**
     * Value of the levels' slider/max depth to generate.
     *
     * @return Levels
     */
    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    /**
     * Should we keep the Jigsaws?
     *
     * @return Keep Jigsaws
     */
    public boolean isKeepingJigsaws() {
        return keepJigsaws;
    }

    public void setKeepJigsaws(boolean keepJigsaws) {
        this.keepJigsaws = keepJigsaws;
    }
}
