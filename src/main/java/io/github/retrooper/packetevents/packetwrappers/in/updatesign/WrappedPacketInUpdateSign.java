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

public class WrappedPacketInUpdateSign extends WrappedPacket {
    private static boolean v_1_7_mode, strArrayMode;
    private static Class<?> blockPosClass, iChatBaseComponentClass;

    private int x, y, z;
    private String[] lines;

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

    public WrappedPacketInUpdateSign(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        if (v_1_7_mode) {
            x = readInt(0);
            y = readInt(1);
            z = readInt(2);
        } else {
            Object blockPos = readObject(0, blockPosClass);
            try {
                x = Reflection.getField(blockPosClass, int.class, 0).getInt(packet);
                y = Reflection.getField(blockPosClass, int.class, 1).getInt(packet);
                z = Reflection.getField(blockPosClass, int.class, 2).getInt(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (strArrayMode) {
            //1.7.10 and the newest versions use this
            lines = readStringArray(0);
        } else {
            //1.8 uses this for example
            Object[] iChatComponents = readObjectArray(0, iChatBaseComponentClass);
            for (int i = 0; i < iChatComponents.length; i++) {
                lines[i] = WrappedPacketOutChat.
                        toStringFromIChatBaseComponent(iChatComponents[i]);
            }
        }
    }

    public int getBlockPositionX() {
        return x;
    }

    public int getBlockPositionY() {
        return y;
    }

    public int getBlockPositionZ() {
        return z;
    }

    public String[] getTextLines() {
        return lines;
    }
}
