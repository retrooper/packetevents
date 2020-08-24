package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.EventSynchronization;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.out.entity.WrappedPacketOutEntity;
import org.bukkit.entity.Entity;
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
        //If PacketEvents cannot detect your server version, it will use the default you specify version
        // getAPI().getSettings().setDefaultServerVersion(ServerVersion.v_1_7_10);


        PacketEvents.getAPI().getEventManager().registerListeners(this);
    }

    @Override
    public void onDisable() {
        PacketEvents.stop();
    }

    @PacketHandler(synchronization = EventSynchronization.NORMAL)
    public void onReceive(PacketSendEvent e) {
        if(PacketType.Server.Util.isInstanceOfEntity(e.getPacketId())) {
            WrappedPacketOutEntity entity = new WrappedPacketOutEntity(e.getNMSPacket());
            double deltaX = entity.getDeltaX();
            double deltaY = entity.getDeltaY();
            double deltaZ = entity.getDeltaZ();
            Entity ent = entity.getEntity();
            e.getPlayer().sendMessage("DX: " + deltaX + ", DY: " + deltaY + ", DZ: " + deltaZ);
        }
    }
}