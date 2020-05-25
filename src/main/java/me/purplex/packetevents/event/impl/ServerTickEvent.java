package me.purplex.packetevents.event.impl;


import me.purplex.packetevents.event.PacketEvent;

public class ServerTickEvent extends PacketEvent {

    private final long timestamp;
    private final int tick;

    public ServerTickEvent(final int tick, final long timestamp) {
        this.tick = tick;
        this.timestamp = timestamp;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public int getCurrentTick() {
        return tick;
    }

    public boolean hasOneSecondPassed() {
        return tick % 20 == 0;
    }

}
