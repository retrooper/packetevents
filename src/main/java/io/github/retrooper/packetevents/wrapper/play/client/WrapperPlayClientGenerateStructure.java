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
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.protocol.data.world.BlockPosition;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * This packet is sent when Generate is pressed on the Jigsaw Block interface.
 */
public class WrapperPlayClientGenerateStructure extends PacketWrapper<WrapperPlayClientGenerateStructure> {
    private BlockPosition blockPosition;
    private int levels;
    private boolean keepJigsaws;

    public WrapperPlayClientGenerateStructure(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientGenerateStructure(BlockPosition blockPosition, int levels, boolean keepJigsaws) {
        super(PacketType.Play.Client.GENERATE_STRUCTURE.getID());
        this.blockPosition = blockPosition;
        this.levels = levels;
        this.keepJigsaws = keepJigsaws;
    }

    @Override
    public void readData() {
        this.blockPosition = new BlockPosition(readLong());
        this.levels = readVarInt();
        this.keepJigsaws = readBoolean();
    }

    @Override
    public void readData(WrapperPlayClientGenerateStructure wrapper) {
        this.blockPosition = wrapper.blockPosition;
        this.levels = wrapper.levels;
        this.keepJigsaws = wrapper.keepJigsaws;
    }

    @Override
    public void writeData() {
        writeLong(this.blockPosition.getSerializedPosition());
        writeVarInt(this.levels);
        writeBoolean(this.keepJigsaws);
    }

    /**
     * The block location of the entity.
     *
     * @return Block location
     */
    public BlockPosition getBlockPosition() {
        return blockPosition;
    }

    /**
     * Modify the block location of the entity.
     *
     * @param blockPosition Block location
     */
    public void setBlockPosition(BlockPosition blockPosition) {
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

    /**
     * Modify levels.
     * @param levels Levels
     */
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

    /**
     * Should we keep the Jigsaws?
     *
     * @param keepJigsaws Keep Jigsaws
     */
    public void setKeepJigsaws(boolean keepJigsaws) {
        this.keepJigsaws = keepJigsaws;
    }
}
