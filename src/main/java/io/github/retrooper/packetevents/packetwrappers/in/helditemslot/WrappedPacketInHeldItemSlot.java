package io.github.retrooper.packetevents.packetwrappers.in.helditemslot;

import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;

public final class WrappedPacketInHeldItemSlot extends WrappedPacket {
    private static Class<?> packetClass;

    static {
        try {
            packetClass = NMSUtils.getNMSClass("PacketPlayInHeldItemSlot");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int itemInHandIndex;

    public WrappedPacketInHeldItemSlot(Object packet) {
        super(packet);
    }

    @Override
    public void setup() {
        try {
            itemInHandIndex = Reflection.getField(packetClass, int.class, 0).getInt(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getItemInHandIndex() {
        return itemInHandIndex;
    }

}
