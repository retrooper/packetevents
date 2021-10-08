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

import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

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
    public void readData() {
        this.leftPaddleTurning = readBoolean();
        this.rightPaddleTurning = readBoolean();
    }

    @Override
    public void readData(WrapperPlayClientSteerBoat wrapper) {
        this.leftPaddleTurning = wrapper.leftPaddleTurning;
        this.rightPaddleTurning = wrapper.rightPaddleTurning;
    }

    @Override
    public void writeData() {
        writeBoolean(leftPaddleTurning);
        writeBoolean(rightPaddleTurning);
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
