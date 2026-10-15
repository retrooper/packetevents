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
package com.github.retrooper.packetevents.protocol.item.blocktransformer;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
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
public final class BlockTransformers {

    // load data from file, block transformers are too complex to define in code here
    private static final Map<ResourceLocation, NBTList<NBT>> TRANSFORMER_DATA;

    static {
        TRANSFORMER_DATA = new HashMap<>();
        try (SequentialNBTReader.Compound dataTag = MappingHelper.decompress("mappings/data/block_transformer")) {
            dataTag.skipOne(); // skip version
            for (Map.Entry<String, NBT> entry : (SequentialNBTReader.Compound) dataTag.next().getValue()) {
                ResourceLocation transformerKey = new ResourceLocation(entry.getKey());
                TRANSFORMER_DATA.put(transformerKey, ((SequentialNBTReader.List) entry.getValue()).readFully());
            }
        } catch (IOException exception) {
            throw new RuntimeException("Error while reading block transformer data", exception);
        }
    }

    private static final VersionedRegistry<BlockTransformer> REGISTRY = new VersionedRegistry<>("block_transformer");

    private BlockTransformers() {
    }

    @ApiStatus.Internal
    public static BlockTransformer define(String key) {
        PacketWrapper<?> wrapper = PacketWrapper.createDummyWrapper(ClientVersion.getLatest());
        return REGISTRY.define(key, data -> {
            NBTList<NBT> dataTag = TRANSFORMER_DATA.get(data.getName());
            if (dataTag == null) {
                throw new IllegalArgumentException("Can't define block transformer " + data.getName() + ", no data found");
            }
            return BlockTransformer.DIRECT_CODEC.decode(dataTag, wrapper).copy(data);
        });
    }

    public static VersionedRegistry<BlockTransformer> getRegistry() {
        return REGISTRY;
    }

    public static final BlockTransformer AXE = define("axe");
    public static final BlockTransformer HOE = define("hoe");
    public static final BlockTransformer SHOVEL = define("shovel");

    static {
        TRANSFORMER_DATA.clear();
        REGISTRY.unloadMappings();
    }
}
