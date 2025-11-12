/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.wrapper.common.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.Nullable;

public class WrapperCommonClientCustomClickAction<T extends WrapperCommonClientCustomClickAction<T>> extends PacketWrapper<T> {

    public static final int MAX_PAYLOAD_SIZE = 1 << 16;

    private ResourceLocation id;
    private @Nullable NBT payload;

    public WrapperCommonClientCustomClickAction(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperCommonClientCustomClickAction(PacketTypeCommon packetType, ResourceLocation id, @Nullable NBT payload) {
        super(packetType);
        this.id = id;
        this.payload = payload;
    }

    @Override
    public void read() {
        this.id = ResourceLocation.read(this);
        this.payload = this.readLengthPrefixed(MAX_PAYLOAD_SIZE, PacketWrapper::readNullableNBT);
    }

    @Override
    public void write() {
        ResourceLocation.write(this, this.id);
        this.writeLengthPrefixed(this.payload, PacketWrapper::writeNBTRaw);
    }

    @Override
    public void copy(T wrapper) {
        this.id = wrapper.getId();
        this.payload = wrapper.getPayload();
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    public @Nullable NBT getPayload() {
        return this.payload;
    }

    public void setPayload(@Nullable NBT payload) {
        this.payload = payload;
    }
}
