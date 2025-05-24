/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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
import com.github.retrooper.packetevents.protocol.world.damagetype.DamageType;
import com.github.retrooper.packetevents.protocol.world.damagetype.DamageTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

public class WrapperPlayServerDamageEvent extends PacketWrapper<WrapperPlayServerDamageEvent> {

    private int entityId;
    private DamageType sourceType;
    private int sourceCauseId;
    private int sourceDirectId;
    private @Nullable Vector3d sourcePosition;

    public WrapperPlayServerDamageEvent(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerDamageEvent(int entityId, DamageType sourceType, int sourceCauseId, int sourceDirectId, @Nullable Vector3d sourcePosition) {
        super(PacketType.Play.Server.DAMAGE_EVENT);
        this.entityId = entityId;
        this.sourceType = sourceType;
        this.sourceCauseId = sourceCauseId;
        this.sourceDirectId = sourceDirectId;
        this.sourcePosition = sourcePosition;
    }

    @Override
    public void read() {
        entityId = readVarInt();
        sourceType = readMappedEntity(DamageTypes.getRegistry());
        sourceCauseId = readVarInt();
        sourceDirectId = readVarInt();
        sourcePosition = readOptional(Vector3d::read);
    }

    @Override
    public void write() {
        writeVarInt(entityId);
        writeMappedEntity(sourceType);
        writeVarInt(sourceCauseId);
        writeVarInt(sourceDirectId);
        writeOptional(sourcePosition, Vector3d::write);
    }

    @Override
    public void copy(WrapperPlayServerDamageEvent wrapper) {
        this.entityId = wrapper.entityId;
        this.sourceType = wrapper.sourceType;
        this.sourceCauseId = wrapper.sourceCauseId;
        this.sourceDirectId = wrapper.sourceDirectId;
        this.sourcePosition = wrapper.sourcePosition;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public DamageType getSourceType() {
        return sourceType;
    }

    public void setSourceType(DamageType sourceType) {
        this.sourceType = sourceType;
    }

    public int getSourceCauseId() {
        return sourceCauseId;
    }

    public void setSourceCauseId(int sourceCauseId) {
        this.sourceCauseId = sourceCauseId;
    }

    public int getSourceDirectId() {
        return sourceDirectId;
    }

    public void setSourceDirectId(int sourceDirectId) {
        this.sourceDirectId = sourceDirectId;
    }

    public @Nullable Vector3d getSourcePosition() {
        return sourcePosition;
    }

    public void setSourcePosition(@Nullable Vector3d sourcePosition) {
        this.sourcePosition = sourcePosition;
    }
}
