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

package io.github.retrooper.packetevents.packetwrappers.status.out.pong;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;

import java.lang.reflect.Constructor;

public class WrappedPacketStatusPong extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private long payload;

    public WrappedPacketStatusPong(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketStatusPong(long payload) {
        this.payload = payload;
    }

    @Override
    protected void load() {
        try {
            packetConstructor = PacketTypeClasses.Status.Server.PONG.getConstructor(long.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public long getPayload() {
        if (packet != null) {
            return readLong(0);
        }
        else {
            return payload;
        }
    }

    public void setPayload(long payload) {
        if (packet != null) {
            writeLong(0, payload);
        }
        else {
            this.payload = payload;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        return packetConstructor.newInstance(getPayload());
    }
}
