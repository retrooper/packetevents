package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class MainExample extends JavaPlugin {

    @Override
    public void onEnable() {
        PacketEvents.start(this);
    }

    @Override
    public void onDisable() {
        PacketEvents.stop();
    }
}
