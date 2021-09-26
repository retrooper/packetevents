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

package io.github.retrooper.packetevents.wrapper.play.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.protocol.packettype.PacketType;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

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
    public void readData() {
        int index;
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_8)) {
            index = readVarInt();
        } else {
            index = readByte();
        }
        this.action = Action.VALUES[index];
    }

    @Override
    public void readData(WrapperPlayClientClientStatus wrapper) {
        this.action = wrapper.action;
    }

    @Override
    public void writeData() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_8)) {
            int index = action.ordinal();
            if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_16)) {
                if (index == 2) {
                    throw new IllegalStateException("The WrapperGameClientClientStatus.Action.OPEN_INVENTORY_ACTION enum constant is not supported on 1.16+ servers!");
                }
            }
            writeVarInt(action.ordinal());
        } else {
            writeByte(action.ordinal());
        }
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

        //This only exists on 1.7.10 -> 1.15.2
        OPEN_INVENTORY_ACHIEVEMENT;

        public static final Action[] VALUES = values();
    }
}
