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

package io.github.retrooper.packetevents.packetwrappers.play.out.custompayload;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class WrappedPacketOutCustomPayload extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> constructor;
    private static Constructor<?> packetDataSerializerConstructor;
    private static int minecraftKeyIndexInClass;

    private static byte constructorMode;
    private String channelName;
    private byte[] data;

    public WrappedPacketOutCustomPayload(String channelName, byte[] data) {
        this.channelName = channelName;
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

    public String getChannelName() {
        if (packet != null) {
            switch (constructorMode) {
                case 0:
                case 1:
                    return readString(0);
                case 2:
                    return readMinecraftKey(minecraftKeyIndexInClass);
                default:
                    return null;
            }
        }
        else {
            return channelName;
        }
    }

    public void setChannelName(String channelName) {
        if (packet != null) {
            switch (constructorMode) {
                case 0:
                case 1:
                    writeString(0, channelName);
                case 2:
                    writeMinecraftKey(0, channelName);
            }
        }
        else {
            this.channelName = channelName;
        }
    }

    public byte[] getData() {
        if (packet != null) {
            switch (constructorMode) {
                case 0:
                    return readByteArray(0);
                case 1:
                case 2:
                    return PacketEvents.get().getByteBufUtil().getBytes(getBuffer());
                default:
                    return new byte[0];
            }
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
                    PacketEvents.get().getByteBufUtil().setBytes(getBuffer(), data);
                    break;
            }

        } else {
            this.data = data;
        }
    }

    private Object getBuffer() {
        Object dataSerializer = readObject(0, NMSUtils.packetDataSerializerClass);
        WrappedPacket dataSerializerWrapper = new WrappedPacket(new NMSPacket(dataSerializer));
        return dataSerializerWrapper.readObject(0, NMSUtils.byteBufClass);
    }

    public void retain() {
        if (packet != null) {
            if (constructorMode != 0) {
                PacketEvents.get().getByteBufUtil().retain(getBuffer());
            }
        }
    }

    public void release() {
        if (packet != null) {
            if (constructorMode != 0) {
                PacketEvents.get().getByteBufUtil().release(getBuffer());
            }
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        byte[] data = getData();
        Object dataSerializer;
        switch (constructorMode) {
            case 0:
                return constructor.newInstance(getChannelName(), data);
            case 1:
                dataSerializer = packetDataSerializerConstructor.newInstance(PacketEvents.get().getByteBufUtil().newByteBuf(data));
                return constructor.newInstance(getChannelName(), dataSerializer);
            case 2:
                Object minecraftKey = NMSUtils.generateMinecraftKeyNew(getChannelName());
                dataSerializer = packetDataSerializerConstructor.newInstance(PacketEvents.get().getByteBufUtil().newByteBuf(data));
                return constructor.newInstance(minecraftKey, dataSerializer);
            default:
                return null;
        }
    }
}
