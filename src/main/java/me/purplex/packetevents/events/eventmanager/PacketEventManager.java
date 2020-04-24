package me.purplex.packetevents.events.eventmanager;

import me.purplex.packetevents.events.packetevent.ServerTickEvent;
import me.purplex.packetevents.events.listener.PacketListener;
import me.purplex.packetevents.events.packetevent.PacketReceiveEvent;
import me.purplex.packetevents.events.packetevent.PacketSendEvent;

import java.util.LinkedList;

public interface PacketEventManager {
    public LinkedList<PacketListener> packetListeners = new LinkedList<>();
    public void callPacketReceiveEvent(PacketReceiveEvent e);
    public void callPacketSendEvent(PacketSendEvent e);

    public void registerPacketListener(PacketListener e);
    public void unregisterPacketListener(PacketListener e);

    public void callServerTickEvent(ServerTickEvent e);

}
