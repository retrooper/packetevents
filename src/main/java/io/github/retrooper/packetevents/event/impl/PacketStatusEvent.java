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
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
/**
 * The {@code PacketStatusEvent} event is fired whenever the a STATUS packet is received from a client
 * or when the server wants to send a STATUS packet to the client.
 * This class implements {@link CancellableEvent}.
 * The {@code PacketStatusEvent} does not have to do with a bukkit player object due to
 * the player object being null in this state.
 * Use the {@link #getChannel()} to identify who sends the packet.
 * @see <a href="https://wiki.vg/Protocol#Status">https://wiki.vg/Protocol#Status</a>
 * @author retrooper
 * @since 1.7
 */
public class PacketStatusEvent extends PacketEvent implements NMSPacketEvent, CancellableEvent {
    private final Object channel;
    private final Object packet;
    private boolean cancelled;
    private byte packetID = -1;

    public PacketStatusEvent(final Object channel, final Object packet) {
        this.channel = channel;
        this.packet = packet;
    }

    /**
     * This method returns the netty channel of the packet sender or receiver as an object.
     * If the packet is client-bound(server-sided) then it will return the channel of the packet receiver.
     * If the packet is server-bound(client-sided) then it will return the channel of the packet sender.
     * You can use that in all cases, it will NOT return the netty channel of the server.
     * The reason it is returned as an object and not with the netty channel import is
     * to maintain 1.7.10 support as 1.7.10 refactored netty to another location.
     * You can use this netty channel to identify who sent the packet.
     * @return Netty channel of the packet sender/receiver.
     */
    public Object getChannel() {
        return channel;
    }


    @Override
    public String getPacketName() {
        return ClassUtil.getClassSimpleName(packet.getClass());
    }

    @Override
    public Object getNMSPacket() {
        return packet;
    }

    /**
     * Each binding in each packet state has their own constants.
     * Example Usage:
     * <p>
     *     {@code if (getPacketId() == PacketType.Status.Client.PING) }
     * </p>
     * @return Packet ID.
     */
    public byte getPacketId() {
        if (packetID == -1) {
            packetID = PacketType.Status.packetIds.getOrDefault(packet.getClass(), (byte) -1);
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
        listener.onPacketStatus(this);
    }
}
