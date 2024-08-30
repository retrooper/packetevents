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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTLong;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalLong;

public interface DimensionType extends MappedEntity, CopyableEntity<DimensionType>, DeepComparableEntity {

    OptionalLong getFixedTime();

    boolean hasSkyLight();

    boolean hasCeiling();

    boolean isUltraWarm();

    boolean isNatural();

    double getCoordinateScale();

    default boolean isShrunk() {
        return this.getCoordinateScale() > 1d;
    }

    boolean isBedWorking();

    boolean isRespawnAnchorWorking();

    default int getMinY() {
        return this.getMinY(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    int getMinY(ClientVersion version);

    default int getHeight() {
        return this.getHeight(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    int getHeight(ClientVersion version);

    default int getLogicalHeight() {
        return this.getLogicalHeight(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    int getLogicalHeight(ClientVersion version);

    String getInfiniburnTag();

    ResourceLocation getEffectsLocation();

    float getAmbientLight();

    // monster settings

    boolean isPiglinSafe();

    boolean hasRaids();

    NBT getMonsterSpawnLightLevel();

    int getMonsterSpawnBlockLightLimit();

    // conversion utilities

    default DimensionTypeRef asRef(ClientVersion version) {
        return new DimensionTypeRef.DirectRef(this, version);
    }

    // nbt decoding/encoding

    static DimensionType decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound) nbt;
        OptionalLong fixedTime = !compound.getTags().containsKey("fixed_time") ? OptionalLong.empty() :
                OptionalLong.of(compound.getNumberTagOrThrow("fixed_time").getAsLong());
        boolean hasSkylight = compound.getBoolean("has_skylight");
        boolean hasCeiling = compound.getBoolean("has_ceiling");
        boolean ultrawarm = compound.getBoolean("ultrawarm");
        boolean natural = compound.getBoolean("natural");
        boolean bedWorking = compound.getBoolean("bed_works");
        boolean respawnAnchorWorking = compound.getBoolean("respawn_anchor_works");
        int logicalHeight = compound.getNumberTagOrThrow("logical_height").getAsInt();
        String infiniburnTag = compound.getStringTagValueOrThrow("infiniburn");
        float ambientLight = compound.getNumberTagOrThrow("ambient_light").getAsFloat();
        boolean piglinSafe = compound.getBoolean("piglin_safe");
        boolean hasRaids = compound.getBoolean("has_raids");

        double coordinateScale;
        int minY = 0;
        int height = 256;
        ResourceLocation effectsLocation = null;
        NBT monsterSpawnLightLevel = null;
        int monsterSpawnBlockLightLimit = 0;
        if (version.isNewerThanOrEquals(ClientVersion.V_1_16_2)) {
            coordinateScale = compound.getNumberTagOrThrow("coordinate_scale").getAsDouble();
            effectsLocation = new ResourceLocation(compound.getStringTagValueOrThrow("effects"));
            if (version.isNewerThanOrEquals(ClientVersion.V_1_17)) {
                minY = compound.getNumberTagOrThrow("min_y").getAsInt();
                height = compound.getNumberTagOrThrow("height").getAsInt();
                if (version.isNewerThanOrEquals(ClientVersion.V_1_19)) {
                    monsterSpawnLightLevel = compound.getTagOrThrow("monster_spawn_light_level");
                    monsterSpawnBlockLightLimit = compound.getNumberTagOrThrow("monster_spawn_block_light_limit").getAsInt();
                }
            }
        } else {
            coordinateScale = compound.getBoolean("shrunk") ? 8d : 1d;
        }

        return new StaticDimensionType(data, fixedTime, hasSkylight, hasCeiling, ultrawarm, natural, coordinateScale,
                bedWorking, respawnAnchorWorking, minY, height, logicalHeight, infiniburnTag, effectsLocation,
                ambientLight, piglinSafe, hasRaids, monsterSpawnLightLevel, monsterSpawnBlockLightLimit);
    }

    static NBT encode(DimensionType dimensionType, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        dimensionType.getFixedTime().ifPresent(fixedTime ->
                compound.setTag("fixed_time", new NBTLong(fixedTime)));
        compound.setTag("has_skylight", new NBTByte(dimensionType.hasSkyLight()));
        compound.setTag("has_ceiling", new NBTByte(dimensionType.hasCeiling()));
        compound.setTag("ultrawarm", new NBTByte(dimensionType.isUltraWarm()));
        compound.setTag("natural", new NBTByte(dimensionType.isNatural()));
        compound.setTag("bed_works", new NBTByte(dimensionType.isBedWorking()));
        compound.setTag("respawn_anchor_works", new NBTByte(dimensionType.isRespawnAnchorWorking()));
        compound.setTag("logical_height", new NBTInt(dimensionType.getLogicalHeight(version)));
        compound.setTag("infiniburn", new NBTString(dimensionType.getInfiniburnTag()));
        compound.setTag("ambient_light", new NBTFloat(dimensionType.getAmbientLight()));
        compound.setTag("piglin_safe", new NBTByte(dimensionType.isPiglinSafe()));
        compound.setTag("has_raids", new NBTByte(dimensionType.hasRaids()));

        if (version.isNewerThanOrEquals(ClientVersion.V_1_16_2)) {
            compound.setTag("coordinate_scale", new NBTDouble(dimensionType.getCoordinateScale()));
            compound.setTag("effects", new NBTString(dimensionType.getEffectsLocation().toString()));
            if (version.isNewerThanOrEquals(ClientVersion.V_1_17)) {
                compound.setTag("min_y", new NBTInt(dimensionType.getMinY(version)));
                compound.setTag("height", new NBTInt(dimensionType.getHeight(version)));
                if (version.isNewerThanOrEquals(ClientVersion.V_1_19)) {
                    compound.setTag("monster_spawn_light_level", dimensionType.getMonsterSpawnLightLevel());
                    compound.setTag("monster_spawn_block_light_limit", new NBTInt(dimensionType.getMonsterSpawnBlockLightLimit()));
                }
            }
        } else {
            compound.setTag("shrunk", new NBTByte(dimensionType.isShrunk()));
        }
        return compound;
    }
}
