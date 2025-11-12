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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientLockDifficulty extends PacketWrapper<WrapperPlayClientLockDifficulty> {
    private boolean locked;

    public WrapperPlayClientLockDifficulty(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientLockDifficulty(boolean locked) {
        super(PacketType.Play.Client.LOCK_DIFFICULTY);
        this.locked = locked;
    }

    @Override
    public void read() {
        locked = readBoolean();
    }

    @Override
    public void write() {
        writeBoolean(locked);
    }

    @Override
    public void copy(WrapperPlayClientLockDifficulty wrapper) {
        locked = wrapper.locked;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
