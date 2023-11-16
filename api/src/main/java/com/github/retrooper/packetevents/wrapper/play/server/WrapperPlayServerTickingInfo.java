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
import org.jetbrains.annotations.Nullable;

public class WrapperPlayServerTickingInfo extends PacketWrapper<WrapperPlayServerTickingInfo> {

    private float tickrate;
    private boolean frozen;

    public WrapperPlayServerTickingInfo(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerTickingInfo(float tickrate, boolean frozen) {
        super(PacketType.Play.Server.TICKING_INFO);
        this.tickrate = tickrate;
        this.frozen = frozen;
    }

    @Override
    public void read() {
        this.tickrate = this.readFloat();
        this.frozen = this.readBoolean();
    }

    @Override
    public void write() {
        this.writeFloat(this.tickrate);
        this.writeBoolean(this.frozen);
    }

    @Override
    public void copy(WrapperPlayServerTickingInfo wrapper) {
        this.tickrate = wrapper.tickrate;
        this.frozen = wrapper.frozen;
    }

    /**
     * Tickrate is measured in ticks per second.
     */
    public float getTickrate() {
        return this.tickrate;
    }

    /**
     * Tickrate is measured in ticks per second.
     */
    public void setTickrate(float tickrate) {
        this.tickrate = tickrate;
    }

    public boolean isFrozen() {
        return this.frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }
}
