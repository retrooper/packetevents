package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.plugin.java.JavaPlugin;

import static io.github.retrooper.packetevents.PacketEvents.getAPI;

public class MainExample extends JavaPlugin {

    @Override
    public void onEnable() {
        PacketEvents.start(this);


        getAPI().getServerTickTask().cancel();
    }

    @Override
    public void onDisable() {
        PacketEvents.stop();
    }

}