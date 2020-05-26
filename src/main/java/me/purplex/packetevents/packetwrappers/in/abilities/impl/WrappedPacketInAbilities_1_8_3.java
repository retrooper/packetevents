package me.purplex.packetevents.packetwrappers.in.abilities.impl;

import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_8_R2.PacketPlayInAbilities;

import java.lang.reflect.Field;

public class WrappedPacketInAbilities_1_8_3 extends WrappedVersionPacket {

    public boolean a;
    public boolean b;
    public boolean c;
    public boolean d;
    public float e;
    public float f;

    public WrappedPacketInAbilities_1_8_3(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayInAbilities p = (PacketPlayInAbilities) packet;
        this.a = p.a();
        this.b = p.isFlying();
        this.c = p.c();
        this.d = p.d();
    }

    private static Field fieldE;
    private static Field fieldF;

    static {
        try {
            fieldE = PacketPlayInAbilities.class.getDeclaredField("e");
            fieldE.setAccessible(true);
            fieldF = PacketPlayInAbilities.class.getDeclaredField("f");
            fieldF.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
