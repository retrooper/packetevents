package io.github.retrooper.packetevents.packetwrappers.in.armanimation;

import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.utils.NMSUtils;

//TODO: Test on 1.9, 1.11, 1.13, 1.14
public final class WrappedPacketInArmAnimation extends WrappedPacket {
    private static Class<?> packetClass;

    static {
        try {
            packetClass = NMSUtils.getNMSClass("PacketPlayInArmAnimation");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long timeStamp;

    public WrappedPacketInArmAnimation(Object packet) {
        super(packet);
    }

    @Override
    public void setup() {
        //Lazy way, do you really need to get the timestamp in the actual class? Edit by Retrooper: Yes lol
        //TODO: Get EnumHand value sent in 1.9+, however not really necessary
        timeStamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timeStamp;
    }

}
