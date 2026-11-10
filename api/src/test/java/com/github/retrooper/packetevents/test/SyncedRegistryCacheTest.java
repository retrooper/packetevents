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

package com.github.retrooper.packetevents.test;

import com.github.retrooper.packetevents.protocol.world.painting.PaintingVariant;
import com.github.retrooper.packetevents.protocol.world.painting.PaintingVariants;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.test.base.BaseDummyAPITest;
import com.github.retrooper.packetevents.util.mappings.SimpleRegistry;
import com.github.retrooper.packetevents.util.mappings.SynchronizedRegistriesHandler;
import com.github.retrooper.packetevents.util.mappings.SynchronizedRegistriesHandler.RegistryEntry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerRegistryData.RegistryElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class SyncedRegistryCacheTest extends BaseDummyAPITest {

    private static final String CUSTOM_PAINTING = "packetevents_test:custom";

    private final Object cacheKey = new Object(); // unique per test, the cache is static

    @AfterEach
    public void dropCacheEntries() {
        SynchronizedRegistriesHandler.invalidateSyncedRegistries(key -> key == this.cacheKey);
    }

    @SuppressWarnings("unchecked")
    private static RegistryEntry<PaintingVariant> paintingEntry() {
        RegistryEntry<?> entry = SynchronizedRegistriesHandler.getRegistryEntry(
                PaintingVariants.getRegistry().getRegistryKey());
        assertNotNull(entry);
        return (RegistryEntry<PaintingVariant>) entry;
    }

    private static List<RegistryElement> elements(int width) {
        NBTCompound data = new NBTCompound();
        data.setTag("asset_id", new NBTString(CUSTOM_PAINTING));
        data.setTag("width", new NBTInt(width));
        data.setTag("height", new NBTInt(1));
        return Collections.singletonList(new RegistryElement(
                new ResourceLocation(CUSTOM_PAINTING), data));
    }

    private static int customWidth(SimpleRegistry<PaintingVariant> registry) {
        PaintingVariant variant = registry.getByName(CUSTOM_PAINTING);
        assertNotNull(variant);
        return variant.getWidth();
    }

    @Test
    @DisplayName("Equal registry data reuses the cached registry")
    public void testEqualDataHitsCache() {
        RegistryEntry<PaintingVariant> entry = paintingEntry();
        PacketWrapper<?> wrapper = PacketWrapper.createDummyWrapper(ClientVersion.V_1_21_5);

        SimpleRegistry<PaintingVariant> first = entry.computeSyncedRegistry(this.cacheKey, elements(2), wrapper);
        // freshly built, equal elements: must be a cache hit
        SimpleRegistry<PaintingVariant> second = entry.computeSyncedRegistry(this.cacheKey, elements(2), wrapper);

        assertSame(first, second);
        assertSame(first, entry.getSyncedRegistry(this.cacheKey));
        assertEquals(2, customWidth(first));
    }

    @Test
    @DisplayName("Changed registry data under the same key is decoded again and replaces the entry")
    public void testChangedDataMissesCache() {
        RegistryEntry<PaintingVariant> entry = paintingEntry();
        PacketWrapper<?> wrapper = PacketWrapper.createDummyWrapper(ClientVersion.V_1_21_5);

        SimpleRegistry<PaintingVariant> before = entry.computeSyncedRegistry(this.cacheKey, elements(2), wrapper);
        SimpleRegistry<PaintingVariant> after = entry.computeSyncedRegistry(this.cacheKey, elements(3), wrapper);

        assertNotSame(before, after);
        assertEquals(3, customWidth(after));
        assertSame(after, entry.getSyncedRegistry(this.cacheKey));
    }

    @Test
    @DisplayName("A different version under the same key is decoded again")
    public void testChangedVersionMissesCache() {
        RegistryEntry<PaintingVariant> entry = paintingEntry();

        SimpleRegistry<PaintingVariant> before = entry.computeSyncedRegistry(this.cacheKey, elements(2),
                PacketWrapper.createDummyWrapper(ClientVersion.V_1_21_5));
        SimpleRegistry<PaintingVariant> after = entry.computeSyncedRegistry(this.cacheKey, elements(2),
                PacketWrapper.createDummyWrapper(ClientVersion.V_1_21_4));

        assertNotSame(before, after);
    }

    @Test
    @DisplayName("Modifying received nbt after caching doesn't change what the cache matches")
    public void testCachedElementsAreCopied() {
        RegistryEntry<PaintingVariant> entry = paintingEntry();
        PacketWrapper<?> wrapper = PacketWrapper.createDummyWrapper(ClientVersion.V_1_21_5);

        List<RegistryElement> received = elements(2);
        SimpleRegistry<PaintingVariant> cached = entry.computeSyncedRegistry(this.cacheKey, received, wrapper);
        // e.g. a packet listener rewriting the registry data after packetevents read it
        ((NBTCompound) received.get(0).getData()).setTag("width", new NBTInt(5));

        assertNotSame(cached, entry.computeSyncedRegistry(this.cacheKey, elements(5), wrapper));
    }

    @Test
    @DisplayName("Invalidated keys are dropped")
    public void testInvalidation() {
        RegistryEntry<PaintingVariant> entry = paintingEntry();
        PacketWrapper<?> wrapper = PacketWrapper.createDummyWrapper(ClientVersion.V_1_21_5);

        entry.computeSyncedRegistry(this.cacheKey, elements(2), wrapper);
        SynchronizedRegistriesHandler.invalidateSyncedRegistries(key -> key == this.cacheKey);

        assertNull(entry.getSyncedRegistry(this.cacheKey));
    }
}
