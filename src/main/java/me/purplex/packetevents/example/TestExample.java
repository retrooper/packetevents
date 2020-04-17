package me.purplex.packetevents.example;

import me.purplex.packetevents.events.PacketHandler;
import me.purplex.packetevents.events.PacketListener;
import me.purplex.packetevents.events.PacketReceiveEvent;
import me.purplex.packetevents.events.PacketSendEvent;
import me.purplex.packetevents.packets.Packet;
import me.retrooper.packetevents.events.*;

public class TestExample implements PacketListener {
    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        if (e.getPacketName().equals(Packet.Client.POSITION)) {
            e.getPlayer().sendMessage("You sent a position packet!");
        }
    }

    @PacketHandler
    public void onPacketSend(PacketSendEvent e) {

    }
}
