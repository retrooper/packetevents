package io.github.retrooper.packetevents.packetwrappers.in.custompayload;

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
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
            try {
                this.minecraftKey = Reflection.getField(packetClass, nmsMinecraftKey, 0).get(packet);
                this.dataSerializer = Reflection.getField(packetClass, nmsPacketDataSerializer, 0).get(packet);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

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

    public String getData() {
        return data;
    }

    public Object getMinecraftKey() {
        return minecraftKey;
    }

    @Nullable
    public Object getDataSerializer() {
        return dataSerializer;
    }
}
