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

package com.github.retrooper.packetevents.protocol.world.chunk.palette;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.stream.NetStreamInputWrapper;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutputWrapper;
import com.github.retrooper.packetevents.protocol.world.biome.Biome;
import com.github.retrooper.packetevents.protocol.world.biome.Biomes;
import com.github.retrooper.packetevents.protocol.world.chunk.storage.BitStorage;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.util.mappings.GlobalRegistryHolder;
import com.github.retrooper.packetevents.util.mappings.IRegistry;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

public enum PaletteType {

    /**
     * Added with 1.18:
     * <ul>
     * <li>0 bits → {@link SingletonPalette}</li>
     * <li>1, 2 or 3 bits → {@link ListPalette}</li>
     * <li>else → {@link GlobalPalette}</li>
     * </ul>
     */
    BIOME(3, 3, false, 2),
    /**
     * <ul>
     * <li>0, 1, 2, 3 or 4 bits → {@link ListPalette}</li>
     * <li>5, 6, 7 or 8 bits → {@link MapPalette}</li>
     * <li>else → {@link GlobalPalette}</li>
     * </ul>
     * Changed with 1.18:
     * <ul>
     * <li>0 bits → {@link SingletonPalette}</li>
     * <li>1, 2, 3 or 4 bits → {@link ListPalette}</li>
     * <li>5, 6, 7 or 8 bits → {@link MapPalette}</li>
     * <li>else → {@link GlobalPalette}</li>
     * </ul>
     */
    CHUNK(4, 8, true, 4);

    private final int maxBitsPerEntryForList;
    private final int maxBitsPerEntryForMap;
    private final boolean forceMaxListPaletteSize;
    private final int bitShift;
    private final int storageSize;

    PaletteType(
            int maxBitsPerEntryForList,
            int maxBitsPerEntryForMap,
            boolean forceMaxListPaletteSize,
            int bitShift
    ) {
        this.maxBitsPerEntryForList = maxBitsPerEntryForList;
        this.maxBitsPerEntryForMap = maxBitsPerEntryForMap;
        this.forceMaxListPaletteSize = forceMaxListPaletteSize;
        this.bitShift = bitShift;
        this.storageSize = 1 << bitShift * 3;
    }

    public static void write(PacketWrapper<?> wrapper, DataPalette palette) {
        boolean lengthPrefix = wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5);
        DataPalette.write(new NetStreamOutputWrapper(wrapper), palette, lengthPrefix);
    }

    public DataPalette read(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_16)) {
            return DataPalette.readLegacy(new NetStreamInputWrapper(wrapper), wrapper.getServerVersion().toClientVersion());
        }
        return DataPalette.read(wrapper, this);
    }

    public DataPalette create() {
        return this.create(null, null);
    }

    public DataPalette create(ClientVersion version) {
        return this.create(version, null);
    }

    /**
     * Creates a palette for the packet's protocol version and synchronized registries.
     */
    public DataPalette create(PacketWrapper<?> wrapper) {
        return this.create(wrapper.getServerVersion().toClientVersion(), this.getBiomeRegistry(wrapper));
    }

    private DataPalette create(@Nullable ClientVersion version, @Nullable IRegistry<Biome> biomeRegistry) {
        int bitsPerEntry = this.getMaxBitsPerEntryForList();
        Palette palette = new ListPalette(bitsPerEntry);
        BitStorage storage = new BitStorage(bitsPerEntry, this.getStorageSize());
        return new DataPalette(palette, storage, this, version, biomeRegistry);
    }

    @Nullable IRegistry<Biome> getBiomeRegistry(PacketWrapper<?> wrapper) {
        return this == BIOME ? wrapper.replaceRegistry(Biomes.getRegistry()) : null;
    }

    int getDirectBits(@Nullable ClientVersion version, @Nullable IRegistry<Biome> biomeRegistry) {
        PacketEventsAPI<?> api = PacketEvents.getAPI();
        if (api == null) {
            // Mapping resources require an initialized API.
            return this == BIOME ? 7 : GlobalPalette.BITS_PER_ENTRY;
        }
        if (version == null || version == ClientVersion.UNKNOWN) {
            version = api.getServerManager().getVersion().toClientVersion();
        }

        int registrySize;
        if (this == CHUNK) {
            if (version.isOlderThan(ClientVersion.V_1_13)) {
                // Pre-flattening block IDs are not dense.
                return GlobalPalette.BITS_PER_ENTRY;
            }
            registrySize = WrappedBlockState.getRegistrySize(version);
        } else {
            VersionedRegistry<Biome> vanilla = Biomes.getRegistry();
            if (biomeRegistry == null) {
                biomeRegistry = GlobalRegistryHolder.INSTANCE.getRegistryOr(vanilla, version);
            }
            registrySize = biomeRegistry == vanilla ? vanilla.size(version) : biomeRegistry.size();
        }
        return Integer.SIZE - Integer.numberOfLeadingZeros(registrySize - 1);
    }

    public int getMaxBitsPerEntryForList() {
        return this.maxBitsPerEntryForList;
    }

    public int getMaxBitsPerEntryForMap() {
        return this.maxBitsPerEntryForMap;
    }

    public boolean isForceMaxListPaletteSize() {
        return this.forceMaxListPaletteSize;
    }

    public int getBitShift() {
        return this.bitShift;
    }

    public int getStorageSize() {
        return this.storageSize;
    }

    @Deprecated
    public int getMaxBitsPerEntry() {
        return this.maxBitsPerEntryForMap;
    }

    @Deprecated
    public int getMinBitsPerEntry() {
        return this.maxBitsPerEntryForList;
    }
}
