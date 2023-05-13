package io.github.retrooper.packetevents.factory.fabric;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class ServerInstanceManager {
    private static MinecraftServer serverInstance;

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> serverInstance = server);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> serverInstance = null);
    }
    public static MinecraftServer getServerInstance() {
        return serverInstance;
    }
}
