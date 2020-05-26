package me.purplex.packetevents.packetwrappers.in.flying.impl;

import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_8_R2.PacketPlayInFlying;

public class WrappedPacketInFlying_1_8_3 extends WrappedVersionPacket {
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public boolean f;
    public boolean hasPos;
    public boolean hasLook;
    public WrappedPacketInFlying_1_8_3(Object packet) {
        super(packet);
    }


    @Override
    protected void setup() {
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

    public static class WrappedPacketInPosition_1_8_3 extends WrappedPacketInFlying_1_8_3 {

        public WrappedPacketInPosition_1_8_3(Object packet) {
            super(packet);
            this.hasPos = true;
        }
    }

    public static class WrappedPacketInPosition_Look_1_8_3 extends WrappedPacketInFlying_1_8_3 {

        public WrappedPacketInPosition_Look_1_8_3(Object packet) {
            super(packet);
            this.hasLook = true;
        }
    }


}