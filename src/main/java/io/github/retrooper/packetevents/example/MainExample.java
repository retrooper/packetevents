package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class MainExample extends JavaPlugin {

    @Override
    public void onEnable() {
        PacketEvents.start(this);

        PacketEvents.getServerTickTask().cancel();

        //To fix bugs it is recommend to set this to TRUE
        PacketEvents.setShouldKickOnRestart(true);
    }

    @Override
    public void onDisable() {
        PacketEvents.stop(); //it kicks when you call the stop() function
    }
}
