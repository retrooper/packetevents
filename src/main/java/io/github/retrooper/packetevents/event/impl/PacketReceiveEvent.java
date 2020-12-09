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

import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.eventtypes.CancellableEvent;
import io.github.retrooper.packetevents.event.eventtypes.NMSPacketEvent;
import io.github.retrooper.packetevents.event.eventtypes.PlayerEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelUtils;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;

/**
 * The {@code PacketReceiveEvent} event is fired whenever the a PLAY packet is
 * received from any connected client.
 * Cancelling this event will result in preventing minecraft from processing the incoming packet.
 * It would be as if the player never sent the packet from the server's view.
 * This class implements {@link CancellableEvent} and {@link PlayerEvent}.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol#Play">https://wiki.vg/Protocol#Play</a>
 * @since 1.2.6
 */
public final class PacketReceiveEvent extends PacketEvent implements NMSPacketEvent,CancellableEvent, PlayerEvent {
    private final Player player;
    private final InetSocketAddress address;
    private final Object packet;
    private boolean cancelled;
    private byte packetID = -2;

    public PacketReceiveEvent(final Player player, final Object channel, final Object packet) {
        this.player = player;
        this.address = ChannelUtils.getSocketAddress(channel);
        this.packet = packet;
    }

    /**
     * This method returns the bukkit player object of the packet sender.
     * The player object might be null during early packets.
     * @return Packet sender.
     */
    @NotNull
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * This method returns the socket address of the packet sender.
     * This address if guaranteed to never be null.
     * You could use this to identify who is sending packets
     * whenever the player object is null.
     * @return Packet sender's socket address.
     */
    @NotNull
    @Override
    public InetSocketAddress getSocketAddress() {
        return address;
    }

    @NotNull
    @Override
    public String getPacketName() {
        return ClassUtil.getClassSimpleName(packet.getClass());
    }

    @NotNull
    @Override
    public Object getNMSPacket() {
        return packet;
    }

    /**
     * Each binding in each packet state has their own constants.
     * Example Usage:
     * <p>
     *     {@code if (getPacketId() == PacketType.Play.Client.USE_ENTITY) }
     * </p>
     * @return Packet ID.
     */
    @Override
    public byte getPacketId() {
        if (packetID == -2) {
            packetID = PacketType.Play.Client.packetIds.getOrDefault(packet.getClass(), (byte) -1);
        }
        return packetID;
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

    @Override
    public void call(PacketListenerDynamic listener) {
        listener.onPacketReceive(this);
    }
}
