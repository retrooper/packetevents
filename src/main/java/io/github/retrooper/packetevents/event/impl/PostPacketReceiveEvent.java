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
import io.github.retrooper.packetevents.event.eventtypes.PlayerEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import org.bukkit.entity.Player;
/**
 * The {@code PostPacketReceiveEvent} event is fired after minecraft processes
 * a PLAY server-bound packet.
 * You cannot cancel this event since minecraft already processed this packet.
 * If the incoming packet was cancelled, resulting in it not being processed by minecraft,
 * this event won't be called.
 * This event assures you that the {@link PacketReceiveEvent} event wasn't cancelled.
 * @see <a href="https://wiki.vg/Protocol#Serverbound_4">https://wiki.vg/Protocol#Serverbound_4</a>
 * @author retrooper
 * @since 1.7
 */
public class PostPacketReceiveEvent extends PacketEvent implements PlayerEvent {
    private final Player player;
    private final Object packet;
    private byte packetID = -1;

    public PostPacketReceiveEvent(final Player player, final Object packet) {
        this.player = player;
        this.packet = packet;
    }

    /**
     * This method returns the bukkit player object of the packet sender.
     * The player object is guaranteed to NOT be null.
     * @return Packet sender.
     */
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * This method returns the name of the packet.
     * To get the name of the packet we get the class of the packet and then the name of the class.
     * We use java's simple name method.
     * @see Class#getSimpleName()
     * We cache the simple name after the first call to improve performance.
     * It is not recommended to call this method unless you NEED it.
     * If you are comparing packet types, use the {@link PacketType} byte system.
     * You would only need the packet name if packet type system doesn't contain your desired packet yet.
     * @return Name of the packet.
     */
    public String getPacketName() {
        return ClassUtil.getClassSimpleName(packet.getClass());
    }

    /**
     * Get minecraft's decoded packet object.
     * PacketEvents uses this object in the packet wrappers to read the fields.
     * @return Minecraft's decoded packet object.
     */
    public Object getNMSPacket() {
        return packet;
    }

    /**
     * Each binding in each packet state has their own constants.
     * Example Usage:
     * <p>
     *     {@code if (getPacketId() == PacketType.Play.Client.ARM_ANIMATION) }
     * </p>
     * @return Packet ID.
     */
    public byte getPacketId() {
        if (packetID == -1) {
            packetID = PacketType.Play.Client.packetIds.getOrDefault(packet.getClass(), (byte) -1);
        }
        return packetID;
    }

    @Override
    public void call(PacketListenerDynamic listener) {
        listener.onPostPacketReceive(this);
    }
}
