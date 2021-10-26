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
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientFlying<T extends WrapperPlayClientFlying> extends PacketWrapper<T> {
    private boolean onGround;

    public WrapperPlayClientFlying(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientFlying(boolean onGround) {
        super(PacketType.Play.Client.PLAYER_FLYING);
        this.onGround = onGround;
    }

    public WrapperPlayClientFlying(PacketTypeCommon type, boolean onGround) {
        super(type);
        this.onGround = onGround;
    }

    //TODO Rethink, should this be somewhere else?
    public static boolean isInstanceOfFlying(PacketTypeCommon type) {
        return type == PacketType.Play.Client.PLAYER_FLYING
                || type == PacketType.Play.Client.PLAYER_POSITION
                || type == PacketType.Play.Client.PLAYER_ROTATION
                || type == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION;
    }


    @Override
    public void readData() {
        onGround = readBoolean();
    }

    @Override
    public void readData(WrapperPlayClientFlying wrapper) {
        onGround = wrapper.onGround;
    }

    @Override
    public void writeData() {
        writeBoolean(onGround);
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
