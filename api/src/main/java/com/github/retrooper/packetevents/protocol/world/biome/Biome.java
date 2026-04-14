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

package com.github.retrooper.packetevents.protocol.world.biome;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.util.CodecNameable;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributeMap;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface Biome extends MappedEntity, CopyableEntity<Biome>, DeepComparableEntity {

    NbtCodec<Biome> CODEC = new NbtMapCodec<Biome>() {
        @Override
        public Biome decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
            float temperature = compound.getNumberTagOrThrow("temperature").getAsFloat();
            TemperatureModifier temperatureModifier = compound.getOr("temperature_modifier", TemperatureModifier.CODEC, TemperatureModifier.NONE, wrapper);
            float downfall = compound.getNumberTagOrThrow("downfall").getAsFloat();

            boolean precipitation;
            Category category = null;
            Float depth = null;
            Float scale = null;
            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
                precipitation = compound.getBoolean("has_precipitation");
            } else {
                precipitation = compound.getOrThrow("precipitation", Precipitation.CODEC, wrapper) != Precipitation.NONE;
                if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_19)) {
                    category = compound.getOrThrow("category", Category.CODEC, wrapper);
                    if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_18)) {
                        depth = compound.getNumberTagOrThrow("depth").getAsFloat();
                        scale = compound.getNumberTagOrThrow("scale").getAsFloat();
                    }
                }
            }

            EnvironmentAttributeMap attributes;
            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11)) {
                attributes = compound.getOr("attributes", EnvironmentAttributeMap.CODEC, EnvironmentAttributeMap.EMPTY, wrapper);
            } else {
                attributes = EnvironmentAttributeMap.EMPTY;
            }

            BiomeEffects effects = compound.getOrThrow("effects", BiomeEffects.codecWithAttributes(attributes), wrapper);
            return new StaticBiome(null, precipitation, temperature, temperatureModifier,
                    downfall, category, depth, scale, effects, attributes);
        }

        @Override
        public void encode(NBTCompound compound, PacketWrapper<?> wrapper, Biome value) throws NbtCodecException {
            compound.setTag("temperature", new NBTFloat(value.getTemperature()));
            if (value.getTemperatureModifier() != TemperatureModifier.NONE) {
                compound.set("temperature_modifier", value.getTemperatureModifier(), TemperatureModifier.CODEC, wrapper);
            }
            compound.setTag("downfall", new NBTFloat(value.getDownfall()));
            compound.set("effects", value.getEffects(), BiomeEffects.CODEC, wrapper);
            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
                compound.setTag("has_precipitation", new NBTByte(value.hasPrecipitation()));
                if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11)) {
                    compound.set("attributes", value.getAttributes(), EnvironmentAttributeMap.CODEC, wrapper);
                }
            } else {
                compound.set("precipitation", value.getPrecipitation(), Precipitation.CODEC, wrapper);
                if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_19)) {
                    if (value.getCategory() != null) {
                        compound.set("category", value.getCategory(), Category.CODEC, wrapper);
                    }
                    if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_18)) {
                        if (value.getDepth() != null) {
                            compound.setTag("depth", new NBTFloat(value.getDepth()));
                        }
                        if (value.getScale() != null) {
                            compound.setTag("scale", new NBTFloat(value.getScale()));
                        }
                    }
                }
            }
        }
    }.codec();

    boolean hasPrecipitation();

    /**
     * @versions -1.19.2
     */
    @ApiStatus.Obsolete(since = "1.19.3")
    Precipitation getPrecipitation();

    float getTemperature();

    TemperatureModifier getTemperatureModifier();

    float getDownfall();

    /**
     * @versions -1.18.2
     */
    @ApiStatus.Obsolete(since = "1.19")
    @Nullable Category getCategory();

    /**
     * @versions -1.18.2
     */
    @ApiStatus.Obsolete(since = "1.18")
    @Nullable Float getDepth();

    /**
     * @versions -1.18.2
     */
    @ApiStatus.Obsolete(since = "1.18")
    @Nullable Float getScale();

    BiomeEffects getEffects();

    /**
     * @versions 1.21.11+
     */
    EnvironmentAttributeMap getAttributes();

    @Deprecated
    static Biome decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        return CODEC.decode(nbt, PacketWrapper.createDummyWrapper(version)).copy(data);
    }

    @Deprecated
    static NBT encode(Biome biome, ClientVersion version) {
        return CODEC.encode(PacketWrapper.createDummyWrapper(version), biome);
    }

    @ApiStatus.Obsolete(since = "1.19")
    enum Category implements CodecNameable {

        NONE("none"),
        TAIGA("taiga"),
        EXTREME_HILLS("extreme_hills"),
        JUNGLE("jungle"),
        MESA("mesa"),
        PLAINS("plains"),
        SAVANNA("savanna"),
        ICY("icy"),
        THE_END("the_end"),
        BEACH("beach"),
        FOREST("forest"),
        OCEAN("ocean"),
        DESERT("desert"),
        RIVER("river"),
        SWAMP("swamp"),
        MUSHROOM("mushroom"),
        NETHER("nether"),
        UNDERGROUND("underground"),
        MOUNTAIN("mountain");

        public static final NbtCodec<Category> CODEC = NbtCodecs.forEnum(values());
        public static final Index<String, Category> ID_INDEX = Index.create(Category.class,
                Category::getId);

        private final String id;

        Category(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }

        @Override
        public String getCodecName() {
            return this.id;
        }
    }

    @ApiStatus.Obsolete(since = "1.19.3")
    enum Precipitation implements CodecNameable {

        NONE("none"),
        RAIN("rain"),
        SNOW("snow");

        public static final NbtCodec<Precipitation> CODEC = NbtCodecs.forEnum(values());
        public static final Index<String, Precipitation> ID_INDEX = Index.create(Precipitation.class,
                Precipitation::getId);

        private final String id;

        Precipitation(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }

        @Override
        public String getCodecName() {
            return this.id;
        }
    }

    enum TemperatureModifier implements CodecNameable {

        NONE("none"),
        FROZEN("frozen");

        public static final NbtCodec<TemperatureModifier> CODEC = NbtCodecs.forEnum(values());
        public static final Index<String, TemperatureModifier> ID_INDEX = Index.create(
                TemperatureModifier.class, TemperatureModifier::getId);

        private final String id;

        TemperatureModifier(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }

        @Override
        public String getCodecName() {
            return this.id;
        }
    }
}
