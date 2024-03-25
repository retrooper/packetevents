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
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.wrapper.common.client.WrapperClientResourcePackStatus;

import java.util.UUID;

@Deprecated
public class WrapperPlayClientResourcePackStatus extends WrapperClientResourcePackStatus {

    public WrapperPlayClientResourcePackStatus(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientResourcePackStatus(Result result) {
        super(ConnectionState.PLAY, result);
    }

    public WrapperPlayClientResourcePackStatus(UUID packId, Result result) {
        super(ConnectionState.PLAY, packId, result);
    }

    @Deprecated
    public WrapperPlayClientResourcePackStatus(String hash, Result result) {
        super(hash, result);
    }
}
