package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketEvent;
import org.bukkit.entity.Player;


public final class PacketSendEvent extends PacketEvent {
    private final Player player;
    private final String name;
    private final Object packet;
    private boolean cancelled;

    public PacketSendEvent(final Player player, final String packetName, final Object packet) {
        this.player = player;
        this.name = packetName;
        this.packet = packet;
        this.cancelled = false;
    }


    public Player getPlayer() {
        return this.player;
    }

    public String getPacketName() {
        return this.name;
    }


    /**
     * Get the NMS packet object
     *
     * @return packet object
     */
    public Object getNMSPacket() {
        return this.packet;
    }

    /**
     * Get the class of the NMS packet object
     * @return packet object class
     */
    public Class<?> getNMSPacketClass() {
        return getNMSPacket().getClass();
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}

