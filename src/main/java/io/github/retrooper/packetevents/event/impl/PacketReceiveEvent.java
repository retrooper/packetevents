/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.CancellableEvent;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.packet.PacketType;
import org.bukkit.entity.Player;

/**
 * This event is called each time a packet is received from a client.
 */
public final class PacketReceiveEvent extends PacketEvent implements CancellableEvent {
    private final Player player;
    private final Object packet;
    private boolean cancelled;

    public PacketReceiveEvent(final Player player, final Object packet) {
        this.player = player;
        this.packet = packet;
    }

    /**
     * Get the packet sender
     * @return player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the packet's name (NMS packet class simple name).
     * @return Name of the packet
     * @deprecated It is recommended not to use this.
     */
    @Deprecated
    public String getPacketName() {
        return this.packet.getClass().getSimpleName();
    }

    /**
     * Get the raw packet object
     * @return packet object
     */
    public Object getNMSPacket() {
        return this.packet;
    }

    /**
     * Get the class of the NMS packet object
     * @deprecated It is useless, rather use getNMSPacket().getClass()
     * @return packet object class
     */
    @Deprecated
    public Class<?> getNMSPacketClass() {
        return packet.getClass();
    }

    /**
     * Get the ID of the packet
     * @return packet id
     */
    public byte getPacketId() {
        return PacketType.Client.packetIds.getOrDefault(packet.getClass(), (byte) -1);
    }

    @Override
    public void cancel() {
        this.cancelled = true;
    }

    @Override
    public void uncancel() {
        this.cancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
