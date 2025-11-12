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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.manager.server.VersionComparison;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientClientStatus extends PacketWrapper<WrapperPlayClientClientStatus> {
    private Action action;

    public WrapperPlayClientClientStatus(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientClientStatus(Action action) {
        super(PacketType.Play.Client.CLIENT_STATUS);
        this.action = action;
    }

    @Override
    public void read() {
        this.action = readMultiVersional(VersionComparison.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_8,
                wrapper -> Action.getById(wrapper.readVarInt()), packetWrapper -> Action.getById(packetWrapper.readByte()));
    }

    @Override
    public void write() {
        writeMultiVersional(VersionComparison.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_8, action.ordinal(), (wrapper, integer) -> {
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16) && integer == 2) {
                throw new IllegalStateException("The WrapperGameClientClientStatus.Action.OPEN_INVENTORY_ACTION enum constant is not supported on 1.16+ servers!");
            }
            wrapper.writeVarInt(integer);
        }, PacketWrapper::writeByte);
    }

    @Override
    public void copy(WrapperPlayClientClientStatus wrapper) {
        this.action = wrapper.action;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public enum Action {
        PERFORM_RESPAWN,
        REQUEST_STATS,

        // This only exists on 1.7.10 -> 1.15.2
        OPEN_INVENTORY_ACHIEVEMENT;

        private static final Action[] VALUES = values();

        public static Action getById(int index) {
            return VALUES[index];
        }
    }
}
