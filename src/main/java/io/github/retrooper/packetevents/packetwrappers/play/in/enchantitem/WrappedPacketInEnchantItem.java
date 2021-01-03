package io.github.retrooper.packetevents.packetwrappers.play.in.enchantitem;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInEnchantItem extends WrappedPacket {
    public WrappedPacketInEnchantItem(NMSPacket packet) {
        super(packet);
    }

    public int getWindowId() {
        return readInt(0);
    }

    public int getButton() {
        return readInt(1);
    }
}
