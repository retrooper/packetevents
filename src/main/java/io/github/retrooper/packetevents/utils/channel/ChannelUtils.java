/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package io.github.retrooper.packetevents.utils.channel;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.manager.server.ServerVersion;

import java.net.InetSocketAddress;

public final class ChannelUtils {
    public static InetSocketAddress getSocketAddress(Object ch) {
        if (ch == null) {
            return null;
        }
        if (PacketEvents.get().getServerManager().getVersion() == ServerVersion.v_1_7_10) {
            return ChannelUtils7.getSocketAddress(ch);
        } else {
            return ChannelUtils8.getSocketAddress(ch);
        }
    }
}
