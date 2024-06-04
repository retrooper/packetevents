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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDebugSample.SampleType;

public class WrapperPlayClientDebugSampleSubscription extends PacketWrapper<WrapperPlayClientDebugSampleSubscription> {

    private SampleType sampleType;

    public WrapperPlayClientDebugSampleSubscription(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientDebugSampleSubscription(SampleType sampleType) {
        super(PacketType.Play.Client.DEBUG_SAMPLE_SUBSCRIPTION);
        this.sampleType = sampleType;
    }

    @Override
    public void read() {
        this.sampleType = SampleType.values()[this.readVarInt()];
    }

    @Override
    public void write() {
        this.writeVarInt(this.sampleType.ordinal());
    }

    @Override
    public void copy(WrapperPlayClientDebugSampleSubscription wrapper) {
        this.sampleType = wrapper.sampleType;
    }

    public SampleType getSampleType() {
        return this.sampleType;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }
}
