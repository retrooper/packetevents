package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.annotations.PacketHandler;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;

public class ListenerExample implements PacketListener {

    @PacketHandler(priority = PacketEventPriority.NORMAL)
    public void packetReceiveEvent(PacketReceiveEvent event) {
        boolean cancelled = event.isCancelled();
        if (cancelled) return;
    }

}
