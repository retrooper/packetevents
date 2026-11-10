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

package com.github.retrooper.packetevents.util.mappings;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.chat.ChatType;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.dialog.Dialog;
import com.github.retrooper.packetevents.protocol.dialog.Dialogs;
import com.github.retrooper.packetevents.protocol.entity.cat.CatSoundVariant;
import com.github.retrooper.packetevents.protocol.entity.cat.CatSoundVariants;
import com.github.retrooper.packetevents.protocol.entity.cat.CatVariant;
import com.github.retrooper.packetevents.protocol.entity.cat.CatVariants;
import com.github.retrooper.packetevents.protocol.entity.chicken.ChickenSoundVariant;
import com.github.retrooper.packetevents.protocol.entity.chicken.ChickenSoundVariants;
import com.github.retrooper.packetevents.protocol.entity.chicken.ChickenVariant;
import com.github.retrooper.packetevents.protocol.entity.chicken.ChickenVariants;
import com.github.retrooper.packetevents.protocol.entity.cow.CowSoundVariant;
import com.github.retrooper.packetevents.protocol.entity.cow.CowSoundVariants;
import com.github.retrooper.packetevents.protocol.entity.cow.CowVariant;
import com.github.retrooper.packetevents.protocol.entity.cow.CowVariants;
import com.github.retrooper.packetevents.protocol.entity.frog.FrogVariant;
import com.github.retrooper.packetevents.protocol.entity.frog.FrogVariants;
import com.github.retrooper.packetevents.protocol.entity.nautilus.ZombieNautilusVariant;
import com.github.retrooper.packetevents.protocol.entity.nautilus.ZombieNautilusVariants;
import com.github.retrooper.packetevents.protocol.entity.pig.PigSoundVariant;
import com.github.retrooper.packetevents.protocol.entity.pig.PigSoundVariants;
import com.github.retrooper.packetevents.protocol.entity.pig.PigVariant;
import com.github.retrooper.packetevents.protocol.entity.pig.PigVariants;
import com.github.retrooper.packetevents.protocol.entity.sulfurcube.SulfurCubeArchtype;
import com.github.retrooper.packetevents.protocol.entity.sulfurcube.SulfurCubeArchtypes;
import com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfSoundVariant;
import com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfSoundVariants;
import com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfVariant;
import com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfVariants;
import com.github.retrooper.packetevents.protocol.item.banner.BannerPattern;
import com.github.retrooper.packetevents.protocol.item.banner.BannerPatterns;
import com.github.retrooper.packetevents.protocol.item.blocktransformer.BlockTransformer;
import com.github.retrooper.packetevents.protocol.item.blocktransformer.BlockTransformers;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentType;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import com.github.retrooper.packetevents.protocol.item.instrument.Instrument;
import com.github.retrooper.packetevents.protocol.item.instrument.Instruments;
import com.github.retrooper.packetevents.protocol.item.jukebox.IJukeboxSong;
import com.github.retrooper.packetevents.protocol.item.jukebox.JukeboxSongs;
import com.github.retrooper.packetevents.protocol.item.trimmaterial.TrimMaterial;
import com.github.retrooper.packetevents.protocol.item.trimmaterial.TrimMaterials;
import com.github.retrooper.packetevents.protocol.item.trimpattern.TrimPattern;
import com.github.retrooper.packetevents.protocol.item.trimpattern.TrimPatterns;
import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.util.NbtDecoder;
import com.github.retrooper.packetevents.protocol.world.attributes.timelines.Timeline;
import com.github.retrooper.packetevents.protocol.world.attributes.timelines.Timelines;
import com.github.retrooper.packetevents.protocol.world.biome.Biome;
import com.github.retrooper.packetevents.protocol.world.biome.Biomes;
import com.github.retrooper.packetevents.protocol.world.blockentity.decopot.DecoratedPotPattern;
import com.github.retrooper.packetevents.protocol.world.blockentity.decopot.DecoratedPotPatterns;
import com.github.retrooper.packetevents.protocol.world.clock.WorldClock;
import com.github.retrooper.packetevents.protocol.world.clock.WorldClocks;
import com.github.retrooper.packetevents.protocol.world.damagetype.DamageType;
import com.github.retrooper.packetevents.protocol.world.damagetype.DamageTypes;
import com.github.retrooper.packetevents.protocol.world.generation.BlockStateProvider;
import com.github.retrooper.packetevents.protocol.world.generation.BlockStateProviders;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionTypes;
import com.github.retrooper.packetevents.protocol.world.painting.PaintingVariant;
import com.github.retrooper.packetevents.protocol.world.painting.PaintingVariants;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerRegistryData.RegistryElement;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@NullMarked
@ApiStatus.Internal
public final class SynchronizedRegistriesHandler {

