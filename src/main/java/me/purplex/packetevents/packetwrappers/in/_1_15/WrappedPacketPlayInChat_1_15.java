package me.purplex.packetevents.packetwrappers.in._1_15;

import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_15_R1.PacketPlayInChat;

public class WrappedPacketPlayInChat_1_15 extends WrappedVersionPacket {

    public String message;
    public WrappedPacketPlayInChat_1_15(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayInChat p = (PacketPlayInChat)packet;
        this.message = p.b();
    }
}
