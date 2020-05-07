package me.purplex.packetevents.packetwrappers.in._1_9_4;

import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_9_R2.PacketPlayInFlying;

import java.lang.reflect.Field;

public class WrappedPacketPlayInFlying_1_9_4 extends WrappedVersionPacket {
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public boolean f;
    public boolean hasPos;
    public boolean hasLook;

    public WrappedPacketPlayInFlying_1_9_4(Object packet) {
        super(packet);
    }


    @Override
    protected void setup() {
        PacketPlayInFlying p = (PacketPlayInFlying) packet;
        try {
            this.hasPos = fieldHasPos.getBoolean(p);
            this.hasLook = fieldHasLook.getBoolean(p);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (hasPos) {
            this.x = p.a(0.0D);
            this.y = p.b(0.0D);
            this.z = p.c(0.0D);
        } else {
            try {
                this.x = fieldX.getDouble(p);
                this.y = fieldY.getDouble(p);
                this.z = fieldZ.getDouble(p);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (hasLook) {
            this.yaw = p.a(0.0F);
            this.pitch = p.b(0.0F);
        } else {
            try {
                this.yaw = fieldYaw.getFloat(p);
                this.pitch = fieldPitch.getFloat(p);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        this.f = p.a();
    }

    public static class WrappedPacketPlayInPosition_1_9_4 extends WrappedPacketPlayInFlying_1_9_4 {

        public WrappedPacketPlayInPosition_1_9_4(Object packet) {
            super(packet);
            this.hasPos = true;
        }
    }

    public static class WrappedPacketPlayInPosition_Look_1_9_4 extends WrappedPacketPlayInFlying_1_9_4 {

        public WrappedPacketPlayInPosition_Look_1_9_4(Object packet) {
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
            fieldHasPos.setAccessible(true);
            fieldHasLook = PacketPlayInFlying.class.getDeclaredField("hasLook");
            fieldHasLook.setAccessible(true);
            fieldX = PacketPlayInFlying.class.getDeclaredField("x");
            fieldX.setAccessible(true);
            fieldY = PacketPlayInFlying.class.getDeclaredField("y");
            fieldY.setAccessible(true);
            fieldZ = PacketPlayInFlying.class.getDeclaredField("z");
            fieldZ.setAccessible(true);
            fieldYaw = PacketPlayInFlying.class.getDeclaredField("yaw");
            fieldYaw.setAccessible(true);
            fieldPitch = PacketPlayInFlying.class.getDeclaredField("pitch");
            fieldPitch.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


}