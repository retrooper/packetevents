package io.github.retrooper.packetevents.wrapper.status.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperStatusClientPing extends PacketWrapper {
    private final long time;

    public WrapperStatusClientPing(PacketReceiveEvent event) {
        super(event);
        this.time = readLong();
    }

    public long getTime() {
        return this.time;
    }
}
