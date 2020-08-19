package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import org.bukkit.entity.Player;


public final class PacketSendEvent extends PacketEvent {
    private final Player player;
    private final Object packet;
    private boolean cancelled;

    public PacketSendEvent(final Player player, final Object packet) {
        this.player = player;
        this.packet = packet;
        this.cancelled = false;
    }


    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the packet's name (NMS packet class simple name)
     * Deprecated, it is recommended not to use this for performance reasons.
     */
    @Deprecated
    public String getPacketName() {
        return this.packet.getClass().getSimpleName();
    }

    /**
     * Get the ID of the packet
     *
     * @return packet id
     */
    public byte getPacketId() {
        return PacketType.Server.packetIds.getOrDefault(packet.getClass(), (byte) -1);
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
     * Get the class of the NMS packet object.
     * Deprecated because it is useless, rather use getNMSPacket().getClass().
     *
     * @return packet object class
     */
    @Deprecated
    public Class<?> getNMSPacketClass() {
        return packet.getClass();
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}

