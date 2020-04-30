package me.purplex.packetevents.packetwrappers.in._1_7_10;

import net.minecraft.server.v1_7_R4.PacketPlayInAbilities;


public class WrappedPacketPlayInAbilities_1_7_10 {
    private final Object packet;

    public WrappedPacketPlayInAbilities_1_7_10(Object packet) {
        this.packet = packet;
        setupFields();
    }


    private void setupFields() {
        PacketPlayInAbilities p = (PacketPlayInAbilities) packet;
        this.a = p.c();
        this.b = p.isFlying();
        this.c = p.e();
        this.d = p.f();
        this.e = p.g();
        this.f = p.h();
    }

    public boolean a;
    public boolean b;
    public boolean c;
    public boolean d;
    public float e;
    public float f;
}