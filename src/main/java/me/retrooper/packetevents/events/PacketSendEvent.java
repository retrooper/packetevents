package me.retrooper.packetevents.events;

import org.bukkit.event.*;

public class PacketSendEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public PacketSendEvent() {
    }


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
