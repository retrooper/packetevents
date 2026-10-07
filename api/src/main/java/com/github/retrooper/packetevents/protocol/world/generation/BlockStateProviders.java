/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world.generation;

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
 * @versions 26.3+
 */
@NullMarked
public final class BlockStateProviders {

    // load data from file, block state providers are too complex to define in code here
    private static final Map<ResourceLocation, NBTCompound> PROVIDER_DATA;

    static {
        PROVIDER_DATA = new HashMap<>();
        try (SequentialNBTReader.Compound dataTag = MappingHelper.decompress("mappings/data/worldgen/block_state_provider")) {
            dataTag.skipOne(); // skip version
            for (Map.Entry<String, NBT> entry : (SequentialNBTReader.Compound) dataTag.next().getValue()) {
                ResourceLocation providerKey = new ResourceLocation(entry.getKey());
                PROVIDER_DATA.put(providerKey, ((SequentialNBTReader.Compound) entry.getValue()).readFully());
            }
        } catch (IOException exception) {
            throw new RuntimeException("Error while reading block state provider data", exception);
        }
    }

    private static final VersionedRegistry<BlockStateProvider> REGISTRY = new VersionedRegistry<>("worldgen/block_state_provider");

    private BlockStateProviders() {
    }

    @ApiStatus.Internal
    public static BlockStateProvider define(String key) {
        PacketWrapper<?> wrapper = PacketWrapper.createDummyWrapper(ClientVersion.getLatest());
        return REGISTRY.define(key, data -> {
            NBTCompound dataTag = PROVIDER_DATA.get(data.getName());
            if (dataTag == null) {
                throw new IllegalArgumentException("Can't define block state provider " + data.getName() + ", no data found");
            }
            return BlockStateProvider.DIRECT_CODEC.decode(dataTag, wrapper).copy(data);
        });
    }

    public static VersionedRegistry<BlockStateProvider> getRegistry() {
        return REGISTRY;
    }

    public static final BlockStateProvider CAVE_VINES_BODY = define("cave_vines_body");
    public static final BlockStateProvider CAVE_VINES_HEAD = define("cave_vines_head");
    public static final BlockStateProvider FLOWER_FLOWER_FOREST = define("flower_flower_forest");
    public static final BlockStateProvider FLOWER_MEADOW = define("flower_meadow");
    public static final BlockStateProvider FLOWER_PLAIN = define("flower_plain");
    public static final BlockStateProvider MANGROVE_PROPAGULE = define("mangrove_propagule");
    public static final BlockStateProvider PODZOL_BENEATH_TREE = define("podzol_beneath_tree");
    public static final BlockStateProvider SOIL_BENEATH_TREE = define("soil_beneath_tree");

    static {
        PROVIDER_DATA.clear();
        REGISTRY.unloadMappings();
    }
}
