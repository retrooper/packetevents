package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketEvent;

public class PacketLoginEvent extends PacketEvent {
    private final Object channel;
    private final String name;
    private final Object packet;
    public PacketLoginEvent(final Object channel, final String packetName, final Object packet) {
        this.channel =channel;
        this.name = packetName;
        this.packet = packet;
    }

    public final Object getNettyChannel() {
        return channel;
    }

    public final String getPacketName() {
        return name;
    }

    public final Object getPacket() {
        return packet;
    }
}
