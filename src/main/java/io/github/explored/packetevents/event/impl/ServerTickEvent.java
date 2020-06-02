package io.github.explored.packetevents.event.impl;

import io.github.explored.packetevents.PacketEvents;
import io.github.explored.packetevents.event.PacketEvent;

public class ServerTickEvent extends PacketEvent {
    private final int tick;
    private final double tps;
    public ServerTickEvent(final int tick, final long timestamp) {
        this.tick = tick;
        this.timestamp = timestamp;
        this.tps = PacketEvents.getCurrentServerTPS();
    }

    public ServerTickEvent(final int tick) {
        this.tick = tick;
        this.tps = PacketEvents.getCurrentServerTPS();
    }


    public long getTimestamp() {
        return timestamp;
    }

    public int getCurrentTick() {
        return tick;
    }

    public double getCurrentServerTPS() {
        return tps;
    }
}
