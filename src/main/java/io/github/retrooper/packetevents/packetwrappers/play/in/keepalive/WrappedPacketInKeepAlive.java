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

package io.github.retrooper.packetevents.packetwrappers.play.in.keepalive;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.reflection.Reflection;

public final class WrappedPacketInKeepAlive extends WrappedPacket {
    private static boolean integerPresentInIndex0;

    public WrappedPacketInKeepAlive(final NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        Class<?> packetClass = PacketTypeClasses.Play.Client.KEEP_ALIVE;
        integerPresentInIndex0 = Reflection.getField(packetClass, int.class, 0) != null;
    }

    public long getId() {
        if (!integerPresentInIndex0) {
            return readLong(0);
        } else {
            return readInt(0);
        }
    }

    public void setId(long id) {
        if (!integerPresentInIndex0) {
            writeLong(0, id);
        } else {
            if (id > Integer.MAX_VALUE) {
                id = Integer.MAX_VALUE;
            } else if (id < Integer.MIN_VALUE) {
                id = Integer.MIN_VALUE;
            }
            writeInt(0, (int) id);
        }
    }
}
