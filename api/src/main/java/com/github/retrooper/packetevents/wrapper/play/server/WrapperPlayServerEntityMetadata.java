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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.datawatcher.WatchableObject;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.List;

public class WrapperPlayServerEntityMetadata extends PacketWrapper<WrapperPlayServerEntityMetadata> {
    private int entityID;
    private List<WatchableObject> watchableObjects;

    public WrapperPlayServerEntityMetadata(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityMetadata(int entityID, List<WatchableObject> watchableObjects) {
        super(PacketType.Play.Server.ENTITY_METADATA);
        this.entityID = entityID;
        this.watchableObjects = watchableObjects;
    }
    @Override
    public void readData() {
        entityID = readInt();
        watchableObjects = readWatchableObjects();
    }

    @Override
    public void readData(WrapperPlayServerEntityMetadata wrapper) {
        entityID = wrapper.entityID;
        watchableObjects = wrapper.watchableObjects;
    }

    @Override
    public void writeData() {
        writeInt(entityID);
        writeWatchableObjects(watchableObjects);
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public List<WatchableObject> getWatchableObjects() {
        return watchableObjects;
    }

    public void setWatchableObjects(List<WatchableObject> watchableObjects) {
        this.watchableObjects = watchableObjects;
    }
}
