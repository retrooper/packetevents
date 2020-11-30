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

package io.github.retrooper.packetevents.packetwrappers.in.updatesign;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.out.chat.WrappedPacketOutChat;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;

import java.lang.reflect.InvocationTargetException;

public class WrappedPacketInUpdateSign extends WrappedPacket {
    private static boolean v_1_7_mode, strArrayMode;
    private static Class<?> blockPosClass, iChatBaseComponentClass;

    private Object blockPosObj;

    public WrappedPacketInUpdateSign(Object packet) {
        super(packet);
    }

    public static void load() {
        v_1_7_mode = Reflection.getField(PacketTypeClasses.Client.UPDATE_SIGN, int.class, 0) != null;
        strArrayMode = Reflection.getField(PacketTypeClasses.Client.UPDATE_SIGN, String[].class, 0) != null;

        try {
            blockPosClass = NMSUtils.getNMSClass("BlockPosition");
        } catch (ClassNotFoundException e) {
            if (!v_1_7_mode) {
                e.printStackTrace();
            }
        }
        try {
            iChatBaseComponentClass = NMSUtils.getNMSClass("IChatBaseComponent");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getX() {
        if (v_1_7_mode) {
            return readInt(0);
        } else {
            if (blockPosObj == null) {
                blockPosObj = readObject(0, blockPosClass);

            }
            try {
                return (int) Reflection.getMethod(blockPosObj.getClass().getSuperclass(), "getX", 0).invoke(blockPosObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }

    public int getY() {
        if (v_1_7_mode) {
            return readInt(1);
        } else {
            if (blockPosObj == null) {
                blockPosObj = readObject(0, blockPosClass);

            }
            try {
                return (int) Reflection.getMethod(blockPosObj.getClass().getSuperclass(), "getY", 0).invoke(blockPosObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }

    public int getZ() {
        if (v_1_7_mode) {
            return readInt(2);
        } else {
            if (blockPosObj == null) {
                blockPosObj = readObject(0, blockPosClass);

            }
            try {
                return (int) Reflection.getMethod(blockPosObj.getClass().getSuperclass(), "getZ", 0).invoke(blockPosObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }

    public String[] getTextLines() {
        if (strArrayMode) {
            //1.7.10 and the newest versions use this
            return readStringArray(0);
        } else {
            //1.8 uses this for example
            Object[] iChatComponents = (Object[]) readAnyObject(1);
            String[] lines = new String[iChatComponents.length];
            for (int i = 0; i < iChatComponents.length; i++) {
                lines[i] = WrappedPacketOutChat.
                        toStringFromIChatBaseComponent(iChatComponents[i]);
            }
            return lines;
        }
    }
}
