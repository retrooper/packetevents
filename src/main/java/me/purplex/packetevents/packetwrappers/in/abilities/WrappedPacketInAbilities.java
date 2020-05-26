package me.purplex.packetevents.packetwrappers.in.abilities;


import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.packetwrappers.api.WrappedPacket;
import me.purplex.packetevents.packetwrappers.in.abilities.impl.*;

public class WrappedPacketInAbilities extends WrappedPacket {

    public boolean a;
    public boolean b;
    public boolean c;
    public boolean d;
    public float e;
    public float f;

    public WrappedPacketInAbilities(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        if (version == ServerVersion.v_1_7_10) {
            WrappedPacketInAbilities_1_7_10 p = new WrappedPacketInAbilities_1_7_10(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.d = p.d;
            this.e = p.e;
        } else if (version == ServerVersion.v_1_8) {
            WrappedPacketInAbilities_1_8 p = new WrappedPacketInAbilities_1_8(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_8_3) {
            WrappedPacketInAbilities_1_8_3 p = new WrappedPacketInAbilities_1_8_3(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_8_8) {
            WrappedPacketInAbilities_1_8_8 p = new WrappedPacketInAbilities_1_8_8(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_9) {
            WrappedPacketInAbilities_1_9 p = new WrappedPacketInAbilities_1_9(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_9_4) {
            WrappedPacketInAbilities_1_9_4 p = new WrappedPacketInAbilities_1_9_4(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_10) {
            WrappedPacketInAbilities_1_10 p = new WrappedPacketInAbilities_1_10(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_11) {
            WrappedPacketInAbilities_1_11 p = new WrappedPacketInAbilities_1_11(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_12) {
            WrappedPacketInAbilities_1_12 p = new WrappedPacketInAbilities_1_12(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_13) {
            WrappedPacketInAbilities_1_13 p = new WrappedPacketInAbilities_1_13(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_13_2) {
            WrappedPacketInAbilities_1_13_2 p = new WrappedPacketInAbilities_1_13_2(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_14) {
            WrappedPacketInAbilities_1_14 p = new WrappedPacketInAbilities_1_14(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else if (version == ServerVersion.v_1_15) {
            WrappedPacketInAbilities_1_15 p = new WrappedPacketInAbilities_1_15(packet);
            this.a = p.a;
            this.b = p.b;
            this.c = p.c;
            this.e = p.e;
            this.f = p.f;
        } else {
            throw throwUnsupportedVersion();
        }
    }

}
