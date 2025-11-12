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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

/**
 * Not to be confused with {@link WrapperPlayClientSteerBoat}
 * <p>
 * This packet is for sending player inputs to the server
 * <p>
 * On 1.8 and older, vehicle control is server sided.  This packet includes inputs for movement.
 * On 1.9 to 1.21.2, plugins may use this packet to create vehicles out of ordinary entities.
 * <p>
 * Starting with 1.21.2, the server sends all movement inputs
 * using the {@link WrapperPlayClientPlayerInput} packet instead.
 */
@ApiStatus.Obsolete
public class WrapperPlayClientSteerVehicle extends PacketWrapper<WrapperPlayClientSteerVehicle> {
    private float sideways;
    private float forward;
    private byte flags;

    public WrapperPlayClientSteerVehicle(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientSteerVehicle(float sideways, float forward, byte flags) {
        super(PacketType.Play.Client.STEER_VEHICLE);
        this.sideways = sideways;
        this.forward = forward;
        this.flags = flags;
    }

    @Override
    public void read() {
        this.sideways = readFloat();
        this.forward = readFloat();
        this.flags = readByte();
    }

    @Override
    public void write() {
        writeFloat(sideways);
        writeFloat(forward);
        writeByte(flags);
    }

    @Override
    public void copy(WrapperPlayClientSteerVehicle wrapper) {
        this.sideways = wrapper.sideways;
        this.forward = wrapper.forward;
        this.flags = wrapper.flags;
    }

    public float getSideways() {
        return sideways;
    }

    public void setSideways(float sideways) {
        this.sideways = sideways;
    }

    public float getForward() {
        return forward;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public byte getFlags() {
        return flags;
    }

    public void setFlags(byte flags) {
        this.flags = flags;
    }

    public boolean isJump() {
        return (flags & 0x01) != 0;
    }

    public void setJump(boolean jump) {
        if (jump) {
            flags |= 0x01;
        } else {
            flags &= ~0x01;
        }
    }

    public boolean isUnmount() {
        return (flags & 0x02) != 0;
    }

    public void setUnmount(boolean unmount) {
        if (unmount) {
            flags |= 0x02;
        } else {
            flags &= ~0x02;
        }
    }
}
