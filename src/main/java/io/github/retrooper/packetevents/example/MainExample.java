package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.ClientVersion;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PlayerInjectEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.entityaction.WrappedPacketInEntityAction;
import org.bukkit.plugin.java.JavaPlugin;

public class MainExample extends JavaPlugin implements PacketListener {

    @Override
    public void onEnable() {
        PacketEvents.start(this);

        PacketEvents.getServerTickTask().cancel();

        PacketEvents.getEventManager().registerListener(this);

    }

    @Override
    public void onDisable() {
        PacketEvents.stop();
    }


    @PacketHandler
    public void onInject(PlayerInjectEvent e) {
        final ClientVersion version = e.getClientVersion();
        System.out.println("version with protocollib: " + version);
    }
}