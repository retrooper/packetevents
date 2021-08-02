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

package io.github.retrooper.packetevents.packetwrappers.play.out.updatetime;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;

import java.lang.reflect.Constructor;

public class WrappedPacketOutUpdateTime extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private long worldAgeTicks;
    private long timeOfDayTicks;

    public WrappedPacketOutUpdateTime(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutUpdateTime(long worldAgeTicks, long timeOfDayTicks) {
        this.worldAgeTicks = worldAgeTicks;
        this.timeOfDayTicks = timeOfDayTicks;
    }

    @Override
    protected void load() {
        try {
            packetConstructor = PacketTypeClasses.Play.Server.UPDATE_TIME.getConstructor(long.class, long.class, boolean.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public long getWorldAgeTicks() {
        if (packet != null) {
            return readLong(0);
        }
        return worldAgeTicks;
    }

    public void setWorldAgeTicks(long ticks) {
        if (packet != null) {
            writeLong(0, ticks);
        } else {
            this.worldAgeTicks = ticks;
        }
    }

    public long getTimeOfDayTicks() {
        if (packet != null) {
            return readLong(1);
        }
        return timeOfDayTicks;
    }

    public void setTimeOfDayTicks(long ticks) {
        if (packet != null) {
            writeLong(1, ticks);
        } else {
            this.timeOfDayTicks = ticks;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        return packetConstructor.newInstance(getWorldAgeTicks(), getTimeOfDayTicks(), true);
    }
}
