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

package com.github.retrooper.packetevents.wrapper.login.client;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

public class WrapperLoginClientCookieResponse extends PacketWrapper<WrapperLoginClientCookieResponse> {

    public static final int MAX_PAYLOAD_SIZE = 1024 * 5;

    private ResourceLocation key;
    private byte @Nullable [] payload;

    public WrapperLoginClientCookieResponse(PacketSendEvent event) {
        super(event);
    }

    public WrapperLoginClientCookieResponse(ResourceLocation key, byte @Nullable [] payload) {
        super(PacketType.Login.Client.COOKIE_RESPONSE);
        this.key = key;
        this.payload = payload;
    }

    @Override
    public void read() {
        this.key = this.readIdentifier();
        this.payload = this.readOptional(wrapper -> wrapper.readByteArray(MAX_PAYLOAD_SIZE));
    }

    @Override
    public void write() {
        this.writeIdentifier(this.key);
        this.writeOptional(this.payload, PacketWrapper::writeByteArray);
    }

    @Override
    public void copy(WrapperLoginClientCookieResponse wrapper) {
        this.key = wrapper.key;
        this.payload = wrapper.payload;
    }

    public ResourceLocation getKey() {
        return this.key;
    }

    public void setKey(ResourceLocation key) {
        this.key = key;
    }

    public byte @Nullable [] getPayload() {
        return this.payload;
    }

    public void setPayload(byte @Nullable [] payload) {
        this.payload = payload;
    }
}
