/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

public class WrapperPlayServerDebugSample extends PacketWrapper<WrapperPlayServerDebugSample> {

    private long[] sample;
    private SampleType sampleType;

    public WrapperPlayServerDebugSample(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerDebugSample(long[] sample, SampleType sampleType) {
        super(PacketType.Play.Server.DEBUG_SAMPLE);
        this.sample = sample;
        this.sampleType = sampleType;
    }

    @Override
    public void read() {
        this.sample = this.readLongArray();
        this.sampleType = SampleType.values()[this.readVarInt()];
    }

    @Override
    public void write() {
        this.writeLongArray(this.sample);
        this.writeVarInt(this.sampleType.ordinal());
    }

    @Override
    public void copy(WrapperPlayServerDebugSample wrapper) {
        this.sample = wrapper.sample;
        this.sampleType = wrapper.sampleType;
    }

    public long[] getSample() {
        return this.sample;
    }

    public void setSample(long[] sample) {
        this.sample = sample;
    }

    public SampleType getSampleType() {
        return this.sampleType;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    public enum SampleType {
        TICK_DURATION,
    }
}
