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
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;

import java.util.OptionalLong;

public interface DimensionType extends MappedEntity {

    OptionalLong getFixedTime();

    boolean hasSkyLight();

    boolean hasCeiling();

    boolean isUltraWarm();

    boolean isNatural();

    double getCoordinateScale();

    boolean isBedWorks();

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

    static DimensionType decode(NBT nbt) {
        NBTCompound compound = (NBTCompound) nbt;
        OptionalLong fixedTime = !compound.getTags().containsKey("fixed_time") ? OptionalLong.empty() :
                OptionalLong.of(compound.getNumberTagOrThrow("fixed_time").getAsLong());
        boolean hasSkylight = compound.getBoolean("has_skylight");
        boolean hasCeiling = compound.getBoolean("has_ceiling");
        boolean ultrawarm = compound.getBoolean("ultrawarm");
        boolean natural = compound.getBoolean("natural");
        double coordinateScale = compound.getNumberTagOrThrow("coordinate_scale").getAsDouble();
        boolean bedWorks = compound.getBoolean("bed_works");
        boolean respawnAnchorWorking = compound.getBoolean("respawn_anchor_works");
        int minY = compound.getNumberTagOrThrow("min_y").getAsInt();
        int height = compound.getNumberTagOrThrow("min_y").getAsInt();
        int logicalHeight = compound.getNumberTagOrThrow("min_y").getAsInt();
        String infiniburnTag = compound.getStringTagValueOrThrow("infiniburn");
        ResourceLocation effectsLocation = new ResourceLocation(compound.getStringTagValueOrThrow("effects"));
        float ambientLight = compound.getNumberTagOrThrow("ambient_light").getAsFloat();
        boolean piglinSafe = compound.getBoolean("piglin_safe");
        boolean hasRaids = compound.getBoolean("has_raids");
        NBT monsterSpawnLightLevel = compound.getTagOrThrow("monster_spawn_light_level");
        int monsterSpawnBlockLightLimit = compound.getNumberTagOrThrow("monster_spawn_block_light_limit").getAsInt();
        return new StaticDimensionType(fixedTime, hasSkylight, hasCeiling, ultrawarm, natural, coordinateScale,
                bedWorks, respawnAnchorWorking, minY, height, logicalHeight, infiniburnTag, effectsLocation,
                ambientLight, piglinSafe, hasRaids, monsterSpawnLightLevel, monsterSpawnBlockLightLimit);
    }

    static NBT encode(DimensionType dimensionType) {
        // TODO
    }
}
