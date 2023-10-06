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

package com.github.retrooper.packetevents.wrapper.configuration.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperConfigClientResourcePackStatus extends PacketWrapper<WrapperConfigClientResourcePackStatus> {

    private Result result;

    public WrapperConfigClientResourcePackStatus(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperConfigClientResourcePackStatus(Result result) {
        super(PacketType.Configuration.Client.RESOURCE_PACK_STATUS);
        this.result = result;
    }

    @Override
    public void read() {
        this.result = Result.VALUES[this.readVarInt()];
    }

    @Override
    public void write() {
        this.writeVarInt(this.result.ordinal());
    }

    @Override
    public void copy(WrapperConfigClientResourcePackStatus wrapper) {
        this.result = wrapper.result;
    }

    public Result getResult() {
        return this.result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public enum Result {
        SUCCESSFULLY_LOADED,
        DECLINED,
        FAILED_DOWNLOAD,
        ACCEPTED;

        public static final Result[] VALUES = values();
    }
}
