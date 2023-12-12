/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2023 retrooper and contributors
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

public class WrapperPlayServerTickingStep extends PacketWrapper<WrapperPlayServerTickingStep> {

    private int tickSteps;

    public WrapperPlayServerTickingStep(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerTickingStep(int tickSteps) {
        super(PacketType.Play.Server.TICKING_STEP);
        this.tickSteps = tickSteps;
    }

    @Override
    public void read() {
        this.tickSteps = this.readVarInt();
    }

    @Override
    public void write() {
        this.writeVarInt(this.tickSteps);
    }

    @Override
    public void copy(WrapperPlayServerTickingStep wrapper) {
        this.tickSteps = wrapper.tickSteps;
    }

    public int getTickSteps() {
        return this.tickSteps;
    }

    public void setTickSteps(int tickSteps) {
        this.tickSteps = tickSteps;
    }
}
