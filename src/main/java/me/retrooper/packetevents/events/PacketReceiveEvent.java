package me.retrooper.packetevents.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketReceiveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private String name;
    private Object packet;
    public PacketReceiveEvent(Player player, String packetName, Object packet) {
        this.player = player;
        this.name = packetName;
        this.packet = packet;
    }


    public Player getPlayer() {
        return this.player;
    }

    public String getPacketName() {
        return this.name;
    }

    public Object getPacket() {
        return this.packet;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
