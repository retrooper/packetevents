package me.purplex.packetevents.eventmanager;

import me.purplex.packetevents.events.PacketEvent;
import me.purplex.packetevents.events.packetevent.ServerTickEvent;
import me.purplex.packetevents.events.listener.PacketListener;
import me.purplex.packetevents.events.packetevent.PacketReceiveEvent;
import me.purplex.packetevents.events.packetevent.PacketSendEvent;

import java.util.LinkedList;

public interface PacketEventManager {
    public LinkedList<PacketListener> packetListeners = new LinkedList<>();
    public void callEvent(PacketEvent e);

    public void registerListener(PacketListener e);
    public void unregisterListener(PacketListener e);

    public void unregisterAllListeners(PacketListener e);



}
