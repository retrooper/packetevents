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

/**
 * Exists since 1.21.2, includes all movement inputs from the client.
 * <p>
 * This client is sent every tick, if the movement inputs changed.
 * <p>
 * Versions below 1.21.2 will only send {@link WrapperPlayClientSteerVehicle} when on a vehicle.
 */
public class WrapperPlayClientPlayerInput extends PacketWrapper<WrapperPlayClientPlayerInput> {

    private boolean forward;
    private boolean backward;
    private boolean left;
    private boolean right;
    private boolean jump;
    private boolean shift;
    private boolean sprint;

    public WrapperPlayClientPlayerInput(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientPlayerInput(
            boolean forward, boolean backward,
            boolean left, boolean right,
            boolean jump, boolean shift, boolean sprint
    ) {
        super(PacketType.Play.Client.PLAYER_INPUT);
        this.forward = forward;
        this.backward = backward;
        this.left = left;
        this.right = right;
        this.jump = jump;
        this.shift = shift;
        this.sprint = sprint;
    }

    @Override
    public void read() {
        byte flags = this.readByte();
        this.forward = (flags & 1) != 0;
        this.backward = (flags & (1 << 1)) != 0;
        this.left = (flags & (1 << 2)) != 0;
        this.right = (flags & (1 << 3)) != 0;
        this.jump = (flags & (1 << 4)) != 0;
        this.shift = (flags & (1 << 5)) != 0;
        this.sprint = (flags & (1 << 6)) != 0;
    }

    @Override
    public void write() {
        byte flags = 0;
        flags = (byte) (flags | (this.forward ? 1 : 0));
        flags = (byte) (flags | (this.backward ? (1 << 1) : 0));
        flags = (byte) (flags | (this.left ? (1 << 2) : 0));
        flags = (byte) (flags | (this.right ? (1 << 3) : 0));
        flags = (byte) (flags | (this.jump ? (1 << 4) : 0));
        flags = (byte) (flags | (this.shift ? (1 << 5) : 0));
        flags = (byte) (flags | (this.sprint ? (1 << 6) : 0));
        this.writeByte(flags);
    }

    @Override
    public void copy(WrapperPlayClientPlayerInput wrapper) {
        this.forward = wrapper.forward;
        this.backward = wrapper.backward;
        this.left = wrapper.left;
        this.right = wrapper.right;
        this.jump = wrapper.jump;
        this.shift = wrapper.shift;
        this.sprint = wrapper.sprint;
    }

    public boolean isForward() {
        return this.forward;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public boolean isBackward() {
        return this.backward;
    }

    public void setBackward(boolean backward) {
        this.backward = backward;
    }

    public boolean isLeft() {
        return this.left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return this.right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isJump() {
        return this.jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public boolean isShift() {
        return this.shift;
    }

    public void setShift(boolean shift) {
        this.shift = shift;
    }

    public boolean isSprint() {
        return this.sprint;
    }

    public void setSprint(boolean sprint) {
        this.sprint = sprint;
    }
}
