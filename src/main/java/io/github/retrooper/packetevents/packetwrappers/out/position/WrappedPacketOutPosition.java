package io.github.retrooper.packetevents.packetwrappers.out.position;

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;

public final class WrappedPacketOutPosition extends WrappedPacket {
    private static Class<?> packetClass;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public WrappedPacketOutPosition(Object packet) {
        super(packet);
    }

    public static void load() {
        packetClass = PacketTypeClasses.Server.POSITION;
    }

    @Override
    protected void setup() {
        //TODO boolean onGround and flags
        try {
            this.x = Reflection.getField(packetClass, double.class, 0).getDouble(packet);
            this.y = Reflection.getField(packetClass, double.class, 1).getDouble(packet);
            this.z = Reflection.getField(packetClass, double.class, 2).getDouble(packet);

            this.yaw = Reflection.getField(packetClass, float.class, 0).getFloat(packet);
            this.pitch = Reflection.getField(packetClass, float.class, 1).getFloat(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