    private static final boolean FORCE_PER_USER_REGISTRIES = Boolean.getBoolean("packetevents.force-per-user-registries");
    private static final Map<ResourceLocation, RegistryEntry<?>> REGISTRY_KEYS = new HashMap<>();

    static {
        // packetevents ignores a few unimportant registries which aren't used anywhere else in the protocol:
        // test_environment, test_instance
        Stream.of(
                new RegistryEntry<>(Biomes.getRegistry(), Biome.CODEC),
                new RegistryEntry<>(ChatTypes.getRegistry(), ChatType::decode),
                new RegistryEntry<>(TrimPatterns.getRegistry(), (NbtEntryDecoder<TrimPattern>) TrimPattern::decode),
                new RegistryEntry<>(TrimMaterials.getRegistry(), TrimMaterial.DIRECT_CODEC),
                new RegistryEntry<>(WolfVariants.getRegistry(), WolfVariant.CODEC),
                new RegistryEntry<>(WolfSoundVariants.getRegistry(), WolfSoundVariant.CODEC),
                new RegistryEntry<>(PigVariants.getRegistry(), PigVariant::decode),
                new RegistryEntry<>(FrogVariants.getRegistry(), FrogVariant::decode),
                new RegistryEntry<>(CatVariants.getRegistry(), CatVariant.CODEC),
                new RegistryEntry<>(CowVariants.getRegistry(), CowVariant.CODEC),
                new RegistryEntry<>(ChickenVariants.getRegistry(), ChickenVariant.CODEC),
                new RegistryEntry<>(ZombieNautilusVariants.getRegistry(), ZombieNautilusVariant::decode),
                new RegistryEntry<>(PaintingVariants.getRegistry(), PaintingVariant.CODEC),
                new RegistryEntry<>(DimensionTypes.getRegistry(), DimensionType.CODEC),
                new RegistryEntry<>(DamageTypes.getRegistry(), DamageType.DIRECT_CODEC),
                new RegistryEntry<>(BannerPatterns.getRegistry(), BannerPattern::decode),
                new RegistryEntry<>(EnchantmentTypes.getRegistry(), (NbtEntryDecoder<EnchantmentType>) EnchantmentType::decode),
                new RegistryEntry<>(JukeboxSongs.getRegistry(), (NbtEntryDecoder<IJukeboxSong>) IJukeboxSong::decode),
                new RegistryEntry<>(Instruments.getRegistry(), (NbtEntryDecoder<Instrument>) Instrument::decode),
                new RegistryEntry<>(Dialogs.getRegistry(), Dialog::decodeDirect),
                new RegistryEntry<>(Timelines.getRegistry(), Timeline.CODEC),
                new RegistryEntry<>(PigSoundVariants.getRegistry(), PigSoundVariant.CODEC),
                new RegistryEntry<>(CatSoundVariants.getRegistry(), CatSoundVariant.CODEC),
                new RegistryEntry<>(CowSoundVariants.getRegistry(), CowSoundVariant.CODEC),
                new RegistryEntry<>(ChickenSoundVariants.getRegistry(), ChickenSoundVariant.CODEC),
                new RegistryEntry<>(WorldClocks.getRegistry(), WorldClock.DIRECT_CODEC),
                new RegistryEntry<>(SulfurCubeArchtypes.getRegistry(), SulfurCubeArchtype.DIRECT_CODEC),
                new RegistryEntry<>(DecoratedPotPatterns.getRegistry(), DecoratedPotPattern.CODEC),
                new RegistryEntry<>(BlockTransformers.getRegistry(), BlockTransformer.DIRECT_CODEC),
                new RegistryEntry<>(BlockStateProviders.getRegistry(), BlockStateProvider.DIRECT_CODEC)
        ).forEach(entry -> REGISTRY_KEYS.put(entry.getRegistryKey(), entry));
    }

