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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;

public class WrapperPlayServerUpdateScore extends PacketWrapper<WrapperPlayServerUpdateScore> {
    private String entityName;
    private Action action;
    private String objectiveName;
    private Optional<Integer> value;

    public enum Action {
        CREATE_OR_UPDATE_ITEM,
        REMOVE_ITEM;

        public static final Action[] VALUES = values();
    }

    public WrapperPlayServerUpdateScore(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerUpdateScore(String entityName, Action action, String objectiveName, Optional<Integer> value) {
        super(PacketType.Play.Server.UPDATE_SCORE);
        this.entityName = entityName;
        this.action = action;
        this.objectiveName = objectiveName;
        this.value = value;
    }

    @Override
    public void read() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            entityName = readString(16);
            action = Action.VALUES[readByte()];
            if (action != Action.REMOVE_ITEM) {
                objectiveName = readString(16);
                value = Optional.of(readInt());
            } else {
                objectiveName = "";
                value = Optional.empty();
            }
        } else {
            entityName = readString(40);
            action = Action.VALUES[readByte()];
            objectiveName = readString(16);
            if (action != Action.REMOVE_ITEM) {
                value = Optional.of(readVarInt());
            } else {
                objectiveName = "";
                value = Optional.empty();
            }
        }
    }

    @Override
    public void write() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            writeString(entityName, 16);
            writeByte(action.ordinal());
            if (action != Action.REMOVE_ITEM) {
                writeString(objectiveName, 16);
                writeInt(value.orElse(-1));
            } else {
                objectiveName = "";
                value = Optional.empty();
            }
        } else {
            writeString(entityName, 40);
            writeByte(action.ordinal());
            writeString(objectiveName, 16);
            if (action != Action.REMOVE_ITEM) {
                writeVarInt(value.orElse(-1));
            }
        }

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

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getObjectiveName() {
        return objectiveName;
    }

    public void setObjectiveName(String objectiveName) {
        this.objectiveName = objectiveName;
    }

    public Optional<Integer> getValue() {
        return value;
    }

    public void setValue(Optional<Integer> value) {
        this.value = value;
    }
}
