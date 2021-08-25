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
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import io.github.retrooper.packetevents.utils.wrapper.PacketWrapperUtils;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * This packet is sent when Generate is pressed on the Jigsaw Block interface.
 */
public class WrapperGameClientGenerateStructure extends PacketWrapper {
    private final Vector3i blockPosition;
    private final int levels;
    private final boolean keepJigsaws;

    public WrapperGameClientGenerateStructure(ClientVersion version, ByteBufAbstract byteBuf) {
        super(version, byteBuf);
        this.blockPosition = PacketWrapperUtils.readVectorFromLong(readLong());
        this.levels = readVarInt();
        this.keepJigsaws = readBoolean();
    }

    /**
     * The block location of the entity.
     *
     * @return Block location
     */
    public Vector3i getBlockPosition() {
        return blockPosition;
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
     * Should we keep the Jigsaws?
     *
     * @return Keep Jigsaws
     */
    public boolean isKeepingJigsaws() {
        return keepJigsaws;
    }
}
