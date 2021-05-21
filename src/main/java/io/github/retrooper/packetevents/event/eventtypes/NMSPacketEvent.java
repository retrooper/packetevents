/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2016-2021 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.event.eventtypes;

import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelUtils;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;

import java.net.InetSocketAddress;

/**
 * The {@code NMSPacketEvent} abstract class represents an event that has to do with an actual packet.
 * Don't mix this up with {@link io.github.retrooper.packetevents.event.PacketEvent}.
 * The PacketEvent class represents an event that belongs to PacketEvent's packet system.
 *
 * @author retrooper
 * @since 1.8
 */
public abstract class NMSPacketEvent extends PacketEvent implements CallableEvent {
    private final InetSocketAddress socketAddress;
    private final byte packetID;
    protected NMSPacket packet;
    protected boolean cancelled;

    public NMSPacketEvent(Object channel, NMSPacket packet) {
        this(ChannelUtils.getSocketAddress(channel), packet);//Call the constructor below
    }

    public NMSPacketEvent(InetSocketAddress address, NMSPacket packet) {
        this.socketAddress = address;
        this.packet = packet;
        packetID = PacketType.packetIDMap.getOrDefault(packet.getRawNMSPacket().getClass(), PacketType.INVALID);
    }

    /**
     * Get the associated player's socket address.
     *
     * @return Socket address of the associated player.
     */
    public final InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    /**
     * This method returns the name of the packet.
     * To get the name of the packet we get the class of the packet and then the name of the class.
     * See also: {@link Class#getSimpleName()}
     * We cache the name after the first call to improve performance.
     * Java 8 does not cache the name.
     * It is not recommended to call this method unless you NEED it.
     * If you are comparing packet types, use the {@link PacketType} byte system.
     * You would only need the packet name if packet type system doesn't contain your desired packet yet.
     *
     * @return Name of the packet.
     */
    @Deprecated
    public final String getPacketName() {
        return ClassUtil.getClassSimpleName(packet.getRawNMSPacket().getClass());
    }

    /**
     * Get the NMS packet.
     *
     * @return Get NMS packet.
     */
    public final NMSPacket getNMSPacket() {
        return packet;
    }

    /**
     * Update the NMS Packet.
     *
     * @param packet NMS Object
     */
    public final void setNMSPacket(NMSPacket packet) {
        this.packet = packet;
    }

    /**
     * Get the Packet ID.
     *
     * @return Packet ID.
     */
    public byte getPacketId() {
        return packetID;
    }

    @Override
    public boolean isInbuilt() {
        return true;
    }
}
