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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.Difficulty;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientSetDifficulty extends PacketWrapper<WrapperPlayClientSetDifficulty> {
    private Difficulty difficulty;

    public WrapperPlayClientSetDifficulty(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientSetDifficulty(Difficulty difficulty) {
        super(PacketType.Play.Client.SET_DIFFICULTY);
        this.difficulty = difficulty;
    }

    @Override
    public void read() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
            this.difficulty = this.readEnum(Difficulty.class);
        } else {
            this.difficulty = Difficulty.getById(this.readUnsignedByte());
        }
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
            this.writeEnum(this.difficulty);
        } else {
            this.writeByte(this.difficulty.getId());
        }
    }

    @Override
    public void copy(WrapperPlayClientSetDifficulty wrapper) {
        this.difficulty = wrapper.difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}
