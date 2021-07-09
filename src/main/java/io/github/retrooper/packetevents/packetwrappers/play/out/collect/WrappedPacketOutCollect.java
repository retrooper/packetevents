package io.github.retrooper.packetevents.packetwrappers.play.out.collect;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;

import java.lang.reflect.Constructor;

public class WrappedPacketOutCollect extends WrappedPacket implements SendableWrapper {

    private int collectedEntityId, collectorEntityId, itemCount;
    private static Constructor<?> packetConstructor;

    public WrappedPacketOutCollect(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutCollect(final int collectedEntityId, final int collectorEntityId, final int itemCount) {
        this.collectedEntityId = collectedEntityId;
        this.collectorEntityId = collectorEntityId;
        this.itemCount = itemCount;
    }

    @Override
    protected void load() {
        try {
            packetConstructor = PacketTypeClasses.Play.Server.COLLECT.getConstructors()[1];
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

    public int getItemCount() {
        if (packet != null) {
            return readInt(2);
        } else {
            return this.itemCount;
        }
    }

    public void setItemCount(int count) {
        if (packet != null) {
            writeInt(2, count);
        } else {
            this.itemCount = count;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        /*
         * The reasoning of the checking for constructor parameter count is that on newer versions
         * the packet has an extra field and an extra parameter which is the picked up item count.
         *
         * As we want to support both of these we can just check for the amount
         * of parameters on the constructor and support every version with ease.
         */
        return packetConstructor.getParameterCount() == 3
                ? packetConstructor.newInstance(this.collectedEntityId, this.collectorEntityId, this.itemCount)
                : packetConstructor.newInstance(this.collectedEntityId, this.collectorEntityId);
    }
}
