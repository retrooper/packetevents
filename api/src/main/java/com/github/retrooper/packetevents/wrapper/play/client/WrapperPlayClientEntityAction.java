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

public class WrapperPlayClientEntityAction extends PacketWrapper<WrapperPlayClientEntityAction> {
    private int entityID;
    private Action action;
    private int jumpBoost;

    public WrapperPlayClientEntityAction(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientEntityAction(int entityID, Action action, int jumpBoost) {
        super(PacketType.Play.Client.ENTITY_ACTION);
        this.entityID = entityID;
        this.action = action;
        this.jumpBoost = jumpBoost;
    }

    @Override
    public void read() {
        boolean v1_8 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8);
        entityID = v1_8 ? readVarInt() : readInt();
        int id = v1_8 ? readVarInt() : readByte();
        action = Action.getById(serverVersion, id);
        jumpBoost = v1_8 ? readVarInt() : readInt();
    }

    @Override
    public void write() {
        boolean v1_8 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8);
        if (v1_8) {
            writeVarInt(entityID);
            int actionIndex = action.getId(serverVersion);
            writeVarInt(actionIndex);
            writeVarInt(jumpBoost);
        } else {
            writeInt(entityID);
            int actionIndex = action.getId(serverVersion);
            writeByte(actionIndex);
            writeInt(jumpBoost);
        }
    }

    @Override
    public void copy(WrapperPlayClientEntityAction wrapper) {
        entityID = wrapper.entityID;
        action = wrapper.action;
        jumpBoost = wrapper.jumpBoost;
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getJumpBoost() {
        return jumpBoost;
    }

    public void setJumpBoost(int jumpBoost) {
        this.jumpBoost = jumpBoost;
    }

    public enum Action {
        START_SNEAKING,
        STOP_SNEAKING,
        LEAVE_BED,
        START_SPRINTING,
        STOP_SPRINTING,
        START_JUMPING_WITH_HORSE,
        STOP_JUMPING_WITH_HORSE,
        OPEN_HORSE_INVENTORY,
        START_FLYING_WITH_ELYTRA;

        private static final Action[] VALUES = values();

        public int getId(ServerVersion serverVersion) {
            int actionIndex = ordinal();
            if (serverVersion.isOlderThan(ServerVersion.V_1_9)) {
                if (this == OPEN_HORSE_INVENTORY) {
                    actionIndex--;
                }
            }
            return actionIndex;
        }

        public static Action getById(ServerVersion serverVersion, int id) {
            if (id >= VALUES.length || id < 0) {
                throw new IllegalStateException("EntityAction action out of bounds: " + id);
            }
            Action action = Action.VALUES[id];
            if (serverVersion.isOlderThan(ServerVersion.V_1_9)) {
                if (action == Action.STOP_JUMPING_WITH_HORSE) {
                    action = Action.OPEN_HORSE_INVENTORY;
                }
            }
            return action;
        }
    }
}
