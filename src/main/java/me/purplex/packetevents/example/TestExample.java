package me.purplex.packetevents.example;

import me.purplex.packetevents.events.PacketHandler;
import me.purplex.packetevents.events.PacketListener;
import me.purplex.packetevents.events.PacketReceiveEvent;
import me.purplex.packetevents.events.PacketSendEvent;
import me.purplex.packetevents.packets.Packet;

public class TestExample implements PacketListener {
    /**
     * How to register a packet listener?
     *
     *  PacketEvents.getPacketManager().registerPacketListener(new TestExample());
     */


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
}
