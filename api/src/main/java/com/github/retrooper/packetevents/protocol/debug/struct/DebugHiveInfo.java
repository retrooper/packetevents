/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.debug.struct;

import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 1.21.9+
 */
@NullMarked
public final class DebugHiveInfo {

    private final StateType type;
    private final int occupantCount;
    private final int honeyLevel;
    private final boolean sedated;

    public DebugHiveInfo(StateType type, int occupantCount, int honeyLevel, boolean sedated) {
        this.type = type;
        this.occupantCount = occupantCount;
        this.honeyLevel = honeyLevel;
        this.sedated = sedated;
    }

    public static DebugHiveInfo read(PacketWrapper<?> wrapper) {
        StateType type = wrapper.readMappedEntity(StateTypes.getRegistry()).getStateType();
        int occupantCount = wrapper.readVarInt();
        int honeyLevel = wrapper.readVarInt();
        boolean sedated = wrapper.readBoolean();
        return new DebugHiveInfo(type, occupantCount, honeyLevel, sedated);
    }

    public static void write(PacketWrapper<?> wrapper, DebugHiveInfo info) {
        wrapper.writeMappedEntity(info.type.getMapped());
        wrapper.writeVarInt(info.occupantCount);
        wrapper.writeVarInt(info.honeyLevel);
        wrapper.writeBoolean(info.sedated);
    }

    public StateType getType() {
        return this.type;
    }

    public int getOccupantCount() {
        return this.occupantCount;
    }

    public int getHoneyLevel() {
        return this.honeyLevel;
    }

    public boolean isSedated() {
        return this.sedated;
    }
}
