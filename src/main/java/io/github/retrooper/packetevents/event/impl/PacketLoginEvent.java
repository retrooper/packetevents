package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketEvent;

public class PacketLoginEvent extends PacketEvent {
    private final String name;
    private final Object packet;
    public PacketLoginEvent(final String packetName, final Object packet) {
        this.name = packetName;
        this.packet = packet;
    }

    public String getPacketName() {
        return name;
    }

    public Object getPacket() {
        return packet;
    }
}
