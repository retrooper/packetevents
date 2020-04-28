package me.purplex.packetevents.example;

import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.events.packetevent.ServerTickEvent;
import me.purplex.packetevents.events.handler.PacketHandler;
import me.purplex.packetevents.events.listener.PacketListener;
import me.purplex.packetevents.events.packetevent.*;
import me.purplex.packetevents.events.packetevent.packet.impl.in.WrappedPacketPlayInUseEntity;
import me.purplex.packetevents.packets.Packet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TestExample implements PacketListener {
    /**
     * How to register a packet listener?
     * <p>
     * PacketEvents.getPacketManager().registerPacketListener(new TestExample());
     */

    private int tick;

    private static ServerVersion serverVersion = ServerVersion.getVersion();

    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        //ONLY CLIENT PACKETS ALLOWED HERE!

        //System.out.println(e.getPacket().getClass().getSimpleName());
        if (e.getPacketName().equals(Packet.Client.USE_ENTITY)) {
            WrappedPacketPlayInUseEntity packet = new WrappedPacketPlayInUseEntity(e.getPacket());
            Entity entity = packet.getEntity();
            if (packet.getEntityUseAction() == EntityUseAction.ATTACK) {
                double dist = entity.getLocation().distanceSquared(p.getLocation());
                //p.sendMessage("dist: " + dist);
            }
        }

    }


    @PacketHandler
    public void onServerTick(ServerTickEvent e) {
        //CALLED EVERY TICK
        tick++;
        if (tick % 20 == 0) {
            /*for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage("One second has passed!");
            }*/
        }
    }

}
