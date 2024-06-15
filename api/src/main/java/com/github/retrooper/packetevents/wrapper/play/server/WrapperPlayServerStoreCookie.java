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
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerStoreCookie extends PacketWrapper<WrapperPlayServerStoreCookie> {

    public static final int MAX_PAYLOAD_SIZE = 1024 * 5;

    private ResourceLocation key;
    private byte[] payload;

    public WrapperPlayServerStoreCookie(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerStoreCookie(ResourceLocation key, byte[] payload) {
        super(PacketType.Play.Server.STORE_COOKIE);
        this.key = key;
        this.payload = payload;
    }

    @Override
    public void read() {
        this.key = this.readIdentifier();
        this.payload = this.readByteArray(MAX_PAYLOAD_SIZE);
    }

    @Override
    public void write() {
        this.writeIdentifier(this.key);
        this.writeByteArray(this.payload);
    }

    @Override
    public void copy(WrapperPlayServerStoreCookie wrapper) {
        this.key = wrapper.key;
        this.payload = wrapper.payload;
    }

    public ResourceLocation getKey() {
        return this.key;
    }

    public void setKey(ResourceLocation key) {
        this.key = key;
    }

    public byte[] getPayload() {
        return this.payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
