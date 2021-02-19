package io.github.retrooper.packetevents.packetwrappers.play.in.pickitem;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInPickItem extends WrappedPacket {
    public WrappedPacketInPickItem(NMSPacket packet) {
        super(packet);
    }

    public int getSlot() {
        return readInt(0);
    }
}
