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

package io.github.retrooper.packetevents.packetwrappers.play.out.custompayload;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class WrappedPacketOutCustomPayload extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> constructor;
    private static Constructor<?> packetDataSerializerConstructor;
    private static int minecraftKeyIndexInClass;

    private static byte constructorMode = 0;
    private String tag;
    private byte[] data;

    public WrappedPacketOutCustomPayload(String tag, byte[] data) {
        this.tag = tag;
        this.data = data;
    }

    public WrappedPacketOutCustomPayload(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        Class<?> packetClass = PacketTypeClasses.Play.Server.CUSTOM_PAYLOAD;
        try {
            packetDataSerializerConstructor = NMSUtils.packetDataSerializerClass.getConstructor(NMSUtils.byteBufClass);
        } catch (NullPointerException | NoSuchMethodException e) {
            //For some reason some 1.7.10 spigots don't have this constructor although its on normal spigot 1.7.10???
        }

        //Constructors:

        //String, byte[]

        //String, PacketDataSerializer

        //MinecraftKey, PacketDataSerializer
        try {
            //1.7 constructor
            constructor = packetClass.getConstructor(String.class, byte[].class);
            constructorMode = 0;
        } catch (NoSuchMethodException e) {
            //That's fine, just a newer version
            try {
                constructor = packetClass.getConstructor(String.class, NMSUtils.packetDataSerializerClass);
                constructorMode = 1;
            } catch (NoSuchMethodException e2) {
                //That's fine, just an even newer version
                try {
                    //Minecraft key exists
                    for (int i = 0; i < packetClass.getDeclaredFields().length; i++) {
                        Field f = packetClass.getDeclaredFields()[i];
                        if (!Modifier.isStatic(f.getModifiers())) {
                            minecraftKeyIndexInClass = i;
                            break;
                        }
                    }
                    constructor = packetClass.getConstructor(NMSUtils.minecraftKeyClass, NMSUtils.packetDataSerializerClass);
                    constructorMode = 2;
                } catch (NoSuchMethodException e3) {
                    throw new IllegalStateException("PacketEvents is unable to resolve the PacketPlayOutCustomPayload constructor.");
                }
            }
        }
    }

    public String getTag() {
        if (packet != null) {
            switch (constructorMode) {
                case 0:
                case 1:
                    return readString(0);
                case 2:
                    Object minecraftKey = readObject(minecraftKeyIndexInClass, NMSUtils.minecraftKeyClass);
                    return NMSUtils.getStringFromMinecraftKey(minecraftKey);
            }
            return null;
        }
        return tag;
    }

    public void setTag(String tag) {
        if (packet != null) {
            switch (constructorMode) {
                case 0:
                case 1:
                    writeString(0, tag);
                case 2:
                    Object minecraftKey = NMSUtils.generateMinecraftKey(tag);
                    write(NMSUtils.minecraftKeyClass, minecraftKeyIndexInClass, minecraftKey);
            }
        } else {
            this.tag = tag;
        }
    }

    public byte[] getData() {
        if (packet != null) {
            switch (constructorMode) {
                case 0:
                    return readByteArray(0);
                case 1:
                case 2:
                    Object dataSerializer = readObject(0, NMSUtils.packetDataSerializerClass);
                    WrappedPacket byteBufWrapper = new WrappedPacket(new NMSPacket(dataSerializer));

                    Object byteBuf = byteBufWrapper.readObject(0, NMSUtils.byteBufClass);

                    return PacketEvents.get().getByteBufUtil().getBytes(byteBuf);
            }
            return new byte[0];
        }
        return data;
    }

    public void setData(byte[] data) {
        if (packet != null) {
            switch (constructorMode) {
                case 0:
                    writeByteArray(0, data);
                    break;
                case 1:
                case 2:
                    Object dataSerializer = readObject(0, NMSUtils.packetDataSerializerClass);
                    WrappedPacket dataSerializerWrapper = new WrappedPacket(new NMSPacket(dataSerializer));

                    Object byteBuf = PacketEvents.get().getByteBufUtil().newByteBuf(data);
                    dataSerializerWrapper.write(NMSUtils.byteBufClass, 0, byteBuf);
                    break;
            }

        } else {
            this.data = data;
        }
    }

    @Override
    public Object asNMSPacket() {
        byte[] data = getData();
        Object byteBufObject = PacketEvents.get().getByteBufUtil().newByteBuf(data);
        switch (constructorMode) {
            case 0:
                try {
                    return constructor.newInstance(getTag(), data);
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    Object dataSerializer = packetDataSerializerConstructor.newInstance(byteBufObject);
                    return constructor.newInstance(getTag(), dataSerializer);
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            case 2:

                try {
                    Object minecraftKey = NMSUtils.generateMinecraftKey(getTag());
                    Object dataSerializer = packetDataSerializerConstructor.newInstance(byteBufObject);
                    return constructor.newInstance(minecraftKey, dataSerializer);
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }
}
