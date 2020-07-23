package io.github.retrooper.packetevents.packetwrappers.in.custompayload;

import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

public final class WrappedPacketInCustomPayload extends WrappedPacket {
    private static Class<?> packetClass, nmsMinecraftKey, nmsPacketDataSerializer;

    static {
        try {
            packetClass = NMSUtils.getNMSClass("PacketPlayInCustomPayload");
            nmsPacketDataSerializer = NMSUtils.getNMSClass("PacketDataSerializer");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            //Only on 1.13+
            nmsMinecraftKey = NMSUtils.getNMSClass("MinecraftKey");
        } catch (Exception e) {
            //Its okay, this means they are on versions 1.7.10 ~ 1.12.2
        }
    }

    private String data;
    private Object minecraftKey, dataSerializer;

    public WrappedPacketInCustomPayload(Object packet) {
        super(packet);
    }

    @Override
    public void setup() {
        if(Reflection.getField(packetClass, String.class, 0) == null) {
            try {
                this.minecraftKey = Reflection.getField(packetClass, nmsMinecraftKey, 0).get(packet);
                this.dataSerializer = Reflection.getField(packetClass, nmsPacketDataSerializer, 0).get(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.data = (String)Reflection.getField(packetClass, String.class, 0).get(packet);
                this.dataSerializer = Reflection.getField(packetClass, nmsPacketDataSerializer, 0).get(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getData() {
        return data;
    }

    public Object getDataSerializer() {
        return dataSerializer;
    }
}
