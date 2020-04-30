package me.purplex.packetevents.packetwrappers.in._1_13_2;

import net.minecraft.server.v1_13_R2.PacketPlayInFlying;

import java.lang.reflect.Field;

public class WrappedPacketPlayInFlying_1_13_2 {
    private final Object packet;

    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public boolean f;
    public boolean hasPos;
    public boolean hasLook;
    public WrappedPacketPlayInFlying_1_13_2(Object packet) {
        this.packet = packet;
        try {
            setupFields();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }



    private void setupFields() throws IllegalAccessException {
        PacketPlayInFlying p = (PacketPlayInFlying) packet;
        this.hasPos = fieldHasPos.getBoolean(p);
        this.hasLook = fieldHasLook.getBoolean(p);
        if (hasPos) {
            this.x = p.a(0.0D);
            this.y = p.b(0.0D);
            this.z = p.c(0.0D);
        } else {
            this.x = fieldX.getDouble(p);
            this.y = fieldY.getDouble(p);
            this.z = fieldZ.getDouble(p);
        }

        if (hasLook) {
            this.yaw = p.a(0.0F);
            this.pitch = p.b(0.0F);
        }
        else {
            this.yaw = fieldYaw.getFloat(p);
            this.pitch = fieldPitch.getFloat(p);
        }
        this.f = p.a();
    }

    public static class WrappedPacketPlayInPosition_1_13_2 extends WrappedPacketPlayInFlying_1_13_2 {

        public WrappedPacketPlayInPosition_1_13_2(Object packet) {
            super(packet);
            this.hasPos = true;
        }
    }

    public static class WrappedPacketPlayInPosition_Look_1_13_2 extends WrappedPacketPlayInFlying_1_13_2 {

        public WrappedPacketPlayInPosition_Look_1_13_2(Object packet) {
            super(packet);
            this.hasLook = true;
        }
    }

    private static Field fieldHasPos;
    private static Field fieldHasLook;
    private static Field fieldX;
    private static Field fieldY;
    private static Field fieldZ;
    private static Field fieldYaw;
    private static Field fieldPitch;

    static {
        try {
            fieldHasPos = PacketPlayInFlying.class.getDeclaredField("hasPos");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            fieldHasLook = PacketPlayInFlying.class.getDeclaredField("hasLook");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            fieldX = PacketPlayInFlying.class.getDeclaredField("x");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            fieldY = PacketPlayInFlying.class.getDeclaredField("y");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            fieldZ = PacketPlayInFlying.class.getDeclaredField("z");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            fieldYaw = PacketPlayInFlying.class.getDeclaredField("yaw");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            fieldPitch = PacketPlayInFlying.class.getDeclaredField("pitch");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


}

