package io.github.retrooper.packetevents.packetwrappers.play.in.difficultylock;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

public final class WrappedPacketInDifficultyLock extends WrappedPacket {
    public WrappedPacketInDifficultyLock(NMSPacket packet) {
        super(packet);
    }

    public boolean isLocked() {
        return readBoolean(0);
    }

    public void setLocked(boolean locked) {
        writeBoolean(0, locked);
    }

    @Override
    public boolean isSupported() {
        return version.isNewerThan(ServerVersion.v_1_15_2);
    }
}
