package me.purplex.packetevents.events;

import me.purplex.packetevents.bukkitevent.ServerTickEvent;

import java.util.LinkedList;

public interface PacketEventManager {
    public LinkedList<PacketListener> packetListeners = new LinkedList<>();
    public LinkedList<PacketReceiveEvent> packetReiveEvents = new LinkedList<>();
    public void callPacketReceiveEvent(PacketReceiveEvent e);
    public void callPacketSendEvent(PacketSendEvent e);

    public void registerPacketListener(PacketListener e);

    public void callServerTickEvent(ServerTickEvent e);

}
