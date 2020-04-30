package me.purplex.packetevents.packetwrappers.in._1_8_8;

import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

public class WrappedPacketPlayInFlying_1_8_8 {
    private final Object packet;
    public WrappedPacketPlayInFlying_1_8_8(Object packet) {
        this.packet = packet;
        setupFields();
    }

    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public boolean f;
    public boolean hasPos;
    public boolean hasLook;


    private void setupFields() {
        PacketPlayInFlying p = (PacketPlayInFlying)packet;
        this.x = p.a();
        this.y = p.b();
        this.z = p.c();
        this.yaw = p.d();
        this.pitch = p.e();
        this.f = p.f();
        this.hasPos = p.g();
        this.hasLook = p.h();
    }

    public static class WrappedPacketPlayInPosition_1_8_8 extends WrappedPacketPlayInFlying_1_8_8 {

        public WrappedPacketPlayInPosition_1_8_8(Object packet) {
            super(packet);
            this.hasPos = true;
        }
    }

    public static class WrappedPacketPlayInPosition_Look_1_8_8 extends WrappedPacketPlayInFlying_1_8_8 {

        public WrappedPacketPlayInPosition_Look_1_8_8(Object packet) {
            super(packet);
            this.hasLook = true;
        }
    }


}
