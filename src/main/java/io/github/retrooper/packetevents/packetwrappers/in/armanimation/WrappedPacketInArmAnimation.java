package io.github.retrooper.packetevents.packetwrappers.in.armanimation;

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.NMSUtils;

@Deprecated
public final class WrappedPacketInArmAnimation extends WrappedPacket {
    private static Class<?> packetClass;

    public static void load() {
        packetClass = PacketTypeClasses.Client.ARM_ANIMATION;
    }


    public WrappedPacketInArmAnimation(Object packet) {
        super(packet);
    }

    @Override
    public void setup() {

    }

}
