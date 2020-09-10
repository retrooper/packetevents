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

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.in.clientcommand.WrappedPacketInClientCommand;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

import java.lang.reflect.Field;

public final class WrappedPacketInCustomPayload extends WrappedPacket {
    private static Class<?> packetClass, nmsMinecraftKey, nmsPacketDataSerializer;

    private static boolean strPresentInIndex0;
    private String data;
    private Object minecraftKey, dataSerializer;
    public WrappedPacketInCustomPayload(Object packet) {
        super(packet);
    }

    public static void load() {
        packetClass = PacketTypeClasses.Client.CUSTOM_PAYLOAD;
        strPresentInIndex0 = Reflection.getField(packetClass, String.class, 0) != null;
        try {
            nmsPacketDataSerializer = NMSUtils.getNMSClass("PacketDataSerializer");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            //Only on 1.13+
            nmsMinecraftKey = NMSUtils.getNMSClass("MinecraftKey");
        } catch (ClassNotFoundException e) {
            //Its okay, this means they are on versions 1.7.10 ~ 1.12.2
        }
    }

    @Override
    public void setup() {
        if (!strPresentInIndex0) {
            this.minecraftKey = readObject(0, nmsMinecraftKey);
            this.dataSerializer = readObject(0, nmsPacketDataSerializer);

        } else {
            Field dataSerializerField = Reflection.getField(packetClass, nmsPacketDataSerializer, 0);
            try {
                this.data = (String) Reflection.getField(packetClass, String.class, 0).get(packet);
                if (dataSerializerField != null) {
                    this.dataSerializer = dataSerializerField.get(packet);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Get the data
     * @return String data
     */
    public String getData() {
        return data;
    }

    /**
     * Get the minecraft key
     * @return Minecraft Key object
     */
    public Object getMinecraftKey() {
        return minecraftKey;
    }


    /**
     * Get the data serializer
     * @return Data Serializer object
     */
    @Nullable
    public Object getDataSerializer() {
        return dataSerializer;
    }
}
