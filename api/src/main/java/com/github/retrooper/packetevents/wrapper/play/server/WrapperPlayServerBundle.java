/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2023 retrooper and contributors
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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * Mojang name: ClientboundBundleDelimiterPacket
 * <p>
 * A delimiter for a bundle of packets, carrying no data. The first delimiter opens a bundle, the next one
 * closes it. While a bundle is open the client buffers every packet it receives, and processes all of them
 * on a single tick once the bundle closes. A bundle holds at most 4096 packets, the client disconnects when
 * that is exceeded.
 * <p>
 * Delimiters carry no owner. Sending one while a bundle is already open closes that bundle, and the
 * delimiter meant to close it opens a new one. Every delimiter after that is inverted, so the client keeps
 * buffering packets and stops acting on them. The vanilla server opens a bundle for the packets which spawn
 * an entity, and plugins open bundles as well, so an already open bundle is normal.
 * <p>
 * Track the state per user with a boolean, flipped every time this packet is seen in a
 * {@link PacketSendEvent}. With no bundle open, send a delimiter, your packets, and a delimiter. With a
 * bundle open, send your packets and let whoever opened it close it.
 *
 * @version 1.19.4+
 */
public class WrapperPlayServerBundle extends PacketWrapper<WrapperPlayServerBundle> {
    public WrapperPlayServerBundle(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerBundle() {
        super(PacketType.Play.Server.BUNDLE);
    }
}
