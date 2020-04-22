package me.purplex.packetevents.example;

import me.purplex.packetevents.PacketEvents;
import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.events.packetevent.ServerTickEvent;
import me.purplex.packetevents.events.handler.PacketHandler;
import me.purplex.packetevents.events.listener.PacketListener;
import me.purplex.packetevents.events.packetevent.PacketReceiveEvent;
import me.purplex.packetevents.events.packetevent.PacketSendEvent;
import me.purplex.packetevents.packets.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TestExample implements PacketListener {
    /**
     * How to register a packet listener?
     * <p>
     * PacketEvents.getPacketManager().registerPacketListener(new TestExample());
     */

    private int tick;


    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        //ONLY CLIENT PACKETS ALLOWED HERE!
        if (e.getPacketName().equals(Packet.Client.USE_ENTITY)) {
            Entity entity = e.getInteractedEntity();
            EntityUseAction entityUseAction = e.getEntityUseAction();
            e.getPlayer().sendMessage("Dist: " + entity.getLocation().distanceSquared(e.getPlayer().getLocation()));
        } else if (e.getPacketName().equals(Packet.Client.ARM_ANIMATION)) {
            long timestam = e.getTimestamp();
        } else if (e.getPacketName().equals(Packet.Client.BLOCK_DIG)) {
            PlayerDigType type = e.getPlayerDigType();
        } else if (e.getPacketName().equals(Packet.Client.CHAT)) {
            String message = e.getChatPacketMessage();
            System.out.println("got: " + message);
            if(message.equals("pls jump")) {
                //shoot them in the air
                PacketEvents.sendVelocity(e.getPlayer(), 0.0, 50.0, 0.0);
            }
        }
        else if(e.getPacketName().equals(Packet.Client.POSITION)) {
            double motX = e.getMotionX();
            double motY = e.getMotionY();
            double motZ= e.getMotionZ();
        }
    }

    @PacketHandler
    public void onPacketSend(PacketSendEvent e) {
        //ONLY SERVER PACKETS ALLOWED HERE!
        if (e.getPacketName().equals(Packet.Server.ENTITY_VELOCITY)) {
            int ping = e.getPing();
            Entity entity = e.getVelocityEntity();
            double velX = e.getVelocityX();
            double velY = e.getVelocityY();
            double velZ = e.getVelocityZ();
        }
    }

    @PacketHandler
    public void onServerTick(ServerTickEvent e) {
        //CALLED EVERY TICK
        tick++;
        if (tick % 20 == 0) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage("One second has passed!");
            }
        }
    }
}
