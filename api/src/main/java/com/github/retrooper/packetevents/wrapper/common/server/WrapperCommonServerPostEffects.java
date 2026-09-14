/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 packetevents contributors
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

package com.github.retrooper.packetevents.wrapper.common.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * Mojang name: ClientboundPostEffectsPacket
 *
 * @versions 26.3+
 */
@NullMarked
public abstract class WrapperCommonServerPostEffects<T extends WrapperCommonServerPostEffects<T>> extends PacketWrapper<T> {

    protected @MonotonicNonNull List<ResourceLocation> postEffects;

    public WrapperCommonServerPostEffects(PacketSendEvent event) {
        super(event);
    }

    public WrapperCommonServerPostEffects(PacketTypeCommon packetType) {
        super(packetType);
    }

    public WrapperCommonServerPostEffects(PacketTypeCommon packetType, List<ResourceLocation> postEffects) {
        super(packetType);
        this.postEffects = postEffects;
    }

    @Override
    public void read() {
        this.postEffects = this.readList(ResourceLocation::read);
    }

    @Override
    public void write() {
        this.writeList(this.postEffects, ResourceLocation::write);
    }

    @Override
    public void copy(T wrapper) {
        this.postEffects = wrapper.postEffects;
    }

    public List<ResourceLocation> getPostEffects() {
        return this.postEffects;
    }

    public void setPostEffects(List<ResourceLocation> postEffects) {
        this.postEffects = postEffects;
    }
}
