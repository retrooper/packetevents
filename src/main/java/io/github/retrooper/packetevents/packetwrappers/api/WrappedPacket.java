package io.github.retrooper.packetevents.packetwrappers.api;

import io.github.retrooper.packetevents.enums.ServerVersion;
import org.bukkit.entity.Player;

public class WrappedPacket {
    private final Player player;
    protected Object packet;
    protected static ServerVersion version = ServerVersion.getVersion();
    private static final String err = "Your Version is unsupported, " +
            "please contact explored " +
            "through his discord server (http://discord.gg/2uZY5A4) " +
            "and tell him what version your server is running on! " +
            "Make sure you are using spigot!";

    public WrappedPacket(final Object packet) {
        this.player = null;
        if(packet == null)return;
        this.packet = packet;
        try {
            setup();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public WrappedPacket(final Player player, final Object packet) {
        this.player = player;
        if(packet == null)return;
        this.packet = packet;
        try {
            setup();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    protected void setup() throws Exception {

    }

    protected void throwUnsupportedVersion() {
        throw new IllegalStateException(err);
    }

    public final Player getPlayer() {
        return player;
    }

}
