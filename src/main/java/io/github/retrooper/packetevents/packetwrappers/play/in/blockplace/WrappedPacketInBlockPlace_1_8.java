/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
        Object blockPosObj = NMSUtils.generateNMSBlockPos(blockPos.x, blockPos.y, blockPos.z);
        write(NMSUtils.blockPosClass, 1, blockPosObj);
    }

    public ItemStack getItemStack() {
        return NMSUtils.toBukkitItemStack(readObject(0, NMSUtils.nmsItemStackClass));
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
