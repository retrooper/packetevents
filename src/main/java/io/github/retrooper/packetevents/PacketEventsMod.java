package io.github.retrooper.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.fabric.FabricPacketEventsBuilder;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketEventsMod implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("packetevents");

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        //TODO Mod idea, every 30 seconds or so, on a new port, establish a new connection to the server, get a server list response and
        //that is your ping
        PacketEvents.setAPI(FabricPacketEventsBuilder.build("packetevents"));
        PacketEvents.getAPI().getSettings().debug(true).bStats(true);
        PacketEvents.getAPI().load();
        PacketEvents.getAPI().init();
    }
}
