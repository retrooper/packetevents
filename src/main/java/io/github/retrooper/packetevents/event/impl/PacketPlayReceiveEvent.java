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

import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.eventtypes.CancellableNMSPacketEvent;
import io.github.retrooper.packetevents.event.eventtypes.PlayerEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code PacketPlayReceiveEvent} event is fired whenever the a PLAY packet is
 * received from any connected client.
 * Cancelling this event will result in preventing minecraft from processing the incoming packet.
 * It would be as if the player never sent the packet from the server's view.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol#Play">https://wiki.vg/Protocol#Play</a>
 * @since 1.2.6
 */
public final class PacketPlayReceiveEvent extends CancellableNMSPacketEvent implements PlayerEvent {
    private final Player player;

    public PacketPlayReceiveEvent(final Player player, final Object channel, final NMSPacket packet) {
        super(channel, packet);
        this.player = player;
    }

    /**
     * This method returns the bukkit player object of the packet sender.
     * The player object might be null during early packets.
     *
     * @return Packet sender.
     */
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * This method returns the socket address of the packet sender.
     * This address if guaranteed to never be null.
     * You could use this to identify who is sending packets
     * whenever the player object is null.
     *
     * @return Packet sender's socket address.
     */

    /**
     * Each binding in each packet state has their own constants.
     * Example Usage:
     * <p>
     * {@code if (getPacketId() == PacketType.Play.Client.USE_ENTITY) }
     * </p>
     *
     * @return Packet ID.
     */
    @Override
    public byte getPacketId() {
        return PacketType.Play.Client.packetIds.getOrDefault(packet.getRawNMSPacket().getClass(), PacketType.INVALID);
    }

    @Override
    public void call(PacketListenerDynamic listener) {
        if (listener.clientSidedPlayAllowance == null || listener.clientSidedPlayAllowance.contains(getPacketId())) {
            listener.onPacketPlayReceive(this);
        }
    }
}
