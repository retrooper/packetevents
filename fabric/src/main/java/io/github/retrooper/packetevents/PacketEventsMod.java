package io.github.retrooper.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.fabric.FabricPacketEventsBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketEventsMod implements PreLaunchEntrypoint, ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("packetevents");

    @Override
    public void onPreLaunch() {
        PacketEvents.setAPI(FabricPacketEventsBuilder.build("packetevents"));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onInitialize() {
        PacketEvents.getAPI().init();
    }
}
