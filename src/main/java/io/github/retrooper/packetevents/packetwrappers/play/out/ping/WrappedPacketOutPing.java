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

package io.github.retrooper.packetevents.packetwrappers.play.out.ping;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Constructor;

public class WrappedPacketOutPing extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private int id;

    public WrappedPacketOutPing(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutPing(int id) {
        this.id = id;
    }

    @Override
    protected void load() {
        try {
            packetConstructor = PacketTypeClasses.Play.Server.PING.getConstructor(int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        if (packet != null) {
            return readInt(0);
        } else {
            return id;
        }
    }

    public void setId(int id) {
        if (packet != null) {
            writeInt(0, id);
        } else {
            this.id = id;
        }
    }

    @Override
    public boolean isSupported() {
        return version.isNewerThanOrEquals(ServerVersion.v_1_17);
    }

    @Override
    public Object asNMSPacket() throws Exception {
        return packetConstructor.newInstance(getId());
    }
}
