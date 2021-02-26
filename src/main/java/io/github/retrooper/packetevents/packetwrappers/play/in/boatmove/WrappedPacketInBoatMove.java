package io.github.retrooper.packetevents.packetwrappers.play.in.boatmove;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

public final class WrappedPacketInBoatMove extends WrappedPacket {
    public WrappedPacketInBoatMove(NMSPacket packet) {
        super(packet);
    }

    public boolean getLeftPaddle() {
        return readBoolean(0);
    }

    public boolean getRightPaddle() {
        return readBoolean(1);
    }

    public void setLeftPaddle(boolean turning) {
        writeBoolean(0, turning);
    }

    public void setRightPaddle(boolean turning) {
        writeBoolean(1, turning);
    }

    @Override
    public boolean isSupported() {
        return version.isNewerThan(ServerVersion.v_1_8);
    }
}
