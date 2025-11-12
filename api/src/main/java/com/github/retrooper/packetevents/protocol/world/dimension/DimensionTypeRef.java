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

package com.github.retrooper.packetevents.protocol.world.dimension;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.IRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame;
import org.jetbrains.annotations.Nullable;

public interface DimensionTypeRef {

    DimensionType resolve(IRegistry<DimensionType> registry, ClientVersion version);

    default ResourceLocation getName() {
        throw new UnsupportedOperationException();
    }

    default int getId() {
        throw new UnsupportedOperationException();
    }

    default NBT getData() {
        throw new UnsupportedOperationException();
    }

    static DimensionTypeRef read(PacketWrapper<?> wrapper) {
        ServerVersion version = wrapper.getServerVersion();
        if (version.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            return new IdRef(wrapper.readVarInt());
        }
        boolean v1162 = version.isNewerThanOrEquals(ServerVersion.V_1_16_2);
        if (version.isNewerThanOrEquals(ServerVersion.V_1_19)
                || (!v1162 && version.isNewerThanOrEquals(ServerVersion.V_1_16))) {
            return new NameRef(wrapper.readIdentifier());
        } else if (v1162) {
            return new DataRef(wrapper.readNBTRaw());
        } else {
            return new IdRef(wrapper instanceof WrapperPlayServerJoinGame
                    && version.isOlderThan(ServerVersion.V_1_9_2)
                    ? wrapper.readByte() : wrapper.readInt());
        }
    }

    static void write(PacketWrapper<?> wrapper, DimensionTypeRef ref) {
        ServerVersion version = wrapper.getServerVersion();
        if (version.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            wrapper.writeVarInt(ref.getId());
            return;
        }
        boolean v1162 = version.isNewerThanOrEquals(ServerVersion.V_1_16_2);
        if (version.isNewerThanOrEquals(ServerVersion.V_1_19)
                || (!v1162 && version.isNewerThanOrEquals(ServerVersion.V_1_16))) {
            wrapper.writeIdentifier(ref.getName());
        } else if (v1162) {
            wrapper.writeNBTRaw(ref.getData());
        } else if (wrapper instanceof WrapperPlayServerJoinGame
                && version.isOlderThan(ServerVersion.V_1_9_2)) {
            wrapper.writeByte(ref.getId());
        } else {
            wrapper.writeInt(ref.getId());
        }
    }

    final class DirectRef implements DimensionTypeRef {

        private final DimensionType dimensionType;
        private final ClientVersion version;

        public DirectRef(DimensionType dimensionType, ClientVersion version) {
            this.dimensionType = dimensionType;
            this.version = version;
        }

        @Override
        public DimensionType resolve(IRegistry<DimensionType> registry, ClientVersion version) {
            if (this.version != version) {
                throw new IllegalArgumentException("Expected version " + this.version + ", received " + version
                        + " for direct dimension type ref " + this.dimensionType);
            }
            return this.dimensionType;
        }

        @Override
        public ResourceLocation getName() {
            return this.dimensionType.getName();
        }

        @Override
        public int getId() {
            return this.dimensionType.getId(this.version);
        }

        @Override
        public NBT getData() {
            return DimensionType.encode(this.dimensionType, this.version);
        }

        public DimensionType getDimensionType() {
            return this.dimensionType;
        }

        public ClientVersion getVersion() {
            return this.version;
        }
    }

    final class NameRef implements DimensionTypeRef {

        private final ResourceLocation name;

        public NameRef(ResourceLocation name) {
            this.name = name;
        }

        @Override
        public DimensionType resolve(IRegistry<DimensionType> registry, ClientVersion version) {
            return registry.getByName(this.name);
        }

        @Override
        public ResourceLocation getName() {
            return this.name;
        }
    }

    final class IdRef implements DimensionTypeRef {

        private final int id;

        public IdRef(int id) {
            this.id = id;
        }

        @Override
        public DimensionType resolve(IRegistry<DimensionType> registry, ClientVersion version) {
            return registry.getById(version, this.id);
        }

        @Override
        public int getId() {
            return this.id;
        }
    }

    final class DataRef implements DimensionTypeRef {

        private final NBT data;

        public DataRef(NBT data) {
            this.data = data;
        }

        @Override
        public DimensionType resolve(IRegistry<DimensionType> registry, ClientVersion version) {
            // workaround to make DimensionType#getId work
            //
            // some 1.16 versions don't send any info about the registry id or name
            // of the dimension type, so technically we have to assume it is never defined in a registry
            //
            // as some projects depend on getId working, this is a workaround
            // which will hopefully work for nearly everything
            ResourceLocation name = this.getNullableName();
            if (name != null) {
                DimensionType dimensionType = registry.getByName(name);
                if (dimensionType != null) {
                    return dimensionType;
                }
            }
            return DimensionType.decode(this.data, version, null);
        }

        public @Nullable ResourceLocation getNullableName() {
            if (this.data instanceof NBTCompound) {
                String effectsName = ((NBTCompound) this.data).getStringTagValueOrNull("effects");
                if (effectsName != null) {
                    return new ResourceLocation(effectsName);
                }
            }
            return null;
        }

        @Override
        public ResourceLocation getName() {
            ResourceLocation name = this.getNullableName();
            return name != null ? name : DimensionTypeRef.super.getName();
        }

        @Override
        public NBT getData() {
            return this.data;
        }
    }
}
