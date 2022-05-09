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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.Difficulty;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerServerDifficulty extends PacketWrapper<WrapperPlayServerServerDifficulty> {
    private Difficulty difficulty;
    private boolean locked;

    public WrapperPlayServerServerDifficulty(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerServerDifficulty(Difficulty difficulty) {
        super(PacketType.Play.Server.SERVER_DIFFICULTY);
        this.difficulty = difficulty;
    }

    public WrapperPlayServerServerDifficulty(Difficulty difficulty, boolean locked) {
        super(PacketType.Play.Server.SERVER_DIFFICULTY);
        this.difficulty = difficulty;
        this.locked = locked;
    }

    @Override
    public void read() {
        this.difficulty = Difficulty.getById(readByte());
        if (serverVersion.isNewerThan(ServerVersion.V_1_14_4)) {
            this.locked = readBoolean();
        }
    }

    @Override
    public void copy(WrapperPlayServerServerDifficulty wrapper) {
        this.difficulty = wrapper.difficulty;
        this.locked = wrapper.locked;
    }

    @Override
    public void write() {
        writeByte(difficulty.getId());
        if (serverVersion.isNewerThan(ServerVersion.V_1_14_4)) {
            writeBoolean(locked);
        }
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
