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

package io.github.retrooper.packetevents.utils.bytebuf;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.utils.bytebuf.ByteBufAbstract;
import io.netty.buffer.Unpooled;

public final class ByteBufUtil {
    private static boolean legacy = false;
    private static boolean checked = false;


    private static void prepare() {
        if (!checked) {
            legacy = PacketEvents.get().getServerManager().getVersion().isOlderThan(ServerVersion.v_1_8);
            checked = true;
        }
    }

    public static ByteBufAbstract wrappedBuffer(byte[] bytes) {
       prepare();
        if (legacy) {
            return ByteBufUtilLegacy.wrappedBuffer(bytes);
        }
        else {
            return ByteBufUtilModern.wrappedBuffer(bytes);
        }
    }

    public static ByteBufAbstract copiedBuffer(byte[] bytes) {
        prepare();
        if (legacy) {
            return ByteBufUtilLegacy.copiedBuffer(bytes);
        }
        else {
            return ByteBufUtilModern.copiedBuffer(bytes);
        }
    }

    public static ByteBufAbstract buffer() {
        prepare();
        if (legacy) {
            return ByteBufUtilLegacy.buffer();
        }
        else {
            return ByteBufUtilModern.buffer();
        }
    }

    public static ByteBufAbstract buffer(int initialCapacity) {
        prepare();
        if (legacy) {
            return ByteBufUtilLegacy.buffer(initialCapacity);
        }
        else {
            return ByteBufUtilModern.buffer(initialCapacity);
        }
    }

    public static ByteBufAbstract buffer(int initialCapacity, int maxCapacity) {
        prepare();
        if (legacy) {
            return ByteBufUtilLegacy.buffer(initialCapacity, maxCapacity);
        }
        else {
            return ByteBufUtilModern.buffer(initialCapacity, maxCapacity);
        }
    }

    public static ByteBufAbstract directBuffer() {
        prepare();
        if (legacy) {
            return ByteBufUtilLegacy.directBuffer();
        }
        else {
            return ByteBufUtilModern.directBuffer();
        }
    }

    public static ByteBufAbstract directBuffer(int initialCapacity) {
        prepare();
        if (legacy) {
            return ByteBufUtilLegacy.directBuffer(initialCapacity);
        }
        else {
            return ByteBufUtilModern.directBuffer(initialCapacity);
        }
    }

    public static ByteBufAbstract directBuffer(int initialCapacity, int maxCapacity) {
        prepare();
        if (legacy) {
            return ByteBufUtilLegacy.directBuffer(initialCapacity, maxCapacity);
        }
        else {
            return ByteBufUtilModern.directBuffer(initialCapacity, maxCapacity);
        }
    }

    public static ByteBufAbstract compositeBuffer() {
        prepare();
        if (legacy) {
            return ByteBufUtilLegacy.compositeBuffer();
        }
        else {
            return ByteBufUtilModern.compositeBuffer();
        }
    }

    public static ByteBufAbstract compositeBuffer(int maxNumComponents) {
        prepare();
        if (legacy) {
            return ByteBufUtilLegacy.compositeBuffer(maxNumComponents);
        }
        else {
            return ByteBufUtilModern.compositeBuffer(maxNumComponents);
        }
    }
}