    private SynchronizedRegistriesHandler() {
    }

    public static void init() {
        // check this class has been fully loaded
        if (REGISTRY_KEYS.isEmpty()) {
            throw new AssertionError();
        }
    }

    public static @Nullable RegistryEntry<?> getRegistryEntry(ResourceLocation registryKey) {
        return REGISTRY_KEYS.get(registryKey);
    }

    /**
     * Drops cached synchronized registries whose cache key matches the given filter, from every registry.
     * <p>
     * A cache entry is only replaced when registry data with different contents arrives for the same key,
     * so platforms whose cache keys can become unreachable (e.g. a proxy backend server being unregistered)
     * should call this, as cached registries are otherwise kept for the lifetime of the process. Users keep
     * the registries they already received, so this only causes the next lookup with that key to decode again.
     *
     * @param keyFilter returns true for each cache key to drop
     */
    public static void invalidateSyncedRegistries(Predicate<Object> keyFilter) {
        for (RegistryEntry<?> entry : REGISTRY_KEYS.values()) {
            entry.invalidateSyncedRegistries(keyFilter);
        }
    }

    public static void handleRegistry(
            User user, PacketWrapper<?> wrapper,
            ResourceLocation registryName,
            List<RegistryElement> elements
    ) {
        Object cacheKey = PacketEvents.getAPI().getServerManager().getRegistryCacheKey(
                user, wrapper.getServerVersion().toClientVersion());
        handleRegistry(user, wrapper, registryName, elements, cacheKey);
    }

    public static void handleRegistry(
            User user, PacketWrapper<?> wrapper,
            ResourceLocation registryName,
            List<RegistryElement> elements,
            @Nullable Object cacheKey
    ) {
        RegistryEntry<?> registryData = REGISTRY_KEYS.get(registryName);
        if (registryData == null) {
            return;
        }
        try {
            SimpleRegistry<?> syncedRegistry;
            if (FORCE_PER_USER_REGISTRIES || cacheKey == null) {
                syncedRegistry = registryData.createFromElements(elements, wrapper); // no caching
            } else {
                syncedRegistry = registryData.computeSyncedRegistry(cacheKey, elements, wrapper);
            }
            user.putRegistry(syncedRegistry);
        } catch (Exception exception) {
            throw new IllegalStateException("Error while reading registry " + registryName + " for " + user, exception);
        }
    }

    public static void handleLegacyRegistries(
            User user, PacketWrapper<?> wrapper,
            NBTCompound registryData
    ) {
        Object cacheKey = PacketEvents.getAPI().getServerManager().getRegistryCacheKey(
                user, wrapper.getServerVersion().toClientVersion());
        for (NBT tag : registryData.getTags().values()) {
            //On 1.16 they send an NBTList for dimension.
            if (tag instanceof NBTList) {
                @SuppressWarnings("unchecked")
                NBTList<NBTCompound> list = (NBTList<NBTCompound>) tag;
                handleRegistry(user, wrapper, DimensionTypes.getRegistry().getRegistryKey(),
                        RegistryElement.convertNbt(list), cacheKey);

            }
            //Newer versions
            else {
                NBTCompound compound = (NBTCompound) tag;
                // extract registry name
                ResourceLocation registryName = new ResourceLocation(compound.getStringTagValueOrThrow("type"));
                // extract registry entries
                NBTList<NBTCompound> nbtElements = compound.getCompoundListTagOrNull("value");
                if (nbtElements != null) {
                    // store registry elements
                    handleRegistry(user, wrapper, registryName,
                            RegistryElement.convertNbt(nbtElements), cacheKey);
                }
            }
        }
    }

