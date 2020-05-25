package me.purplex.packetevents.packetwrappers.in._1_7_10;

import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_7_R4.PacketPlayInChat;

public class WrappedPacketPlayInChat_1_7_10 extends WrappedVersionPacket {
    public String message;
    public WrappedPacketPlayInChat_1_7_10(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayInChat p = (PacketPlayInChat)packet;
        this.message = p.c();
    }
}
