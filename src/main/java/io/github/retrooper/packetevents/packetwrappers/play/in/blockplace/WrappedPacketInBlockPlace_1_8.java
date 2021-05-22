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
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.vector.Vector3f;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

final class WrappedPacketInBlockPlace_1_8 extends WrappedPacket {

    WrappedPacketInBlockPlace_1_8(final NMSPacket packet) {
        super(packet);
    }

    public Vector3i getBlockPosition() {
        Vector3i blockPos = new Vector3i();

        Object blockPosObj = readObject(1, NMSUtils.blockPosClass);
        try {
            blockPos.x = (int) NMSUtils.getBlockPosX.invoke(blockPosObj);
            blockPos.y = (int) NMSUtils.getBlockPosY.invoke(blockPosObj);
            blockPos.z = (int) NMSUtils.getBlockPosZ.invoke(blockPosObj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return blockPos;
    }

    public void setBlockPosition(Vector3i blockPos) {
        Object blockPosObj = NMSUtils.generateNMSBlockPos(blockPos.z, blockPos.y, blockPos.z);
        write(NMSUtils.blockPosClass, 1, blockPosObj);
    }

    public ItemStack getItemStack() {
        return NMSUtils.toBukkitItemStack(readObject(0, NMSUtils.nmsItemStackClass));
    }

    public void setItemStack(ItemStack stack) {
        Object nmsItemStack = NMSUtils.toNMSItemStack(stack);
        write(NMSUtils.nmsItemStackClass, 0, nmsItemStack);
    }

    public int getFace() {
        return readInt(0);
    }

    public void setFace(int face) {
        writeInt(0, face);
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
