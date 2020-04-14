package me.retrooper.packetevents.example;

import me.retrooper.packetevents.events.*;
import me.retrooper.packetevents.packets.Packet;

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
