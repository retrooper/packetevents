package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketEvent;

//unfinished
public class ServerReloadEvent extends PacketEvent {
    private boolean cancelled;

    public void setCancelled(final boolean val) {
        this.cancelled = val;
    }

    public boolean isCancelled() {
        return cancelled;
    }

}
