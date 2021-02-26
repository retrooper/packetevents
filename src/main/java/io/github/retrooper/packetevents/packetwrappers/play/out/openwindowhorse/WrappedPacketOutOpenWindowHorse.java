package io.github.retrooper.packetevents.packetwrappers.play.out.openwindowhorse;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

public final class WrappedPacketOutOpenWindowHorse extends WrappedPacket {
    public WrappedPacketOutOpenWindowHorse(NMSPacket packet) {
        super(packet);
    }

    public int getWindowId() {
        return readInt(0);
    }

    public int getSlotCount() {
        return readInt(1);
    }

    public int getEntityId() {
        return readInt(2);
    }

    public void setWindowId(int windowId) {
        writeInt(0, windowId);
    }

    public void setSlotCount(int slotCount) {
        writeInt(1, slotCount);
    }

    public void setEntityId(int entityId) {
        writeInt(2, entityId);
    }

    @Override
    public boolean isSupported() {
        return version.isNewerThan(ServerVersion.v_1_15_2);
    }
}
