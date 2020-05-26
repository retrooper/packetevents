package me.purplex.packetevents.packetwrappers.in.flying.impl;


import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_8_R1.PacketPlayInFlying;

public class WrappedPacketInFlying_1_8 extends WrappedVersionPacket {
    public WrappedPacketInFlying_1_8(Object packet) {
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
        PacketPlayInFlying p = (PacketPlayInFlying) packet;
        this.x = p.a();
        this.y = p.b();
        this.z = p.c();
        this.yaw = p.d();
        this.pitch = p.e();
        this.f = p.f();
        this.hasPos = p.g();
        this.hasLook = p.h();
    }


    public static class WrappedPacketInPosition_1_8 extends WrappedPacketInFlying_1_8 {

        public WrappedPacketInPosition_1_8(Object packet) {
            super(packet);
            this.hasPos = true;
        }
    }

    public static class WrappedPacketInPosition_Look_1_8 extends WrappedPacketInFlying_1_8 {

        public WrappedPacketInPosition_Look_1_8(Object packet) {
            super(packet);
            this.hasLook = true;
        }
    }


}
