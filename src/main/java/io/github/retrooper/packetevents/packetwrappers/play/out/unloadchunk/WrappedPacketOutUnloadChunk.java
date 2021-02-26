package io.github.retrooper.packetevents.packetwrappers.play.out.unloadchunk;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

public final class WrappedPacketOutUnloadChunk extends WrappedPacket {
    public WrappedPacketOutUnloadChunk(NMSPacket packet) {
        super(packet);
    }

    public int getChunkX() {
        return readInt(0);
    }

    public int getChunkZ() {
        return readInt(1);
    }

    public void setChunkX(int x) {
        writeInt(0, x);
    }

    public void setChunkZ(int z) {
        writeInt(1, z);
    }

    @Override
    public boolean isSupported() {
        return version.isNewerThan(ServerVersion.v_1_8_8);
    }
}
