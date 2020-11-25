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

package io.github.retrooper.packetevents.packetwrappers.login.out.custompayload;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.bytebuf.ByteBufUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketLoginOutCustomPayload extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> constructor;
    private static Constructor<?> packetDataSerializerConstructor;
    private static Constructor<?> minecraftKeyConstructor;
    private static Class<?> byteBufClass;
    private static Class<?> packetDataSerializerClass;
    private static Class<?> minecraftKeyClass;

    public static void load() {
        Class<?> packetClass = PacketTypeClasses.Login.Server.CUSTOM_PAYLOAD;
        if (packetClass != null) {
            packetDataSerializerClass = NMSUtils.getNMSClassWithoutException("PacketDataSerializer");
            minecraftKeyClass = NMSUtils.getNMSClassWithoutException("MinecraftKey");

            try {
                byteBufClass = NMSUtils.getNettyClass("buffer.ByteBuf");
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            try {
                packetDataSerializerConstructor = packetDataSerializerClass.getConstructor(byteBufClass);
            } catch (NullPointerException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                if (minecraftKeyClass != null) {
                    minecraftKeyConstructor = minecraftKeyClass.getConstructor(String.class);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            try {
                constructor = packetClass.getConstructor(minecraftKeyClass, packetDataSerializerClass);
            } catch (NoSuchMethodException e3) {
                throw new IllegalStateException("PacketEvents is unable to resolve the PacketPlayOutCustomPayload constructor.");
            }
        }
    }

    private int messageID;
    private String channelName;
    private byte[] data;

    public WrappedPacketLoginOutCustomPayload(Object packet) {
        super(packet);
    }

    public int getMessageId() {
        if (packet != null) {
            return readInt(0);
        }
        return messageID;
    }

    public String getChannelName() {
        if (packet != null) {
            Object minecraftKey = readObject(0, minecraftKeyClass);
            WrappedPacket minecraftKeyWrapper = new WrappedPacket(minecraftKey);
            return minecraftKeyWrapper.readString(1);
        }
        return channelName;
    }

    public byte[] getData() {
        if (packet != null) {
            Object dataSerializer = readObject(0, packetDataSerializerClass);
            WrappedPacket byteBufWrapper = new WrappedPacket(dataSerializer);
            Object byteBuf = byteBufWrapper.readObject(0, byteBufClass);
            return ByteBufUtil.getBytes(byteBuf);
        }
        return data;
    }

    @Override
    public Object asNMSPacket() {
        Object byteBufObject = ByteBufUtil.copiedBuffer(data);
        try {
            Object minecraftKey = minecraftKeyConstructor.newInstance(channelName);
            Object dataSerializer = packetDataSerializerConstructor.newInstance(byteBufObject);
            return constructor.newInstance(minecraftKey, dataSerializer);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
