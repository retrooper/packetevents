package io.github.retrooper.packetevents.packetwrappers.play.in.spectate;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

import java.util.UUID;

public class WrappedPacketInSpectate extends WrappedPacket {
    public WrappedPacketInSpectate(NMSPacket packet) {
        super(packet);
    }

    public UUID getUUID() {
        return (UUID) readObject(0, UUID.class);
    }
}
