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

public class WrapperPlayServerTickingState extends PacketWrapper<WrapperPlayServerTickingState> {

    private float tickRate;
    private boolean frozen;

    public WrapperPlayServerTickingState(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerTickingState(float tickRate, boolean frozen) {
        super(PacketType.Play.Server.TICKING_STATE);
        this.tickRate = tickRate;
        this.frozen = frozen;
    }

    @Override
    public void read() {
        this.tickRate = this.readFloat();
        this.frozen = this.readBoolean();
    }

    @Override
    public void write() {
        this.writeFloat(this.tickRate);
        this.writeBoolean(this.frozen);
    }

    @Override
    public void copy(WrapperPlayServerTickingState wrapper) {
        this.tickRate = wrapper.tickRate;
        this.frozen = wrapper.frozen;
    }

    /**
     * The tick rate is measured in ticks per second.
     */
    public float getTickRate() {
        return this.tickRate;
    }

    /**
     * The tick rate is measured in ticks per second.
     */
    public void setTickRate(float tickRate) {
        this.tickRate = tickRate;
    }

    public boolean isFrozen() {
        return this.frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }
}
