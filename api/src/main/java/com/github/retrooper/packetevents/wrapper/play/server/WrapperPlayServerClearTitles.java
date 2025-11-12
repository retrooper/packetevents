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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerClearTitles extends PacketWrapper<WrapperPlayServerClearTitles> {
    private boolean reset;

    public WrapperPlayServerClearTitles(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerClearTitles(boolean reset) {
        super(PacketType.Play.Server.CLEAR_TITLES);
        this.reset = reset;
    }

    @Override
    public void read() {
        reset = readBoolean();
    }

    @Override
    public void copy(WrapperPlayServerClearTitles wrapper) {
        reset = wrapper.reset;
    }

    @Override
    public void write() {
        writeBoolean(reset);
    }

    public boolean getReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }
}
