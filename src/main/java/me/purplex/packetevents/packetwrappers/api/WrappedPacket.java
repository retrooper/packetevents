package me.purplex.packetevents.packetwrappers.api;

import me.purplex.packetevents.enums.ServerVersion;

public class WrappedPacket {
    protected Object packet;
    protected static ServerVersion version = ServerVersion.getVersion();
    private static final String err =  "Your Version is unsupported, " +
            "please contact purplex(creator) " +
            "through his discord server (http://discord.gg/2uZY5A4) " +
            "and tell him what version your server is running on! " +
            "Make sure you are using spigot!";
    public WrappedPacket(Object packet) {
        this.packet = packet;
        try {
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void setup() throws Exception {

    }

    protected void throwUnsupportedVersion() {
        throw new IllegalStateException(err);
    }

}
