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
import com.github.retrooper.packetevents.manager.server.MultiVersion;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientPlayerAbilities extends PacketWrapper<WrapperPlayClientPlayerAbilities> {
    private boolean flying;
    private boolean godMode;
    private boolean flightAllowed;
    private boolean creativeMode;
    private float flySpeed;
    private float walkSpeed;

    public WrapperPlayClientPlayerAbilities(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientPlayerAbilities(boolean flying) {
        this(flying, false, false, false, 0.0F, 0.0F);
    }

    public WrapperPlayClientPlayerAbilities(boolean flying, boolean godMode, boolean flightAllowed, boolean creativeMode, float flySpeed, float walkSpeed) {
        super(PacketType.Play.Client.PLAYER_ABILITIES);
        this.flying = flying;
        this.godMode = godMode;
        this.flightAllowed = flightAllowed;
        this.creativeMode = creativeMode;
        this.flySpeed = flySpeed;
        this.walkSpeed = walkSpeed;
    }

    @Override
    public void read() {
        byte mask = readByte();
        readMulti(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_16, packetWrapper -> flying = (mask & 0x02) != 0, packetWrapper -> {
            godMode = (mask & 0x01) != 0;
            flying = (mask & 0x02) != 0;
            flightAllowed = (mask & 0x04) != 0;
            creativeMode = (mask & 0x08) != 0;
            flySpeed = readFloat();
            walkSpeed = readFloat();
        });
    }

    @Override
    public void write() {
        byte mask = (byte) (flying ? 0x02 : 0x00);
        writeMultiVersional(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_16, mask,
                (packetWrapper, aByte) -> writeByte(aByte),
                (packetWrapper, aByte) -> {
                    byte maskNew = aByte.byteValue();
                    maskNew = 0x00;
                    if (godMode) {
                        maskNew |= 0x01;
                    }

                    if (flying) {
                        maskNew |= 0x02;
                    }

                    if (flightAllowed) {
                        maskNew |= 0x04;
                    }

                    if (creativeMode) {
                        maskNew |= 0x08;
                    }
                    writeByte(maskNew);
                    // These are my guesses of default values, don't cry, just pass in the fly/walk speed next time :0
                    writeFloat(flySpeed == 0.0F ? 0.1F : flySpeed);
                    writeFloat(walkSpeed == 0.0F ? 0.2F : walkSpeed);
                });
    }

    @Override
    public void copy(WrapperPlayClientPlayerAbilities wrapper) {
        godMode = wrapper.godMode;
        flying = wrapper.flying;
        flightAllowed = wrapper.flightAllowed;
        creativeMode = wrapper.creativeMode;
        flySpeed = wrapper.flySpeed;
        walkSpeed = wrapper.walkSpeed;
    }

    public boolean isFlying() {
        return flying;
    }

    public void setFlying(boolean flying) {
        this.flying = flying;
    }

    public boolean isInGodMode() {
        return godMode;
    }

    public void setInGodMode(boolean godMode) {
        this.godMode = godMode;
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

    public void setCreativeMode(boolean creativeMode) {
        this.creativeMode = creativeMode;
    }

    public float getFlySpeed() {
        return flySpeed;
    }

    public void setFlySpeed(float flySpeed) {
        this.flySpeed = flySpeed;
    }

    public float getWalkSpeed() {
        return walkSpeed;
    }

    public void setWalkSpeed(float walkSpeed) {
        this.walkSpeed = walkSpeed;
    }
}
