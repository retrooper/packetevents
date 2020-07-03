package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import org.bukkit.plugin.java.JavaPlugin;

import static io.github.retrooper.packetevents.PacketEvents.getAPI;

public class MainExample extends JavaPlugin implements PacketListener {

    @Override
    public void onEnable() {
        PacketEvents.start(this);


        getAPI().getServerTickTask().cancel();

        getAPI().getEventManager().registerListener(this);
    }

    @Override
    public void onDisable() {
        PacketEvents.stop();
    }

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        if(e.getPacketId() == PacketType.Client.ARM_ANIMATION) {
            e.getPlayer().sendMessage("you swung your arm");
        }
    }
}