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

public class WrapperPlayServerDisplayScoreboard extends PacketWrapper<WrapperPlayServerDisplayScoreboard> {
    private int position;
    private String scoreName;

    public WrapperPlayServerDisplayScoreboard(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerDisplayScoreboard(int position, String scoreName) {
        super(PacketType.Play.Server.DISPLAY_SCOREBOARD);
        this.position = position;
        this.scoreName = scoreName;
    }

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
            position = readVarInt();
        } else {
            position = readByte();
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
            scoreName = readString();
        } else {
            scoreName = readString(16);
        }
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
            writeVarInt(position);
        } else {
            writeByte(position);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
            writeString(scoreName); // length limit removed
        } else {
            writeString(scoreName, 16);
        }
    }

    @Override
    public void copy(WrapperPlayServerDisplayScoreboard wrapper) {
        position = wrapper.position;
        scoreName = wrapper.scoreName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getScoreName() {
        return scoreName;
    }

    public void setScoreName(String scoreName) {
        this.scoreName = scoreName;
    }
}
