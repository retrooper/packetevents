package io.github.explored.packetevents.event.impl;

import io.github.explored.packetevents.event.PacketEvent;
import org.bukkit.entity.Player;

//unfinished
public class PacketLoginEvent extends PacketEvent {
    private final Player player;
    private final String name;
    private final Object packet;
    public PacketLoginEvent(final Player player, final String packetName, final Object packet) {
        this.player = player;
        this.name = packetName;
        this.packet = packet;
    }

    public Player getPlayer() {
        return player;
    }

    public String getPacketName() {
        return name;
    }

    public Object getPacket() {
        return packet;
    }
}
