package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PlayerInjectEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import org.bukkit.plugin.java.JavaPlugin;

public class MainExample extends JavaPlugin implements PacketListener {

    @Override
    public void onLoad() {
        PacketEvents.load();
    }

    @Override
    public void onEnable() {
        PacketEvents.start(this);
        //If packetevents cannot detect your server version, it will use the default you specify version
        // getAPI().getSettings().setDefaultServerVersion(ServerVersion.v_1_7_10);

        //Make sure you set a unique identifier, if another present plugin has the same identifier, you could run into issues
        PacketEvents.getSettings().setIdentifier("official_api");

        PacketEvents.getAPI().getEventManager().registerListeners(this);
    }

    @Override
    public void onDisable() {
        PacketEvents.stop();
    }

    @PacketHandler
    public void onInject(PlayerInjectEvent e) {
        e.getPlayer().sendMessage("you have been injected");
        System.out.println("he has been injected");
    }

    @PacketHandler
    public void onKeepAlive(PacketReceiveEvent e) {
        if(e.getPacketId() == PacketType.Client.KEEP_ALIVE) {
            e.getPlayer().sendMessage("you sent keep alive");
        }
    }
}