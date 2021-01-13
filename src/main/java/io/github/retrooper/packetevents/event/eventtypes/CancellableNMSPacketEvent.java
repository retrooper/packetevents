package io.github.retrooper.packetevents.event.eventtypes;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;

import java.net.InetSocketAddress;

public abstract class CancellableNMSPacketEvent extends NMSPacketEvent implements CancellableEvent {
    private boolean cancelled;

    public CancellableNMSPacketEvent(Object channel, NMSPacket packet) {
        super(channel, packet);
    }

    public CancellableNMSPacketEvent(InetSocketAddress address, NMSPacket packet) {
        super(address, packet);
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

    @Override
    public void uncancel() {
        cancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean value) {
        cancelled = value;
    }
}
