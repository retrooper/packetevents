package io.github.retrooper.packetevents.packetwrappers.play.out.entityheadrotation;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketOutEntityHeadRotation extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetDefaultConstructor;
    private int entityID;
    private byte yaw;

    public WrappedPacketOutEntityHeadRotation(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutEntityHeadRotation(int entityID, byte yaw) {
        this.entityID = entityID;
        this.yaw = yaw;
    }

    @Override
    protected void load() {
        try {
            packetDefaultConstructor = PacketTypeClasses.Play.Server.ENTITY_HEAD_ROTATION.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int getEntityId() {
        if (packet != null) {
            return readInt(0);
        }
        else {
            return entityID;
        }
    }

    public void setEntityId(int entityID) {
        if (packet != null) {
            writeInt(0, entityID);
        }
        else {
            this.entityID = entityID;
        }
    }

    public byte getYaw() {
        if (packet != null) {
            return readByte(0);
        }
        else {
            return yaw;
        }
    }

    public void setYaw(byte yaw) {
        if (packet != null) {
            writeByte(0, yaw);
        }
        else {
            this.yaw = yaw;
        }
    }

    @Override
    public Object asNMSPacket() {
        try {
            Object packetInstance = packetDefaultConstructor.newInstance();
            WrappedPacketOutEntityHeadRotation wrappedPacketOutEntityHeadRotation = new WrappedPacketOutEntityHeadRotation(new NMSPacket(packetInstance));
            wrappedPacketOutEntityHeadRotation.setYaw(getYaw());
            wrappedPacketOutEntityHeadRotation.setEntityId(getEntityId());
            return packetInstance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
