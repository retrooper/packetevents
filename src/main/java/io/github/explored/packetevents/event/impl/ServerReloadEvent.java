package io.github.explored.packetevents.event.impl;

import io.github.explored.packetevents.PacketEvents;
import io.github.explored.packetevents.event.PacketEvent;

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
