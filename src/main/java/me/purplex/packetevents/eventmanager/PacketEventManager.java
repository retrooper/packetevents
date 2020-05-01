package me.purplex.packetevents.eventmanager;

import me.purplex.packetevents.events.PacketEvent;
import me.purplex.packetevents.events.packetevent.ServerTickEvent;
import me.purplex.packetevents.events.listener.PacketListener;
import me.purplex.packetevents.events.packetevent.PacketReceiveEvent;
import me.purplex.packetevents.events.packetevent.PacketSendEvent;

import java.util.LinkedList;

public interface PacketEventManager {
    LinkedList<PacketListener> packetListeners = new LinkedList<>();
    void callEvent(PacketEvent e);

    void registerListener(PacketListener e);
    void unregisterListener(PacketListener e);

    void unregisterAllListeners(PacketListener e);



}
