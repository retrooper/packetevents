package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
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
    public void onPacketReceive(PacketEvent event) {
        if (event instanceof PacketReceiveEvent) {
            PacketReceiveEvent e = (PacketReceiveEvent) event;
            System.out.println(e.getPacketName() + " is NAME");
            if (PacketType.Util.isInstanceOfFlyingPacket(e.getNMSPacket())) {
                WrappedPacketInFlying flying = new WrappedPacketInFlying(e.getNMSPacket());
                System.out.println(flying.isOnGround() + " and " + flying.isPosition() + " and " + flying.isLook() + " and " + flying.getX() + flying.getZ() + flying.getYaw());
            } else if (PacketType.Util.isInstanceOfUseEntity(e.getNMSPacket())) {
                WrappedPacketInUseEntity useEntity = new WrappedPacketInUseEntity(e.getNMSPacket());
                System.out.println("a" + useEntity.getAction().name() + " and " + useEntity.getEntityId());
            }
        }

    }
}