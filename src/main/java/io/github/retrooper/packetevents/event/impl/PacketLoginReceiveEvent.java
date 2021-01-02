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
import io.github.retrooper.packetevents.event.eventtypes.CallableEvent;
import io.github.retrooper.packetevents.event.eventtypes.CancellableEvent;
import io.github.retrooper.packetevents.event.eventtypes.NMSPacketEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelUtils;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

/**
 * The {@code PacketLoginReceiveEvent} event is fired whenever the a LOGIN packet is received from a client.
 * This class implements {@link CancellableEvent}.
 * The {@code PacketLoginReceiveEvent} does not have to do with a bukkit player object due to
 * the player object being null in this state.
 * Use the {@link #getSocketAddress()} to identify who sends the packet.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol#Login">https://wiki.vg/Protocol#Login</a>
 * @since 1.8
 */
public class PacketLoginReceiveEvent extends PacketEvent implements NMSPacketEvent, CancellableEvent, CallableEvent {
    private final InetSocketAddress socketAddress;
    private NMSPacket packet;
    private boolean cancelled;
    private byte packetID = -2;

    public PacketLoginReceiveEvent(final Object channel, final NMSPacket packet) {
        this.socketAddress = ChannelUtils.getSocketAddress(channel);
        this.packet = packet;
    }

    public PacketLoginReceiveEvent(final InetSocketAddress socketAddress, final NMSPacket packet) {
        this.socketAddress = socketAddress;
        this.packet = packet;
    }

    /**
     * Socket address of the associated client.
     * This socket address will never be null.
     *
     * @return Socket address of the client.
     */
    @NotNull
    @Override
    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }


    @NotNull
    @Deprecated
    @Override
    public String getPacketName() {
        return ClassUtil.getClassSimpleName(packet.getRawNMSPacket().getClass());
    }

    @NotNull
    @Override
    public NMSPacket getNMSPacket() {
        return packet;
    }

    @Override
    public void setNMSPacket(final NMSPacket packet) {
        this.packet = packet;
    }

    /**
     * Each binding in each packet state has their own constants.
     * Example Usage:
     * <p>
     * {@code if (getPacketId() == PacketType.Login.Client.HANDSHAKE) }
     * </p>
     *
     * @return Packet ID.
     */
    @Override
    public byte getPacketId() {
        if (packetID == -2) {
            packetID = PacketType.Login.Client.packetIds.getOrDefault(packet.getRawNMSPacket().getClass(), (byte) -1);
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
        listener.onPacketLoginReceive(this);
    }
}
