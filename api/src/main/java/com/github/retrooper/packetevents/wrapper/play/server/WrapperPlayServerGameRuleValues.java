/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

/**
 * Mojang name: ClientboundGameRuleValuesPacket
 *
 * @versions 26.1+
 */
@NullMarked
public class WrapperPlayServerGameRuleValues extends PacketWrapper<WrapperPlayServerGameRuleValues> {

    private @MonotonicNonNull Map<ResourceLocation, String> values;

    public WrapperPlayServerGameRuleValues(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerGameRuleValues(PacketTypeCommon packetType, Map<ResourceLocation, String> values) {
        super(packetType);
        this.values = values;
    }

    @Override
    public void read() {
        this.values = this.readMap(ResourceLocation::read, PacketWrapper::readString);
    }

    @Override
    public void write() {
        this.writeMap(this.values, ResourceLocation::write, PacketWrapper::writeString);
    }

    @Override
    public void copy(WrapperPlayServerGameRuleValues wrapper) {
        this.values = wrapper.values;
    }

    public Map<ResourceLocation, String> getValues() {
        return this.values;
    }

    public void setValues(Map<ResourceLocation, String> values) {
        this.values = values;
    }
}
