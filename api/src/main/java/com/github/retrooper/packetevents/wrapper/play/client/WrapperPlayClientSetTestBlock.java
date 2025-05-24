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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * Added with 1.21.5
 */
public class WrapperPlayClientSetTestBlock extends PacketWrapper<WrapperPlayClientSetTestBlock> {

    private Vector3i position;
    private TestBlockMode mode;
    private String message;

    public WrapperPlayClientSetTestBlock(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientSetTestBlock(Vector3i position, TestBlockMode mode, String message) {
        super(PacketType.Play.Client.SET_TEST_BLOCK);
        this.position = position;
        this.mode = mode;
        this.message = message;
    }

    @Override
    public void read() {
        this.position = this.readBlockPosition();
        this.mode = this.readEnum(TestBlockMode.class);
        this.message = this.readString();
    }

    @Override
    public void write() {
        this.writeBlockPosition(this.position);
        this.writeEnum(this.mode);
        this.writeString(this.message);
    }

    @Override
    public void copy(WrapperPlayClientSetTestBlock wrapper) {
        this.position = wrapper.position;
        this.mode = wrapper.mode;
        this.message = wrapper.message;
    }

    public Vector3i getPosition() {
        return this.position;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    public TestBlockMode getMode() {
        return this.mode;
    }

    public void setMode(TestBlockMode mode) {
        this.mode = mode;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public enum TestBlockMode {

        START,
        LOG,
        FAIL,
        ACCEPT,
    }
}
