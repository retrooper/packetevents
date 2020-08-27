/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.packetwrappers.in.armanimation;

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;

@Deprecated
public final class WrappedPacketInArmAnimation extends WrappedPacket {
    private static Class<?> packetClass;

    public WrappedPacketInArmAnimation(Object packet) {
        super(packet);
    }

    public static void load() {
        packetClass = PacketTypeClasses.Client.ARM_ANIMATION;
    }

    @Override
    public void setup() {

    }

}
