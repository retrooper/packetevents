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

package io.github.retrooper.packetevents.packetwrappers.play.in.jigsawgenerate;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.vector.Vector3i;

/**
 * Sent when Generate is pressed on the Jigsaw Block interface.
 *
 * @author Tecnio
 * @see <a href="https://wiki.vg/Protocol#Generate_Structure"</a>
 */
public class WrappedPacketInJigsawGenerate extends WrappedPacket {

    public WrappedPacketInJigsawGenerate(final NMSPacket packet) {
        super(packet);
    }

    public Vector3i getBlockPosition() {
        return readBlockPosition(0);
    }

    public void setBlockPosition(final Vector3i blockPosition) {
        writeBlockPosition(0, blockPosition);
    }

    public int getLevels() {
        return readInt(0);
    }

    public void setLevels(final int levels) {
        writeInt(0, levels);
    }

    public boolean isKeepingJigsaws() {
        return readBoolean(0);
    }

    public void setKeepingJigsaws(final boolean keepingJigsaws) {
        writeBoolean(0, keepingJigsaws);
    }
}
