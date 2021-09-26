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

public class WrapperPlayClientEntityAction extends PacketWrapper<WrapperPlayClientEntityAction> {
    private int entityID;
    private Action action;
    private int jumpBoost;

    public WrapperPlayClientEntityAction(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientEntityAction(int entityID, Action action, int jumpBoost) {
        super(PacketType.Play.Client.ENTITY_ACTION.getID());
        this.entityID = entityID;
        this.action = action;
        this.jumpBoost = jumpBoost;
    }

    @Override
    public void readData() {
        entityID = readVarInt();
        action = Action.VALUES[readVarInt()];
        if (serverVersion.isOlderThan(ServerVersion.v_1_9)) {
            if (action == Action.STOP_JUMPING_WITH_HORSE) {
                action = Action.OPEN_HORSE_INVENTORY;
            }
        }
        jumpBoost = readVarInt();
    }

    @Override
    public void readData(WrapperPlayClientEntityAction wrapper) {
        entityID = wrapper.entityID;
        action = wrapper.action;
        jumpBoost = wrapper.jumpBoost;
    }

    @Override
    public void writeData() {
        writeVarInt(entityID);
        int actionIndex = action.ordinal();
        if (serverVersion.isOlderThan(ServerVersion.v_1_9)) {
            if (action == Action.OPEN_HORSE_INVENTORY) {
                actionIndex--;
            }
        }
        writeVarInt(actionIndex);
        writeVarInt(jumpBoost);
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
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

        public static final Action[] VALUES = values();
    }
}
