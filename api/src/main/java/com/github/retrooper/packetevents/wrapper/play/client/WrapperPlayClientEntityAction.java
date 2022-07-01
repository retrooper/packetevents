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

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.EntityAction;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientEntityAction extends PacketWrapper<WrapperPlayClientEntityAction> {
    private int entityId;
    private EntityAction action;
    private int jumpBoost;

    public WrapperPlayClientEntityAction(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientEntityAction(int entityId, EntityAction action, int jumpBoost) {
        super(PacketType.Play.Client.ENTITY_ACTION);
        this.entityId = entityId;
        this.action = action;
        this.jumpBoost = jumpBoost;
    }

    @Override
    public void read() {
        entityId = readVarInt();
        action = EntityAction.LEAVE_BED.getById(readVarInt());
        if (serverVersion.isOlderThan(ServerVersion.V_1_9) && action == EntityAction.STOP_JUMPING_WITH_HORSE) {
            action = EntityAction.OPEN_HORSE_INVENTORY;
        }
        jumpBoost = readVarInt();
    }

    @Override
    public void write() {
        writeVarInt(entityId);
        int actionIndex = action.ordinal();
        if (serverVersion.isOlderThan(ServerVersion.V_1_9) && action == EntityAction.OPEN_HORSE_INVENTORY) {
            actionIndex--;
        }
        writeVarInt(actionIndex);
        writeVarInt(jumpBoost);
    }

    @Override
    public void copy(WrapperPlayClientEntityAction wrapper) {
        entityId = wrapper.entityId;
        action = wrapper.action;
        jumpBoost = wrapper.jumpBoost;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public EntityAction getAction() {
        return action;
    }

    public void setAction(EntityAction action) {
        this.action = action;
    }

    public int getJumpBoost() {
        return jumpBoost;
    }

    public void setJumpBoost(int jumpBoost) {
        this.jumpBoost = jumpBoost;
    }
}
