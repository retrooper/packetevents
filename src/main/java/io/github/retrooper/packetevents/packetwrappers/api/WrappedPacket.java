package io.github.retrooper.packetevents.packetwrappers.api;

import io.github.retrooper.packetevents.enums.ServerVersion;
import org.bukkit.entity.Player;

public class WrappedPacket {
    private final Player player;
    protected Object packet;
    protected static ServerVersion version = ServerVersion.getVersion();

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

    protected void setup()  {

    }

    public final Player getPlayer() {
        return player;
    }

}
