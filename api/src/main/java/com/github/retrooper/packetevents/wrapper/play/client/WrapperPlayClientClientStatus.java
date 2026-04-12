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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

/**
 * Mojang name: ServerboundClientCommandPacket
 */
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
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.action = this.readEnum(Action.VALUES);
        } else {
            this.action = Action.VALUES[this.readByte()];
        }
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            if (this.action == Action.THIRD_ENTRY
                    && this.serverVersion.isOlderThan(ServerVersion.V_26_1)
                    && this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
                throw new IllegalStateException("Third entry not supported");
            }
            this.writeEnum(this.action);
        } else {
            this.writeByte(this.action.ordinal());
        }
    }

    @Override
    public void copy(WrapperPlayClientClientStatus wrapper) {
        this.action = wrapper.action;
    }

    public Action getAction() {
        return this.action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public enum Action {

        PERFORM_RESPAWN,
        REQUEST_STATS,
        @ApiStatus.Internal
        THIRD_ENTRY,
        ;

        /**
         * @versions 1.7.10-1.15.2
         */
        public static final Action OPEN_INVENTORY_ACHIEVEMENT = THIRD_ENTRY;
        /**
         * @versions 26.1+
         */
        public static final Action REQUEST_GAMERULE_VALUES = THIRD_ENTRY;

        private static final Action[] VALUES = values();

        public static Action getById(int index) {
            return VALUES[index];
        }
    }
}
