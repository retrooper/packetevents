package me.purplex.packetevents.event.manager;

import me.purplex.packetevents.event.PacketEvent;
import me.purplex.packetevents.event.listener.PacketListener;

import java.util.LinkedList;

public interface PacketEventManager {
    public LinkedList<PacketListener> packetListeners = new LinkedList<>();
    public void callEvent(PacketEvent e);

    public void registerListener(PacketListener e);
    public void unregisterListener(PacketListener e);

    public void unregisterAllListeners(PacketListener e);

    //public void sendPacket(Player player, WrappedPacket packet);

}
