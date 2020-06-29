package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketEvent;

public final class ServerTickEvent extends PacketEvent {
    private final int tick;
    private final double tps;

    public ServerTickEvent(final int tick, final long timestamp) {
        this.tick = tick;
        this.timestamp = timestamp;
        this.tps = PacketEvents.getAPI().getServerUtilities().getCurrentServerTPS();
    }

    public ServerTickEvent(final int tick) {
        this.tick = tick;
        this.tps = PacketEvents.getAPI().getServerUtilities().getCurrentServerTPS();
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
