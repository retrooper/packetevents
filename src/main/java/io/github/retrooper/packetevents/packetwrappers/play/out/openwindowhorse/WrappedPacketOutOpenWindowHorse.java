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

package io.github.retrooper.packetevents.packetwrappers.play.out.openwindowhorse;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Constructor;

/**
 * Wrapper for the OpenWindowHorse packet.
 *
 * @author retrooper, Tecnio
 * @since 1.8
 */
public final class WrappedPacketOutOpenWindowHorse extends WrappedPacketEntityAbstraction implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private int windowID;
    private int slotCount;

    public WrappedPacketOutOpenWindowHorse(NMSPacket packet) {
        super(packet, 2);
    }

    @Override
    protected void load() {
        try {
            packetConstructor = PacketTypeClasses.Play.Server.OPEN_WINDOW_HORSE.getConstructor(int.class, int.class, int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int getWindowId() {
        if (packet != null) {
            return readInt(0);
        } else {
            return windowID;
        }
    }

    public void setWindowId(int windowID) {
        if (packet != null) {
            writeInt(0, windowID);
        } else {
            this.windowID = windowID;
        }
    }

    public int getSlotCount() {
        if (packet != null) {
            return readInt(1);
        } else {
            return slotCount;
        }
    }

    public void setSlotCount(int slotCount) {
        if (packet != null) {
            writeInt(1, slotCount);
        } else {
            this.slotCount = slotCount;
        }
    }

    @Override
    public boolean isSupported() {
        return version.isNewerThan(ServerVersion.v_1_15_2);
    }

    @Override
    public Object asNMSPacket() throws Exception {
        return packetConstructor.newInstance(getWindowId(), getSlotCount(), getEntityId());
    }
}
