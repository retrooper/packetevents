package me.retrooper.packetevents.example;

import me.retrooper.packetevents.events.PacketHandler;
import me.retrooper.packetevents.events.PacketListener;
import me.retrooper.packetevents.events.PacketReceiveEvent;
import me.retrooper.packetevents.events.PacketSendEvent;

public class TestExample implements PacketListener {
    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        System.out.println("RECEEIIVIVEEE");
    }

    @PacketHandler
    public void onPacketSend(PacketSendEvent e) {

        System.out.println("SEEEEEND");
    }
}
