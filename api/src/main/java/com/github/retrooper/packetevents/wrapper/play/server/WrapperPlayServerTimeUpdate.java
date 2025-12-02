/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerTimeUpdate extends PacketWrapper<WrapperPlayServerTimeUpdate> {
    private long worldAge;
    private long timeOfDay;
    private boolean tickTime;

    public WrapperPlayServerTimeUpdate(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerTimeUpdate(long worldAge, long timeOfDay) {
        this(worldAge, timeOfDay, timeOfDay >= 0L);
    }

    public WrapperPlayServerTimeUpdate(long worldAge, long timeOfDay, boolean tickTime) {
        super(PacketType.Play.Server.TIME_UPDATE);
        this.worldAge = worldAge;
        this.timeOfDay = timeOfDay;
        this.tickTime = tickTime;
    }

    @Override
    public void read() {
        this.worldAge = readLong();
        this.timeOfDay = readLong();
        this.tickTime = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)
                ? this.readBoolean() : this.timeOfDay >= 0L;
    }

    @Override
    public void write() {
        writeLong(this.worldAge);
        writeLong(this.timeOfDay);
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            this.writeBoolean(this.tickTime);
        }
    }

    @Override
    public void copy(WrapperPlayServerTimeUpdate wrapper) {
        this.worldAge = wrapper.worldAge;
        this.timeOfDay = wrapper.timeOfDay;
        this.tickTime = wrapper.tickTime;
    }

    public long getWorldAge() {
        return worldAge;
    }

    public void setWorldAge(long worldAge) {
        this.worldAge = worldAge;
    }

    public long getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(long timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public boolean isTickTime() {
        return this.tickTime;
    }

    public void setTickTime(boolean tickTime) {
        this.tickTime = tickTime;
    }
}
