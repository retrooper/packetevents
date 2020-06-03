package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketEvent;

//unfinished
public class ServerReloadEvent extends PacketEvent {
    private final long currentTime;

    public ServerReloadEvent(final long currentTime){
        this.currentTime = currentTime;
    }

    public ServerReloadEvent() {
        this.currentTime = PacketEvents.currentTimeMS();
    }

    public final long getCurrentTime() {
        return currentTime;
    }
}
