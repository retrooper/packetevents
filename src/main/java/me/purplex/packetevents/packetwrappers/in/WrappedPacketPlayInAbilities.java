package me.purplex.packetevents.packetwrappers.in;


import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.packetwrappers.api.WrappedPacket;
import me.purplex.packetevents.packetwrappers.in._1_10.WrappedPacketPlayInAbilities_1_10;
import me.purplex.packetevents.packetwrappers.in._1_11.WrappedPacketPlayInAbilities_1_11;
import me.purplex.packetevents.packetwrappers.in._1_12.WrappedPacketPlayInAbilities_1_12;
import me.purplex.packetevents.packetwrappers.in._1_13.WrappedPacketPlayInAbilities_1_13;
import me.purplex.packetevents.packetwrappers.in._1_13_2.WrappedPacketPlayInAbilities_1_13_2;
import me.purplex.packetevents.packetwrappers.in._1_14.WrappedPacketPlayInAbilities_1_14;
import me.purplex.packetevents.packetwrappers.in._1_15.WrappedPacketPlayInAbilities_1_15;
import me.purplex.packetevents.packetwrappers.in._1_7_10.WrappedPacketPlayInAbilities_1_7_10;
import me.purplex.packetevents.packetwrappers.in._1_8.WrappedPacketPlayInAbilities_1_8;
import me.purplex.packetevents.packetwrappers.in._1_8_3.WrappedPacketPlayInAbilities_1_8_3;
import me.purplex.packetevents.packetwrappers.in._1_8_8.WrappedPacketPlayInAbilities_1_8_8;
import me.purplex.packetevents.packetwrappers.in._1_9.WrappedPacketPlayInAbilities_1_9;
import me.purplex.packetevents.packetwrappers.in._1_9_4.WrappedPacketPlayInAbilities_1_9_4;

public class WrappedPacketPlayInAbilities extends WrappedPacket {

    public boolean a;
    public boolean b;
    public boolean c;
    public boolean d;
    public float e;
    public float f;

    public WrappedPacketPlayInAbilities(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        if(version == ServerVersion.v_1_7_10) {
            WrappedPacketPlayInAbilities_1_7_10 p = new WrappedPacketPlayInAbilities_1_7_10(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.d = p.d;
            this.e = p.e;
        }
        else if (version == ServerVersion.v_1_8) {
            WrappedPacketPlayInAbilities_1_8 p = new WrappedPacketPlayInAbilities_1_8(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_8_3) {
            WrappedPacketPlayInAbilities_1_8_3 p = new WrappedPacketPlayInAbilities_1_8_3(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_8_8) {
            WrappedPacketPlayInAbilities_1_8_8 p = new WrappedPacketPlayInAbilities_1_8_8(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_9) {
            WrappedPacketPlayInAbilities_1_9 p = new WrappedPacketPlayInAbilities_1_9(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_9_4) {
            WrappedPacketPlayInAbilities_1_9_4 p = new WrappedPacketPlayInAbilities_1_9_4(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_10) {
            WrappedPacketPlayInAbilities_1_10 p = new WrappedPacketPlayInAbilities_1_10(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_11) {
            WrappedPacketPlayInAbilities_1_11 p = new WrappedPacketPlayInAbilities_1_11(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_12) {
            WrappedPacketPlayInAbilities_1_12 p = new WrappedPacketPlayInAbilities_1_12(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_13) {
            WrappedPacketPlayInAbilities_1_13 p = new WrappedPacketPlayInAbilities_1_13(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_13_2) {
            WrappedPacketPlayInAbilities_1_13_2 p = new WrappedPacketPlayInAbilities_1_13_2(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_14) {
            WrappedPacketPlayInAbilities_1_14 p = new WrappedPacketPlayInAbilities_1_14(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_15) {
            WrappedPacketPlayInAbilities_1_15 p = new WrappedPacketPlayInAbilities_1_15(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        }
    }

}
