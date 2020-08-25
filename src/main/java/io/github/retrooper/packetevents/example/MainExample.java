package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.enums.EventSynchronization;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.in.windowclick.WrappedPacketInWindowClick;
import io.github.retrooper.packetevents.packetwrappers.out.entity.WrappedPacketOutEntity;
import io.github.retrooper.packetevents.packetwrappers.out.entityvelocity.WrappedPacketOutEntityVelocity;
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


    //TODO test FLYING, WINDOW_CLICK

    @PacketHandler
    public void onReceive(PacketReceiveEvent e) {
        if(PacketType.Client.Util.isInstanceOfFlying(e.getPacketId())) {
            WrappedPacketInFlying flying = new WrappedPacketInFlying(e.getNMSPacket());
            if(flying.isPosition() && !flying.isLook()) {
                WrappedPacketInFlying.WrappedPacketInPosition pos = new WrappedPacketInFlying.WrappedPacketInPosition(e.getNMSPacket());
                System.out.println("X: " + pos.getX() + ", Y: " + pos.getY() + ", Z: " + pos.getZ());
                System.out.println("isPos: "+  pos.isPosition() + ", isLook: " + pos.isLook());
            }
        }
        else if(e.getPacketId() == PacketType.Client.WINDOW_CLICK) {
            WrappedPacketInWindowClick windowClick = new WrappedPacketInWindowClick(e.getNMSPacket());
            e.getPlayer().sendMessage("Type: " + windowClick.getWindowClickType().name());
        }
    }

    @PacketHandler
    public void onSend(PacketSendEvent e) {
        
    }
}