package me.purplex.packetevents.events.packetevent;

import me.purplex.packetevents.events.PacketEvent;


public class ServerTickEvent extends PacketEvent {

    private final long timestamp;
    public ServerTickEvent(long timestamp) {
        this.timestamp = timestamp;
    }



    public long getTimestamp() {
        return timestamp;
    }

}
