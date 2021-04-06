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

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.eventtypes.CancellableEvent;
import io.github.retrooper.packetevents.event.eventtypes.CancellableNMSPacketEvent;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;

import java.net.InetSocketAddress;

/**
 * The {@code PacketStatusSendEvent} event is fired whenever the server wants to send a STATUS packet to the client.
 * This class implements {@link CancellableEvent}.
 * The {@code PacketStatusSendEvent} does not have to do with a bukkit player object due to
 * the player object being null in this state.
 * Use the {@link #getSocketAddress()} to identify who sends the packet.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol#Status">https://wiki.vg/Protocol#Status</a>
 * @since 1.8
 */
public class PacketStatusSendEvent extends CancellableNMSPacketEvent {
    public PacketStatusSendEvent(Object channel, NMSPacket packet) {
        super(channel, packet);
    }

    public PacketStatusSendEvent(InetSocketAddress address, NMSPacket packet) {
        super(address, packet);
    }


    @Override
    public void call(PacketListenerAbstract listener) {
        if (listener.serverSidedStatusAllowance == null || listener.serverSidedStatusAllowance.contains(getPacketId())) {
            listener.onPacketStatusSend(this);
        }
    }
}
