/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.packetwrappers.in.helditemslot;

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;

public final class WrappedPacketInHeldItemSlot extends WrappedPacket {
    private static Class<?> packetClass;
    private int itemInHandIndex;

    public WrappedPacketInHeldItemSlot(Object packet) {
        super(packet);
    }

    public static void load() {
        packetClass = PacketTypeClasses.Client.HELD_ITEM_SLOT;
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
