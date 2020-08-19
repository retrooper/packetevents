package io.github.retrooper.packetevents.packetwrappers.out.entity;

import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.entity.Entity;

public class WrappedPacketOutEntity extends WrappedPacket {
    private static Class<?> packetClass;

    static {
        try {
            packetClass = NMSUtils.getNMSClass("PacketPlayOutEntity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int entityID;
    private double deltaX, deltaY, deltaZ;
    private byte yaw, pitch;
    private boolean onGround;


    public WrappedPacketOutEntity(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        try {
            this.entityID = Reflection.getField(packetClass, int.class, 0).getInt(packet);
            this.onGround = Reflection.getField(packetClass, boolean.class, 0).getBoolean(packet);
            if (version.isLowerThan(ServerVersion.v_1_9)) {
                byte dX = Reflection.getField(packetClass, byte.class, 0).getByte(packet);
                byte dY = Reflection.getField(packetClass, byte.class, 1).getByte(packet);
                byte dZ = Reflection.getField(packetClass, byte.class, 2).getByte(packet);

                this.deltaX = dX / 32.0;
                this.deltaY = dY / 32.0;
                this.deltaZ = dZ / 32.0;

                this.yaw = Reflection.getField(packetClass, byte.class, 3).getByte(packet);
                this.pitch = Reflection.getField(packetClass, byte.class, 4).getByte(packet);
            } else {
                short dX = Reflection.getField(packetClass, short.class, 0).getShort(packet);
                short dY = Reflection.getField(packetClass, short.class, 1).getShort(packet);
                short dZ = Reflection.getField(packetClass, short.class, 2).getShort(packet);

                this.deltaX = dX / 4096.0;
                this.deltaY = dY / 4096.0;
                this.deltaZ = dZ / 4096.0;

                this.yaw = Reflection.getField(packetClass, byte.class, 0).getByte(packet);
                this.pitch = Reflection.getField(packetClass, byte.class, 1).getByte(packet);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public byte getPitch() {
        return pitch;
    }

    public byte getYaw() {
        return yaw;
    }

    public double getDeltaZ() {
        return deltaZ;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public Entity getEntity() {
        return NMSUtils.getEntityById(entityID);
    }

    public int getEntityId() {
        return entityID;
    }

    public boolean isOnGround() {
        return onGround;
    }
}
