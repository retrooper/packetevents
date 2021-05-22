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

package io.github.retrooper.packetevents.packetwrappers.login.out.setcompression;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//This packet does not exist on 1.7.10
public class WrappedPacketLoginOutSetCompression extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> constructor;
    private int threshold;

    public WrappedPacketLoginOutSetCompression(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketLoginOutSetCompression(int threshold) {
        this.threshold = threshold;
    }

    @Override
    protected void load() {
        try {
            if (PacketTypeClasses.Login.Server.SET_COMPRESSION != null) {
                constructor = PacketTypeClasses.Login.Server.SET_COMPRESSION.getConstructor(int.class);
            }
        } catch (NoSuchMethodException e) {
            //probably 1.7.10
        }
    }

    /**
     * Maximum size of a packet before it can be compressed.
     *
     * @return threshold Threshold
     */
    public int getThreshold() {
        if (packet != null) {
            return readInt(0);
        }
        return threshold;
    }

    public void setThreshold(int threshold) {
        if (packet != null) {
            writeInt(0, threshold);
        } else {
            this.threshold = threshold;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        try {
            return constructor.newInstance(getThreshold());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isSupported() {
        return version.isNewerThan(ServerVersion.v_1_7_10);
    }
}
