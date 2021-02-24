package io.github.retrooper.packetevents.packetwrappers.play.in.itemname;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInItemName extends WrappedPacket {
    public WrappedPacketInItemName(NMSPacket packet) {
        super(packet);
    }

    public String getItemName() {
        return readString(0);
    }

    public void setItemName(String itemName) {
        writeString(0, itemName);
    }
}
