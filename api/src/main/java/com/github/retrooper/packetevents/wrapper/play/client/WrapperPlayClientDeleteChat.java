/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

public class WrapperPlayClientDeleteChat extends PacketWrapper<WrapperPlayClientDeleteChat> {
    private byte[] signature;

    public WrapperPlayClientDeleteChat(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientDeleteChat(byte[] signature) {
        super(PacketType.Play.Client.DELETE_CHAT);
        this.signature = signature;
    }

    @Override
    public void read() {
        // Can we probably use the SaltSignature class for this packet?
        signature = readByteArray();
    }

    @Override
    public void write() {
        writeByteArray(signature);
    }

    @Override
    public void copy(WrapperPlayClientDeleteChat wrapper) {
        signature = wrapper.signature;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}
