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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.manager.server.VersionComparison;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayWorldBorderWarningDelay extends PacketWrapper<WrapperPlayWorldBorderWarningDelay> {
    private long delay;

    public WrapperPlayWorldBorderWarningDelay(long delay) {
        super(PacketType.Play.Server.WORLD_BORDER_WARNING_DELAY);
        this.delay = delay;
    }

    public WrapperPlayWorldBorderWarningDelay(PacketSendEvent event) {
        super(event);
    }

    @Override
    public void read() {
        this.delay = readMultiVersional(VersionComparison.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_19, PacketWrapper::readVarInt, PacketWrapper::readVarLong);
    }

    @Override
    public void write() {
        writeMultiVersional(VersionComparison.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_19, this.delay,
                (packetWrapper, aLong) -> packetWrapper.writeVarInt(Math.toIntExact(aLong)), PacketWrapper::writeVarLong);
    }

    @Override
    public void copy(WrapperPlayWorldBorderWarningDelay packet) {
        delay = packet.delay;
    }
}
