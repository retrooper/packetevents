package me.purplex.packetevents.example;

import me.purplex.packetevents.PacketEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class MainExample extends JavaPlugin {
    @Override
    public void onEnable() {
        /**
         * Starts the servertickevent repeating task (if enabled)
         * and registers onJoin and onQuit events to inject and uninject players.
         *
         * First argument is the JavaPlugin instance to register these events and start the task with,
         * Second argument enables server tick event
         */
        PacketEvents.setup(this, true);

        PacketEvents.getPacketManager().registerListener(new TestExample());
    }

    @Override
    public void onDisable() {
        /**
         * Stops server tick event task if enabled
         */
        PacketEvents.cleanup(this);
    }

}