    @ApiStatus.Internal
    @FunctionalInterface
    public interface LegacyNbtEntryDecoder<T> {

        T decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data);

        default NbtEntryDecoder<T> upgrade() {
            return (nbt, wrapper, data) ->
                    this.decode(nbt, wrapper.getServerVersion().toClientVersion(), data);
        }
    }

    @ApiStatus.Internal
    @FunctionalInterface
    public interface NbtEntryDecoder<T> {

        static <T extends MappedEntity & CopyableEntity<T>> NbtEntryDecoder<T> fromDecoder(NbtDecoder<T> decoder) {
            return (tag, wrapper, data) -> decoder.decode(tag, wrapper).copy(data);
        }

        T decode(NBT tag, PacketWrapper<?> wrapper, @Nullable TypesBuilderData data);
    }

    @ApiStatus.Internal
    public static final class RegistryEntry<T extends MappedEntity & CopyableEntity<T> & DeepComparableEntity> {

        private final IRegistry<T> baseRegistry;
        private final NbtEntryDecoder<T> decoder;

        // each registry may have a synchronized registry cache, for convenience and enhanced performance
        //
        // the key to this cache depends on the platform - it may be a constant value for bukkit servers
        // or some backend server related value for proxy servers; every entry remembers the elements it
        // was decoded from, so it is only reused for identical registry data
        private final Map<Object, CachedRegistry<T>> syncedRegistries = new ConcurrentHashMap<>();

        public RegistryEntry(
                IRegistry<T> baseRegistry,
                LegacyNbtEntryDecoder<T> decoder
        ) {
            this(baseRegistry, decoder.upgrade());
        }

        public RegistryEntry(
                IRegistry<T> baseRegistry,
                NbtDecoder<T> decoder
        ) {
            this(baseRegistry, NbtEntryDecoder.fromDecoder(decoder));
        }

        public RegistryEntry(
                IRegistry<T> baseRegistry,
                NbtEntryDecoder<T> decoder
        ) {
            this.baseRegistry = baseRegistry;
            this.decoder = decoder;
        }

        public @Nullable SimpleRegistry<T> getSyncedRegistry(Object key) {
            CachedRegistry<T> cached = this.syncedRegistries.get(key);
            return cached != null ? cached.registry : null;
        }

        /**
         * Returns the registry cached for the key, or computes and caches it if absent.
         * <p>
         * This doesn't know which registry data the supplied registry was created from, so the entry it
         * caches will be replaced by the next call to {@link #computeSyncedRegistry(Object, List, PacketWrapper)}
         * for this key.
         */
        @SuppressWarnings("unchecked")
        public SimpleRegistry<T> computeSyncedRegistry(Object key, Supplier<SimpleRegistry<?>> registry) {
            return this.syncedRegistries.computeIfAbsent(key,
                    $ -> new CachedRegistry<>(null, null, (SimpleRegistry<T>) registry.get())).registry;
        }

        /**
         * Returns the registry cached for the key if it was created from registry elements equal to the given
         * ones for the same version; otherwise creates it from the given elements and replaces the cached entry.
         * <p>
         * Comparing the elements makes the cache safe against the registry data behind a key changing, e.g. a
         * proxy backend server being restarted at the same address with a different datapack.
         */
        public SimpleRegistry<T> computeSyncedRegistry(Object key, List<RegistryElement> elements, PacketWrapper<?> wrapper) {
            ClientVersion version = wrapper.getServerVersion().toClientVersion();
            CachedRegistry<T> cached = this.syncedRegistries.get(key);
            if (cached != null && cached.matches(version, elements)) {
                return cached.registry; // fast path, without locking
            }
            // decode outside of any map lock, as decoding may be slow and keys of other
            // servers may share a hash bin with this key
            CachedRegistry<T> decoded = new CachedRegistry<>(version, copyElements(elements),
                    this.createFromElements(elements, wrapper));
            return this.syncedRegistries.merge(key, decoded, (current, $) ->
                    // keep an entry another thread cached for the same registry data in the meantime
                    current.matches(version, elements) ? current : decoded).registry;
        }

        private static List<RegistryElement> copyElements(List<RegistryElement> elements) {
            // copy nbt data, as packet listeners may modify it after it has been cached
            List<RegistryElement> copy = new ArrayList<>(elements.size());
            for (RegistryElement element : elements) {
                NBT data = element.getData();
                copy.add(new RegistryElement(element.getId(), data != null ? data.copy() : null));
            }
            return Collections.unmodifiableList(copy);
        }

        public void invalidateSyncedRegistries(Predicate<Object> keyFilter) {
            this.syncedRegistries.keySet().removeIf(keyFilter);
        }

        private void handleElement(
                SimpleRegistry<T> registry,
                RegistryElement element,
                int id, PacketWrapper<?> wrapper
        ) {
            ResourceLocation elementName = element.getId();
            ClientVersion version = wrapper.getServerVersion().toClientVersion();
            T baseEntry = this.baseRegistry.getByName(version, elementName);

            // save new element definition in base entry for comparison - and fallback
            TypesBuilderData data = new SimpleTypesBuilderData(elementName, id);
            T copiedBaseEntry = baseEntry == null ? null : baseEntry.copy(data);

            if (element.getData() != null) {
                // data was provided, use registry element sent over network
                T value = this.decoder.decode(element.getData(), wrapper, data);
                if (!value.deepEquals(copiedBaseEntry)) {
                    // only define decoded value if it doesn't match the base
                    // registry value; this ensures we don't save everything twice,
                    // if it has been already stored in memory
                    registry.define(elementName, id, value);
                    return;
                }
            }

            // fallback to looking up in vanilla registry;
            // this isn't a 100% valid solution, but a full solution to this
            // is not possible with Mojang's concept of known packs
            //
            // if packetevents isn't running on a proxy and two backend servers
            // share the same custom datapack, the entries wouldn't be sent as
            // the player would tell the server it already knows about them
            //
            // this will cause issues, especially when some datapack overrides world height
            // of a vanilla dimension - and this can't be fixed (unless I missed something)

            if (copiedBaseEntry != null) {
                registry.define(elementName, id, copiedBaseEntry);
                return;
            }

            // can't find this element anywhere
            // TODO dummy values to make at least simple stuff work?
            PacketEvents.getAPI().getLogManager().warn("Unknown registry entry "
                    + elementName + " for " + this.getRegistryKey());
        }

        public SimpleRegistry<T> createFromElements(List<RegistryElement> elements, PacketWrapper<?> wrapper) {
            SimpleRegistry<T> registry = new SimpleRegistry<>(this.getRegistryKey());
            for (int id = 0; id < elements.size(); id++) {
                RegistryElement element = elements.get(id);
                this.handleElement(registry, element, id, wrapper);
            }
            return registry;
        }

        public ResourceLocation getRegistryKey() {
            return this.baseRegistry.getRegistryKey();
        }
    }

    private static final class CachedRegistry<T extends MappedEntity> {

        // null if unknown, which never matches
        private final @Nullable ClientVersion version;
        private final @Nullable List<RegistryElement> elements;
        private final SimpleRegistry<T> registry;

        private CachedRegistry(
                @Nullable ClientVersion version,
                @Nullable List<RegistryElement> elements,
                SimpleRegistry<T> registry
        ) {
            this.version = version;
            this.elements = elements;
            this.registry = registry;
        }

        private boolean matches(ClientVersion version, List<RegistryElement> elements) {
            // registry elements compare their ids and nbt data deeply
            return this.version == version && this.elements != null && this.elements.equals(elements);
        }
    }
}
