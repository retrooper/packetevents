package me.purplex.packetevents.packetevent.impl;


import me.purplex.packetevents.packetevent.PacketEvent;

public class ServerTickEvent extends PacketEvent {

    private final long timestamp;
    public ServerTickEvent(long timestamp) {
        this.timestamp = timestamp;
    }



    public long getTimestamp() {
        return timestamp;
    }

}
