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

package com.github.retrooper.packetevents.protocol.world.attributes.timelines;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.serializer.SequentialNBTReader;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.MappingHelper;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @versions 1.21.11+
 */
@NullMarked
public final class Timelines {

    private static final VersionedRegistry<Timeline> REGISTRY = new VersionedRegistry<>("timeline");

    // load data from file, timelines are too complex to define in code here
    private static final Map<ResourceLocation, NBTCompound> TIMELINE_DATA;

    static {
        TIMELINE_DATA = new HashMap<>();
        try (SequentialNBTReader.Compound dataTag = MappingHelper.decompress("mappings/data/timeline")) {
            dataTag.skipOne(); // skip version
            for (Map.Entry<String, NBT> entry : (SequentialNBTReader.Compound) dataTag.next().getValue()) {
                ResourceLocation timelineKey = new ResourceLocation(entry.getKey());
                TIMELINE_DATA.put(timelineKey, ((SequentialNBTReader.Compound) entry.getValue()).readFully());
            }
        } catch (IOException exception) {
            throw new RuntimeException("Error while reading timeline data", exception);
        }
    }

    private Timelines() {
    }

    @ApiStatus.Internal
    public static Timeline define(String name) {
        return REGISTRY.define(name, data -> {
            NBTCompound dataTag = TIMELINE_DATA.get(data.getName());
            if (dataTag != null) {
                PacketWrapper<?> wrapper = PacketWrapper.createDummyWrapper(ClientVersion.getLatest());
                return Timeline.CODEC.decode(dataTag, wrapper).copy(data);
            }
            throw new IllegalArgumentException("Can't define timeline " + data.getName() + ", no data found");
        });
    }

    public static VersionedRegistry<Timeline> getRegistry() {
        return REGISTRY;
    }

    public static final Timeline DAY = define("day");
    public static final Timeline EARLY_GAME = define("early_game");
    public static final Timeline MOON = define("moon");
    public static final Timeline VILLAGER_SCHEDULE = define("villager_schedule");

    static {
        REGISTRY.unloadMappings();
    }
}
