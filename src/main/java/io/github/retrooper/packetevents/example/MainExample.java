package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class MainExample extends JavaPlugin {
    @Override
    public void onEnable() {

        PacketEvents.start(this);

        //If you do not need the server tick event you can just cancel its task like this

        //PacketEvents.getServerTickTask().cancel();


        /**
         * Register TestExample's PacketListener
         */
        PacketEvents.getEventManager().registerListener(new RegisteredListener());
    }

    @Override
    public void onDisable() {
        /**
         * Stops server tick event task if enabled and unregisters packetlisteners
         */
        PacketEvents.stop();

    }

}
