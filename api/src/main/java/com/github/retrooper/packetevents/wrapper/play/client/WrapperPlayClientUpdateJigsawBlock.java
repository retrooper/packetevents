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
import com.github.retrooper.packetevents.protocol.world.JointType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

//Packet added in 1.14
public class WrapperPlayClientUpdateJigsawBlock extends PacketWrapper<WrapperPlayClientUpdateJigsawBlock> {
    private Vector3i position;
    private ResourceLocation name;
    private @Nullable ResourceLocation target; // target name
    private ResourceLocation pool; // target pool
    private String finalState; // turns into
    private @Nullable JointType jointType;
    private int selectionPriority; // Added in 1.20.3
    private int placementPriority; // Added in 1.20.3

    public WrapperPlayClientUpdateJigsawBlock(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientUpdateJigsawBlock(Vector3i position, ResourceLocation name, ResourceLocation pool, String finalState) {
        this(position, name, null, pool, finalState, null);
    }

    public WrapperPlayClientUpdateJigsawBlock(Vector3i position, ResourceLocation name, @Nullable ResourceLocation target,
                                              ResourceLocation pool, String finalState, @Nullable JointType jointType) {
        this(position,name,target,pool,finalState,jointType,0,0);
    }
    public WrapperPlayClientUpdateJigsawBlock(Vector3i position, ResourceLocation name, @Nullable ResourceLocation target,
                                              ResourceLocation pool, String finalState, @Nullable JointType jointType,
                                              int selectionPriority, int placementPriority) {
        super(PacketType.Play.Client.UPDATE_JIGSAW_BLOCK);
        this.position = position;
        this.name = name;
        this.target = target;
        this.pool = pool;
        this.finalState = finalState;
        this.jointType = jointType;
        this.selectionPriority = selectionPriority;
        this.placementPriority = placementPriority;
    }

    @Override
    public void read() {
        this.position = readBlockPosition();
        this.name = readIdentifier();
        if (this.v1_16()) {
            this.target = readIdentifier();
        }
        this.pool = readIdentifier();
        this.finalState = readString();
        if (this.v1_16()) {
            this.jointType = JointType.byName(readString()).orElse(JointType.ALIGNED);
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
            this.selectionPriority = this.readVarInt();
            this.placementPriority = this.readVarInt();
        }
    }

    @Override
    public void write() {
        writeBlockPosition(this.position);
        writeIdentifier(this.name);
        if (this.v1_16()) {
            writeIdentifier(this.target);
        }
        writeIdentifier(this.pool);
        writeString(this.finalState);
        if (this.v1_16()) {
            writeString(this.jointType.getSerializedName());
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
            this.writeVarInt(this.selectionPriority);
            this.writeVarInt(this.placementPriority);
        }
    }

    @Override
    public void copy(WrapperPlayClientUpdateJigsawBlock wrapper) {
        this.position = wrapper.position;
        this.name = wrapper.name;
        this.target = wrapper.target;
        this.pool = wrapper.pool;
        this.finalState = wrapper.finalState;
        this.jointType = wrapper.jointType;
        this.selectionPriority = wrapper.selectionPriority;
        this.placementPriority = wrapper.placementPriority;
    }

    private boolean v1_16() {
        return serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16);
    }

    public Vector3i getPosition() {
        return position;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    public ResourceLocation getName() {
        return name;
    }

    public void setName(ResourceLocation name) {
        this.name = name;
    }

    public Optional<ResourceLocation> getTarget() {
        return Optional.ofNullable(target);
    }

    public void setTarget(@Nullable ResourceLocation target) {
        this.target = target;
    }

    public ResourceLocation getPool() {
        return pool;
    }

    public void setPool(ResourceLocation pool) {
        this.pool = pool;
    }

    public String getFinalState() {
        return finalState;
    }

    public void setFinalState(String finalState) {
        this.finalState = finalState;
    }

    public Optional<JointType> getJointType() {
        return Optional.ofNullable(jointType);
    }

    public void setJointType(@Nullable JointType jointType) {
        this.jointType = jointType;
    }

    public int getSelectionPriority() {
        return this.selectionPriority;
    }

    public void setSelectionPriority(int selectionPriority) {
        this.selectionPriority = selectionPriority;
    }

    public int getPlacementPriority() {
        return this.placementPriority;
    }

    public void setPlacementPriority(int placementPriority) {
        this.placementPriority = placementPriority;
    }
}
