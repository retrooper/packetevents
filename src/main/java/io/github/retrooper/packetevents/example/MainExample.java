package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class MainExample extends JavaPlugin {

    @Override
    public void onLoad() {
        PacketEvents.load();
    }

    @Override
    public void onEnable() {
        PacketEvents.start(this);
        //If packeteventa cannot detect your server version, it will use the recommended version
        // getAPI().getSettings().setDefaultServerVersion(ServerVersion.v_1_7_10);
    }

    @Override
    public void onDisable() {
        PacketEvents.stop();
    }
}