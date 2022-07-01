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

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.MultiVersion;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.scoreboard.UpdateScore;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerUpdateScore extends PacketWrapper<WrapperPlayServerUpdateScore> {
    private String entityName;
    private UpdateScore action;
    private String objectiveName;
    private int value = -1;

    public WrapperPlayServerUpdateScore(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerUpdateScore(String entityName, UpdateScore action, String objectiveName, int value) {
        super(PacketType.Play.Server.UPDATE_SCORE);
        this.entityName = entityName;
        this.action = action;
        this.objectiveName = objectiveName;
        this.value = value;
    }

    @Override
    public void read() {
        readMulti(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_8,
                packetWrapper -> {
                    entityName = packetWrapper.readString(40);
                    action = UpdateScore.getById(packetWrapper.readByte());
                    objectiveName = packetWrapper.readString(16);
                    if (action != UpdateScore.REMOVE_ITEM) {
                        value = packetWrapper.readVarInt();
                    } else {
                        objectiveName = "";
                    }
                }, packetWrapper -> {
                    entityName = packetWrapper.readString(16);
                    action = UpdateScore.getById(packetWrapper.readByte());
                    if (action != UpdateScore.REMOVE_ITEM) {
                        objectiveName = packetWrapper.readString(16);
                        value = packetWrapper.readInt();
                    } else {
                        objectiveName = "";
                    }
                });
    }

    @Override
    public void write() {
        writeMulti(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_8,
                packetWrapper -> {
                    packetWrapper.writeString(entityName, 40);
                    packetWrapper.writeByte(action.ordinal());
                    packetWrapper.writeString(objectiveName, 16);
                    if (action != UpdateScore.REMOVE_ITEM) {
                        packetWrapper.writeVarInt(value);
                    }
                }, packetWrapper -> {
                    packetWrapper.writeString(entityName, 16);
                    packetWrapper.writeByte(action.ordinal());
                    if (action != UpdateScore.REMOVE_ITEM) {
                        packetWrapper.writeString(objectiveName, 16);
                        packetWrapper.writeInt(value);
                    } else {
                        objectiveName = "";
                    }
                });
    }

    @Override
    public void copy(WrapperPlayServerUpdateScore wrapper) {
        entityName = wrapper.entityName;
        action = wrapper.action;
        objectiveName = wrapper.objectiveName;
        value = wrapper.value;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public UpdateScore getAction() {
        return action;
    }

    public void setAction(UpdateScore action) {
        this.action = action;
    }

    public String getObjectiveName() {
        return objectiveName;
    }

    public void setObjectiveName(String objectiveName) {
        this.objectiveName = objectiveName;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
