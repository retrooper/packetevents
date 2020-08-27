/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.event.PacketListener;
import org.bukkit.plugin.java.JavaPlugin;

public class MainExample extends JavaPlugin implements PacketListener {

    @Override
    public void onLoad() {
        PacketEvents.load();
    }

    @Override
    public void onEnable() {
        PacketEvents.getSettings().setIdentifier("packetevents_api");

        PacketEvents.getSettings().setDefaultServerVersion(ServerVersion.v_1_7_10);

        PacketEvents.start(this);
        //PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    @Override
    public void onDisable() {
        PacketEvents.stop();
    }
}