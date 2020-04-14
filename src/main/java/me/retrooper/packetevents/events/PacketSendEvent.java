package me.retrooper.packetevents.events;

import org.bukkit.entity.Player;

public class PacketSendEvent extends PacketEvent {

    private Player player;
    private String name;
    private Object packet;
    private long timestamp;
    public PacketSendEvent(Player player, String packetName, Object packet) {
        this.player = player;
        this.name = packetName;
        this.packet = packet;
        this.timestamp = (System.nanoTime() / 1000000);
    }


    public Player getPlayer() {
        return this.player;
    }

    public String getPacketName() {
        return this.name;
    }

    public Object getPacket() {
        return this.packet;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

