package io.github.retrooper.packetevents.packetwrappers.play.out.collect;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Constructor;
import java.util.Optional;

public class WrappedPacketOutCollect extends WrappedPacket implements SendableWrapper {
    private static boolean v_1_11;
    private static Constructor<?> packetConstructor;
    private int collectedEntityId, collectorEntityId, itemCount;

    public WrappedPacketOutCollect(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutCollect(int collectedEntityId, int collectorEntityId, int itemCount) {
        this.collectedEntityId = collectedEntityId;
        this.collectorEntityId = collectorEntityId;
        this.itemCount = itemCount;
    }

    @Override
    protected void load() {
        v_1_11 = version.isNewerThanOrEquals(ServerVersion.v_1_11);
        try {
            if (v_1_11) {
                packetConstructor = PacketTypeClasses.Play.Server.COLLECT.getConstructor(int.class, int.class, int.class);
            } else {
                packetConstructor = PacketTypeClasses.Play.Server.COLLECT.getConstructor(int.class, int.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCollectedEntityId() {
        if (packet != null) {
            return readInt(0);
        } else {
            return this.collectedEntityId;
        }
    }

    public void setCollectedEntityId(int id) {
        if (packet != null) {
            writeInt(0, id);
        } else {
            this.collectedEntityId = id;
        }
    }

    public int getCollectorEntityId() {
        if (packet != null) {
            return readInt(1);
        } else {
            return this.collectorEntityId;
        }
    }

    public void setCollectorEntityId(int id) {
        if (packet != null) {
            writeInt(1, id);
        } else {
            this.collectorEntityId = id;
        }
    }

    public Optional<Integer> getItemCount() {
        if (!v_1_11) {
            return Optional.empty();
        }
        if (packet != null) {
            return Optional.of(readInt(2));
        } else {
            return Optional.of(this.itemCount);
        }
    }

    public void setItemCount(int count) {
        if (v_1_11) {
            if (packet != null) {
                writeInt(2, count);
            } else {
                this.itemCount = count;
            }
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        /*
         * On newer versions the packet has an extra field which is the picked up item count.
         */
        return v_1_11
                ? packetConstructor.newInstance(getCollectedEntityId(), getCollectorEntityId(), getItemCount())
                : packetConstructor.newInstance(getCollectedEntityId(), getCollectorEntityId());
    }
}
