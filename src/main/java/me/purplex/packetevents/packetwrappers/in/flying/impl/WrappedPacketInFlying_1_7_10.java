package me.purplex.packetevents.packetwrappers.in.flying.impl;

import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;

public class WrappedPacketInFlying_1_7_10 extends WrappedVersionPacket {
    public WrappedPacketInFlying_1_7_10(Object packet) {
        super(packet);
    }

    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public boolean f;
    public boolean hasPos;
    public boolean hasLook;


    @Override
    protected void setup() {
        PacketPlayInFlying p = (PacketPlayInFlying)packet;
        this.x = p.c();
        this.y = p.d();
        this.z = p.e();
        this.yaw = p.g();
        this.pitch = p.h();
        this.f = p.i();
    }

    public static class WrappedPacketInPosition_1_7_10 extends WrappedPacketInFlying_1_7_10 {

        public WrappedPacketInPosition_1_7_10(Object packet) {
            super(packet);
            this.hasPos = true;
        }
    }

    public static class WrappedPacketInPosition_Look_1_7_10 extends WrappedPacketInFlying_1_7_10 {

        public WrappedPacketInPosition_Look_1_7_10(Object packet) {
            super(packet);
            this.hasLook = true;
        }
    }


}
