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

package io.github.retrooper.packetevents.packetwrappers.play.in.updatesign;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.vector.Vector3i;

import java.lang.reflect.InvocationTargetException;

public class WrappedPacketInUpdateSign extends WrappedPacket {
    private static boolean v_1_7_mode, strArrayMode;
    private static Class<?> blockPosClass;

    public WrappedPacketInUpdateSign(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        v_1_7_mode = Reflection.getField(PacketTypeClasses.Play.Client.UPDATE_SIGN, int.class, 0) != null;
        strArrayMode = Reflection.getField(PacketTypeClasses.Play.Client.UPDATE_SIGN, String[].class, 0) != null;
        if (!v_1_7_mode) {
            try {
                blockPosClass = NMSUtils.getNMSClass("BlockPosition");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Vector3i getBlockPosition() {
        int x = 0;
        int y = 0;
        int z = 0;
        if (v_1_7_mode) {
            x = readInt(0);
            y = readInt(1);
            z = readInt(2);

        } else {
            Object blockPosObj = readObject(0, blockPosClass);
            try {
                x = (int) NMSUtils.getBlockPosX.invoke(blockPosObj);
                y = (int) NMSUtils.getBlockPosY.invoke(blockPosObj);
                z = (int) NMSUtils.getBlockPosZ.invoke(blockPosObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new Vector3i(x, y, z);
    }

    public void setBlockPosition(Vector3i blockPos) {
        if (v_1_7_mode) {
            writeInt(0, blockPos.getX());
            writeInt(1, blockPos.getY());
            writeInt(2, blockPos.getZ());
        } else {
            Object blockPosObj = NMSUtils.generateNMSBlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            write(NMSUtils.blockPosClass, 0, blockPosObj);
        }
    }

    public String[] getTextLines() {
        if (strArrayMode) {
            //1.7.10
            return readStringArray(0);
        } else {
            //1.8 and above
            Object[] iChatComponents = (Object[]) readAnyObject(1);
            return NMSUtils.readIChatBaseComponents(iChatComponents);
        }
    }

    public void setTextLines(String[] lines) {
        if (strArrayMode) {
            writeStringArray(0, lines);
        } else {
            Object[] iChatComponents = NMSUtils.generateIChatBaseComponents(lines);
            writeAnyObject(1, iChatComponents);
        }
    }
}
