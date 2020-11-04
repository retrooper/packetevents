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

package io.github.retrooper.packetevents.packetwrappers.in.custompayload;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.bytebuf.ByteBufUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;

public final class WrappedPacketInCustomPayload extends WrappedPacket {
    private static Class<?> nmsMinecraftKey, packetDataSerializerClass, byteBufClass;

    private static boolean strPresent, byteArrayPresent;

    public WrappedPacketInCustomPayload(Object packet) {
        super(packet);
    }

    public static void load() {
        Class<?> packetClass = PacketTypeClasses.Client.CUSTOM_PAYLOAD;
        strPresent = Reflection.getField(packetClass, String.class, 0) != null;
        byteArrayPresent = Reflection.getField(packetClass, byte[].class, 0) != null;
        packetDataSerializerClass = NMSUtils.getNMSClassWithoutException("PacketDataSerializer");
        try {
            byteBufClass = NMSUtils.getNettyClass("buffer.ByteBuf");
        } catch (ClassNotFoundException ignored) {

        }
        try {
            //Only on 1.13+
            nmsMinecraftKey = NMSUtils.getNMSClass("MinecraftKey");
        } catch (ClassNotFoundException e) {
            //Its okay, this means they are on versions 1.7.10 - 1.12.2
        }
    }

    public String getTag() {
        if (strPresent) {
            return readString(0);
        } else {
            Object minecraftKey = readObject(0, nmsMinecraftKey);
            WrappedPacket minecraftKeyWrapper = new WrappedPacket(minecraftKey);
            return minecraftKeyWrapper.readString(1);
        }
    }

    public byte[] getData() {
        if (byteArrayPresent) {
            return readByteArray(0);
        } else {
            Object dataSerializer = readObject(0, packetDataSerializerClass);
            WrappedPacket byteBufWrapper = new WrappedPacket(dataSerializer);

            Object byteBuf = byteBufWrapper.readObject(0, byteBufClass);

            return ByteBufUtil.getBytes(byteBuf);
        }
    }
}
