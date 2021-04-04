package io.github.retrooper.packetevents.packetwrappers.play.out.entitydestroy;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author yanjulang
 * @since 1.8
 */
public class WrappedPacketOutEntityDestroy extends WrappedPacket implements SendableWrapper {

    private static Constructor<?> packetConstructor;
    private int[] entityIds;

    public WrappedPacketOutEntityDestroy(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutEntityDestroy(int... entityIds) {
        this.entityIds = entityIds;
    }

    @Override
    protected void load() {
        try {
            packetConstructor =
                    PacketTypeClasses.Play.Server.ENTITY_DESTROY.getConstructor(int[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int[] getEntityIds() {
        if (packet != null && entityIds == null) {
            return entityIds = readIntArray(0);
        } else {
            return entityIds;
        }
    }

    public void setEntityIds(int... entityIds) {
        this.entityIds = entityIds;
        if (packet != null) {
            writeIntArray(0, entityIds);
        }
    }

    @Override
    public Object asNMSPacket() {
        try {
            return packetConstructor.newInstance((Object) entityIds);
        } catch (InstantiationException | InvocationTargetException
                | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}