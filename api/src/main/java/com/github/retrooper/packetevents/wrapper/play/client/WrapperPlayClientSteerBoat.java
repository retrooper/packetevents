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

/**
 * Not to be confused with {@link WrapperPlayClientSteerVehicle}
 * <p>
 * This packet determines whether the boat will have its paddles turning
 * Right paddle with left or forward button
 * Left paddle with right or forward button
 * <p>
 * It is unclear why this is needed when WrapperPlayClientSteerVehicle already gives the correct information
 */
public class WrapperPlayClientSteerBoat extends PacketWrapper<WrapperPlayClientSteerBoat> {
    private boolean leftPaddleTurning;
    private boolean rightPaddleTurning;

    public WrapperPlayClientSteerBoat(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientSteerBoat(boolean leftPaddleTurning, boolean rightPaddleTurning) {
        super(PacketType.Play.Client.STEER_BOAT);
        this.leftPaddleTurning = leftPaddleTurning;
        this.rightPaddleTurning = rightPaddleTurning;
    }

    @Override
    public void read() {
        this.leftPaddleTurning = readBoolean();
        this.rightPaddleTurning = readBoolean();
    }

    @Override
    public void write() {
        writeBoolean(leftPaddleTurning);
        writeBoolean(rightPaddleTurning);
    }

    @Override
    public void copy(WrapperPlayClientSteerBoat wrapper) {
        this.leftPaddleTurning = wrapper.leftPaddleTurning;
        this.rightPaddleTurning = wrapper.rightPaddleTurning;
    }

    public boolean isLeftPaddleTurning() {
        return leftPaddleTurning;
    }

    public void setLeftPaddleTurning(boolean leftPaddleTurning) {
        this.leftPaddleTurning = leftPaddleTurning;
    }

    public boolean isRightPaddleTurning() {
        return rightPaddleTurning;
    }

    public void setRightPaddleTurning(boolean rightPaddleTurning) {
        this.rightPaddleTurning = rightPaddleTurning;
    }
}
