/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

public class WrapperPlayServerPlayerAbilities extends PacketWrapper<WrapperPlayServerPlayerAbilities> {
    private boolean godMode;
    private boolean flying;
    private boolean flightAllowed;
    private boolean creativeMode;
    private float flySpeed;
    private float fovModifier;

    public WrapperPlayServerPlayerAbilities(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerPlayerAbilities(boolean godMode, boolean flying, boolean flightAllowed, boolean creativeMode, float flySpeed, float fovModifier) {
        super(PacketType.Play.Server.PLAYER_ABILITIES);
        this.godMode = godMode;
        this.flying = flying;
        this.flightAllowed = flightAllowed;
        this.creativeMode = creativeMode;
        this.flySpeed = flySpeed;
        this.fovModifier = fovModifier;
    }

    @Override
    public void read() {
        byte mask = readByte();
        godMode = (mask & 0x01) != 0;
        flying = (mask & 0x02) != 0;
        flightAllowed = (mask & 0x04) != 0;
        creativeMode = (mask & 0x08) != 0;
        flySpeed = readFloat(); //Defaults to 0.5
        fovModifier = readFloat(); //Defaults to 0.1
    }

    @Override
    public void write() {
        byte mask = 0x00;
        if (godMode) {
            mask |= 0x01;
        }

        if (flying) {
            mask |= 0x02;
        }

        if (flightAllowed) {
            mask |= 0x04;
        }

        if (creativeMode) {
            mask |= 0x08;
        }
        writeByte(mask);
        writeFloat(flySpeed);
        writeFloat(fovModifier);
    }

    @Override
    public void copy(WrapperPlayServerPlayerAbilities wrapper) {
        godMode = wrapper.godMode;
        flying = wrapper.flying;
        flightAllowed = wrapper.flightAllowed;
        creativeMode = wrapper.creativeMode;
        flySpeed = wrapper.flySpeed;
        fovModifier = wrapper.fovModifier;
    }

    public boolean isInGodMode() {
        return godMode;
    }

    public void setInGodMode(boolean godMode) {
        this.godMode = godMode;
    }

    public boolean isFlying() {
        return flying;
    }

    public void setFlying(boolean flying) {
        this.flying = flying;
    }

    public boolean isFlightAllowed() {
        return flightAllowed;
    }

    public void setFlightAllowed(boolean flightAllowed) {
        this.flightAllowed = flightAllowed;
    }

    public boolean isInCreativeMode() {
        return creativeMode;
    }

    public void setInCreativeMode(boolean creativeMode) {
        this.creativeMode = creativeMode;
    }

    public float getFlySpeed() {
        return flySpeed;
    }

    public void setFlySpeed(float flySpeed) {
        this.flySpeed = flySpeed;
    }

    public float getFOVModifier() {
        return fovModifier;
    }

    public void setFOVModifier(float fovModifier) {
        this.fovModifier = fovModifier;
    }
}
