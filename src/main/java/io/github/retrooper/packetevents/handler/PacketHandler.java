package io.github.retrooper.packetevents.handler;


import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.enums.ServerVersion;
import org.bukkit.plugin.Plugin;

public class PacketHandler {
    private static final ServerVersion version = PacketEvents.getServerVersion();
    private final Plugin plugin;

    public PacketHandler(final Plugin plugin) {
        this.plugin = plugin;
    }
    public void initTinyProtocol() {
        if(version.isLowerThan(ServerVersion.v_1_8)) {
            new PacketHandler_1_7(getPlugin()).initTinyProtocol();
        }
        else {
            new PacketHandler_1_8(getPlugin()).initTinyProtocol();
        }
    }

    public final Plugin getPlugin() {
        return plugin;
    }

}