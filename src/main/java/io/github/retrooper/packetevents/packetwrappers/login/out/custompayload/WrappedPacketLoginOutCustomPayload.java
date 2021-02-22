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

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketLoginOutCustomPayload extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> constructor;
    private static Constructor<?> packetDataSerializerConstructor;
    private static Class<?> byteBufClass;
    private static Class<?> packetDataSerializerClass;
    private int messageID;
    private String channelName;
    private byte[] data;

    public WrappedPacketLoginOutCustomPayload(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        Class<?> packetClass = PacketTypeClasses.Login.Server.CUSTOM_PAYLOAD;
        if (packetClass != null) {
            packetDataSerializerClass = NMSUtils.getNMSClassWithoutException("PacketDataSerializer");
            try {
                byteBufClass = NMSUtils.getNettyClass("buffer.ByteBuf");
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }

            try {
                if (packetDataSerializerClass != null) {
                    packetDataSerializerConstructor = packetDataSerializerClass.getConstructor(byteBufClass);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            try {
                constructor = packetClass.getConstructor();
            } catch (NoSuchMethodException e3) {
                throw new IllegalStateException("PacketEvents is unable to resolve the PacketPlayOutCustomPayload constructor.");
            }
        }
    }

    public int getMessageId() {
        if (packet != null) {
            return readInt(0);
        }
        return messageID;
    }

    public String getChannelName() {
        if (packet != null) {
            Object minecraftKey = readObject(0, NMSUtils.minecraftKeyClass);
           return NMSUtils.getStringFromMinecraftKey(minecraftKey);
        }
        return channelName;
    }

    public byte[] getData() {
        if (packet != null) {
            Object dataSerializer = readObject(0, packetDataSerializerClass);
            WrappedPacket byteBufWrapper = new WrappedPacket(new NMSPacket(dataSerializer));
            Object byteBuf = byteBufWrapper.readObject(0, byteBufClass);
            return PacketEvents.get().getByteBufUtil().getBytes(byteBuf);
        }
        return data;
    }

    public void setMessageId(int messageID) {
        if (packet != null) {
            writeInt(0, messageID);
        }
        else {
            this.messageID = messageID;
        }
    }

    public void setChannelName(String channelName) {
        if (packet != null) {
            Object minecraftKey = NMSUtils.generateMinecraftKey(channelName);
            writeObject(0, minecraftKey);
        }
        else {
            this.channelName = channelName;
        }
    }

    //TODO bytebufututil setBytes method, make this method public once that is done
    void setData(byte[] data) {
        Object dataSerializer = readObject(0, packetDataSerializerClass);
        WrappedPacket byteBufWrapper = new WrappedPacket(new NMSPacket(dataSerializer));
        Object byteBuf = byteBufWrapper.readObject(0, byteBufClass);

    }

    @Override
    public Object asNMSPacket() {
        Object byteBufObject = PacketEvents.get().getByteBufUtil().wrappedBuffer(data);
        try {
            Object minecraftKey = NMSUtils.generateMinecraftKey(channelName);
            Object dataSerializer = packetDataSerializerConstructor.newInstance(byteBufObject);
            Object packetInstance = constructor.newInstance();
            WrappedPacket packetWrapper = new WrappedPacket(new NMSPacket(packetInstance));
            packetWrapper.writeInt(0, messageID);
            packetWrapper.writeObject(0, minecraftKey);
            packetWrapper.writeObject(0, dataSerializer);
            return packetInstance;
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isSupported() {
        return PacketTypeClasses.Login.Server.CUSTOM_PAYLOAD != null;
    }
}
