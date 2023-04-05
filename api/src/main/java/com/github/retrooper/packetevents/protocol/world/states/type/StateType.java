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

package com.github.retrooper.packetevents.protocol.world.states.type;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.MaterialType;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;

public class StateType {
    private final String name;
    private final float blastResistance;
    private final float hardness;
    private final boolean isSolid;
    private final boolean isBlocking;
    private final boolean isAir;
    private final boolean requiresCorrectTool;
    private final boolean exceedsCube;
    private final MaterialType materialType;

    public StateType(String name, float blastResistance, float hardness, boolean isSolid, boolean isBlocking, boolean isAir, boolean requiresCorrectTool, boolean isShapeExceedsCube, MaterialType materialType) {
        this.name = name;
        this.blastResistance = blastResistance;
        this.hardness = hardness;
        this.isSolid = isSolid;
        this.isBlocking = isBlocking;
        this.isAir = isAir;
        this.requiresCorrectTool = requiresCorrectTool;
        this.exceedsCube = isShapeExceedsCube;
        this.materialType = materialType;
    }

    public WrappedBlockState createBlockState() {
        return WrappedBlockState.getDefaultState(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), this);
    }

    public WrappedBlockState createBlockState(ClientVersion version) {
        return WrappedBlockState.getDefaultState(version, this);
    }

    public String getName() {
        return name;
    }

    public float getBlastResistance() {
        return blastResistance;
    }

    public float getHardness() {
        return hardness;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public boolean isBlocking() {
        return isBlocking;
    }

    public boolean isAir() {
        return isAir;
    }

    public boolean isRequiresCorrectTool() {
        return requiresCorrectTool;
    }

    public boolean isReplaceable() {
        switch (getMaterialType()) {
            case AIR:
            case STRUCTURAL_AIR:
            case REPLACEABLE_PLANT:
            case REPLACEABLE_FIREPROOF_PLANT:
            case REPLACEABLE_WATER_PLANT:
            case WATER:
            case BUBBLE_COLUMN:
            case LAVA:
            case TOP_SNOW:
            case FIRE:
                return true;
            default:
                return false;
        }
    }

    public boolean exceedsCube() {
        return exceedsCube;
    }

    public MaterialType getMaterialType() {
        return materialType;
    }

    @Override
    public String toString() {
        return name;
    }
}
