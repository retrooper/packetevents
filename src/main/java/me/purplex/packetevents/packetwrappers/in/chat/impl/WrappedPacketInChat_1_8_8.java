package me.purplex.packetevents.packetwrappers.in.chat.impl;

import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_8_R3.PacketPlayInChat;

public class WrappedPacketInChat_1_8_8 extends WrappedVersionPacket {
    public String message;
    public WrappedPacketInChat_1_8_8(Object packet) {
        super(packet);
    }

    @Override
    protected void setup() {
        PacketPlayInChat p = (PacketPlayInChat)packet;
        this.message = p.a();
    }
}
