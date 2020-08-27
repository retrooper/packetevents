package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MainExample extends JavaPlugin implements PacketListener {

    @Override
    public void onLoad() {
        PacketEvents.load();
    }

    @Override
    public void onEnable() {
        //Make sure you set a unique identifier, if another present plugin has the same identifier, you could run into issues
        PacketEvents.getSettings().setIdentifier("official_api");

        PacketEvents.start(this);
        // If PacketEvents cannot detect your server version, it will use the default you specify version
        // PacketEvents.getSettings().setDefaultServerVersion(ServerVersion.v_1_7_10);              Like Above.


       // PacketEvents.getAPI().getEventManager().registerListeners(this);

    }

    @Override
    public void onDisable() {
        PacketEvents.stop();
    }


    //TODO make PacketEvent class abstract and make an abstract method called isAsync()

}
