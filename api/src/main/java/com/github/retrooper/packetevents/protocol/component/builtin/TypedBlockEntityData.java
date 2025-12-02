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

package com.github.retrooper.packetevents.protocol.component.builtin;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.world.blockentity.BlockEntityType;
import com.github.retrooper.packetevents.protocol.world.blockentity.BlockEntityTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public final class TypedBlockEntityData {

    private static final BlockEntityType FALLBACK_TYPE = BlockEntityTypes.FURNACE;

    /**
     * @versions 1.21.9+
     */
    private final BlockEntityType type;
    private final NBTCompound compound;

    public TypedBlockEntityData(NBTCompound compound) {
        this(FALLBACK_TYPE, compound);
    }

    public TypedBlockEntityData(BlockEntityType type, NBTCompound compound) {
        this.type = type;
        this.compound = compound;
    }

    public static TypedBlockEntityData read(PacketWrapper<?> wrapper) {
        BlockEntityType type = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)
                ? wrapper.readMappedEntity(BlockEntityTypes.getRegistry()) : FALLBACK_TYPE;
        NBTCompound compound = wrapper.readNBT();
        return new TypedBlockEntityData(type, compound);
    }

    public static void write(PacketWrapper<?> wrapper, TypedBlockEntityData data) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            wrapper.writeMappedEntity(data.type);
        }
        wrapper.writeNBT(data.compound);
    }

    public BlockEntityType getType() {
        return this.type;
    }

    public NBTCompound getCompound() {
        return this.compound;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        TypedBlockEntityData that = (TypedBlockEntityData) obj;
        if (!this.type.equals(that.type)) return false;
        return this.compound.equals(that.compound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.compound);
    }
}
