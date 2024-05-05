/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public final class Dummy {

    public static final Dummy DUMMY = new Dummy();

    private Dummy() {
    }

    public static Dummy dummyRead(PacketWrapper<?> wrapper) {
        return DUMMY;
    }

    public static Dummy dummyReadNbt(PacketWrapper<?> wrapper) {
        wrapper.readNBTRaw();
        return DUMMY;
    }

    public static void dummyWrite(PacketWrapper<?> wrapper, Dummy dummy) {
        // NO-OP
    }

    public static void dummyWriteNbt(PacketWrapper<?> wrapper, Dummy dummy) {
        wrapper.writeByte(0x0A); // compound start
        wrapper.writeByte(0x00); // compound end
    }

    public static Dummy dummy() {
        return DUMMY;
    }

    @Override
    public String toString() {
        return "Dummy{}";
    }
}
