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

package io.github.retrooper.packetevents.packetwrappers.play.in.blockplace;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.vector.Vector3f;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import org.bukkit.inventory.ItemStack;

final class WrappedPacketInBlockPlace_1_7_10 extends WrappedPacket {
    WrappedPacketInBlockPlace_1_7_10(final NMSPacket packet) {
        super(packet);
    }

    public Vector3i getBlockPosition() {
        return new Vector3i(readInt(0), readInt(1), readInt(2));
    }

    public void setBlockPosition(Vector3i blockPos) {
        Vector3i currentBlockPos = getBlockPosition();
        writeInt(0, blockPos.x);
        writeInt(1, blockPos.y);
        writeInt(2, blockPos.z);
    }

    public int getFace() {
        return readInt(3);
    }

    public void setFace(int face) {
        writeInt(3, face);
    }

    public ItemStack getItemStack() {
        return readItemStack(0);
    }

    public void setItemStack(ItemStack stack) {
        writeItemStack(0, stack);
    }

    public Vector3f getCursorPosition() {
        return new Vector3f(readFloat(0), readFloat(1), readFloat(2));
    }

    public void setCursorPosition(Vector3f cursorPos) {
        writeFloat(0, cursorPos.x);
        writeFloat(1, cursorPos.y);
        writeFloat(2, cursorPos.z);
    }
}
