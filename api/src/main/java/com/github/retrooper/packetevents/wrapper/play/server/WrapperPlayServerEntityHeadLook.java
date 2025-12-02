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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityHeadLook extends PacketWrapper<WrapperPlayServerEntityHeadLook> {
    private static final float ROTATION_FACTOR = 256.0F / 360.0F;
    private int entityID;
    private float headYaw;

    public WrapperPlayServerEntityHeadLook(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityHeadLook(int entityID, float headYaw) {
        super(PacketType.Play.Server.ENTITY_HEAD_LOOK);
        this.entityID = entityID;
        this.headYaw = headYaw;
    }

    @Override
    public void read() {
        entityID = readVarInt();
        headYaw = readByte() / ROTATION_FACTOR;
    }

    @Override
    public void write() {
        writeVarInt(entityID);
        writeByte((int) (headYaw * ROTATION_FACTOR));
    }

    @Override
    public void copy(WrapperPlayServerEntityHeadLook wrapper) {
        entityID = wrapper.entityID;
        headYaw = wrapper.headYaw;
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public float getHeadYaw() {
        return headYaw;
    }

    public void setHeadYaw(float headYaw) {
        this.headYaw = headYaw;
    }
}
