package me.purplex.packetevents.example;

import me.purplex.packetevents.events.PacketHandler;
import me.purplex.packetevents.events.PacketListener;
import me.purplex.packetevents.events.PacketReceiveEvent;
import me.purplex.packetevents.events.PacketSendEvent;

public class TestExample implements PacketListener {
    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {

    }

    @PacketHandler
    public void onPacketSend(PacketSendEvent e) {

    }
}
