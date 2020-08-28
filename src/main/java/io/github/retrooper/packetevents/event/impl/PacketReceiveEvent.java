/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import org.bukkit.entity.Player;

public final class PacketReceiveEvent extends PacketEvent {
    private final Player player;
    private final Object packet;
    private boolean cancelled;

    public PacketReceiveEvent(final Player player, final Object packet) {
        this.player = player;
        this.packet = packet;
    }

    /**
     * Get the player sending the packet
     *
     * @return player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the packet's name (NMS packet class simple name).
     * @deprecated It is recommended not to use this, it is an expensive method to call.
     * @return Name of the packet
     */
    @Deprecated
    public String getPacketName() {
        return this.packet.getClass().getSimpleName();
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
     * Deprecated, as it is useless, rather use getNMSPacket().getClass()
     *
     * @return packet object class
     */
    @Deprecated
    public Class<?> getNMSPacketClass() {
        return packet.getClass();
    }

    /**
     * Get the ID of the packet
     *
     * @return packet id
     */
    public byte getPacketId() {
        return PacketType.Client.packetIds.getOrDefault(packet.getClass(), (byte) -1);
    }

    /**
     * Returns if the packet has been cancelled
     *
     * @return cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Cancel the packet
     *
     * @param cancelled boolean
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isAsyncByDefault() {
        return true;
    }
}
