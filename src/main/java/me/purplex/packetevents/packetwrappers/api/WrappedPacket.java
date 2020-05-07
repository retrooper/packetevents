package me.purplex.packetevents.packetwrappers.api;

import me.purplex.packetevents.enums.ServerVersion;

public class WrappedPacket {
    protected Object packet;
    protected static ServerVersion version;
    static {
        version = ServerVersion.getVersion();
    }
    public WrappedPacket(Object packet) {
        this.packet = packet;
        setup();
    }

   /* public WrappedPacket(String name, PacketData data) {
        String packetName = "PacketPlayOut" + name;
        
    }*/

    protected void setup() {

    }

    public Object getPacket() {
        return packet;
    }
}
