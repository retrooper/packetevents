package io.github.retrooper.packetevents.packetwrappers.play.in.teleportaccept;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInTeleportAccept extends WrappedPacket {
    public WrappedPacketInTeleportAccept(NMSPacket packet) {
        super(packet);
    }

    public int getTeleportId() {
        return readInt(0);
    }
}
