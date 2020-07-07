package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PlayerInjectEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MainExample extends JavaPlugin implements PacketListener {
    private final PacketEvents pe = new PacketEvents(this);
    @Override
    public void onLoad() {
        PacketEvents.load();
    }

    @Override
    public void onEnable() {
        pe.start();
        //If packetevents cannot detect your server version, it will use the recommended version
        // getAPI().getSettings().setDefaultServerVersion(ServerVersion.v_1_7_10);

        pe.getAPI().getEventManager().registerListeners(this);
    }

    @Override
    public void onDisable() {
        pe.stop();
    }

    @PacketHandler
    public void onInject(PlayerInjectEvent e) {
        e.getPlayer().sendMessage("you have been injected");
        System.out.println("he has been injected");
    }
}