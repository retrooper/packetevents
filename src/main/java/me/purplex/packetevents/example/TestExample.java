package me.purplex.packetevents.example;

import me.purplex.packetevents.bukkitevent.ServerTickEvent;
import me.purplex.packetevents.events.PacketHandler;
import me.purplex.packetevents.events.PacketListener;
import me.purplex.packetevents.events.PacketReceiveEvent;
import me.purplex.packetevents.events.PacketSendEvent;
import me.purplex.packetevents.packets.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TestExample implements PacketListener {
    /**
     * How to register a packet listener?
     *
     *  PacketEvents.getPacketManager().registerPacketListener(new TestExample());
     */

    private int tick;


    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        //ONLY CLIENT PACKETS ALLOWED HERE!
        if(e.getPacketName().equals(Packet.Client.USE_ENTITY)) {
            e.getPlayer().sendMessage("You have attacked an entity!");
        }
        else if(e.getPacketName().equals(Packet.Client.ARM_ANIMATION)) {
            e.getPlayer().sendMessage("You swung your arm.");
        }
        else if(e.getPacketName().equals(Packet.Client.POSITION)) {
            e.getPlayer().sendMessage("You updated your position!");
        }
    }

    @PacketHandler
    public void onPacketSend(PacketSendEvent e) {
        //ONLY SERVER PACKETS ALLOWED HERE!
        if(e.getPacketName().equals(Packet.Server.ENTITY_VELOCITY)) {
            e.getPlayer().sendMessage("You took velocity!");
        }
    }

    @PacketHandler
    public void onServerTick(ServerTickEvent e) {
        //CALLED EVERY TICK
        tick++;
        if(tick % 20 == 0) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage("One second has passed!");
            }
        }
    }
}
