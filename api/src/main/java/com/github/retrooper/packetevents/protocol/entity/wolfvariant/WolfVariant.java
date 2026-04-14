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

package com.github.retrooper.packetevents.protocol.entity.wolfvariant;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.biome.Biome;
import com.github.retrooper.packetevents.protocol.world.biome.Biomes;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @versions 1.20.5+
 */
@NullMarked
public interface WolfVariant extends MappedEntity, CopyableEntity<WolfVariant>, DeepComparableEntity {

    WolfAssetSet getAssets();

    /**
     * @versions 26.1+
     */
    WolfAssetSet getBabyAssets();

    /**
     * @versions 1.20.5-1.21.4
     */
    @ApiStatus.Obsolete
    MappedEntitySet<Biome> getBiomes();

    @Deprecated
    default ResourceLocation getWildTexture() {
        return this.getAssets().getWildId();
    }

    @Deprecated
    default ResourceLocation getTameTexture() {
        return this.getAssets().getTameId();
    }

    @Deprecated
    default ResourceLocation getAngryTexture() {
        return this.getAssets().getAngryId();
    }

    static WolfVariant read(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)
                || wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21)) {
            return wrapper.readMappedEntity(WolfVariants.getRegistry());
        }
        return wrapper.readMappedEntityOrDirect(WolfVariants.getRegistry(), WolfVariant::readDirect);
    }

    static void write(PacketWrapper<?> wrapper, WolfVariant variant) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)
                || wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21)) {
            wrapper.writeMappedEntity(variant);
        } else {
            wrapper.writeMappedEntityOrDirect(variant, WolfVariant::writeDirect);
        }
    }

    @Deprecated
    static WolfVariant readDirect(PacketWrapper<?> wrapper) {
        ResourceLocation wildTexture = wrapper.readIdentifier();
        ResourceLocation tameTexture = wrapper.readIdentifier();
        ResourceLocation angryTexture = wrapper.readIdentifier();
        MappedEntitySet<Biome> biomes = MappedEntitySet.read(wrapper, Biomes.getRegistry());
        return new StaticWolfVariant(wildTexture, tameTexture, angryTexture, biomes);
    }

    @Deprecated
    static void writeDirect(PacketWrapper<?> wrapper, WolfVariant variant) {
        wrapper.writeIdentifier(variant.getWildTexture());
        wrapper.writeIdentifier(variant.getTameTexture());
        wrapper.writeIdentifier(variant.getAngryTexture());
        MappedEntitySet.write(wrapper, variant.getBiomes());
    }

    NbtCodec<WolfVariant> CODEC = new NbtMapCodec<WolfVariant>() {
        @Override
        public WolfVariant decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
                WolfAssetSet assets = WolfAssetSet.MAP_CODEC.decode(tag, wrapper);
                MappedEntitySet<Biome> biomes = tag.getOrThrow("biomes", (itag, ew) ->
                        MappedEntitySet.decode(itag, ew, Biomes.getRegistry()), wrapper);
                return new StaticWolfVariant(assets, biomes);
            }
            WolfAssetSet assets = tag.getOrThrow("assets", WolfAssetSet.CODEC, wrapper);
            WolfAssetSet babyAssets = wrapper.getServerVersion().isOlderThan(ServerVersion.V_26_1) ? assets
                    : tag.getOrThrow("baby_assets", WolfAssetSet.CODEC, wrapper);
            return new StaticWolfVariant(assets, babyAssets);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, WolfVariant value) throws NbtCodecException {
            if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
                WolfAssetSet.MAP_CODEC.encode(tag, wrapper, value.getAssets());
                tag.set("biomes", value.getBiomes(), MappedEntitySet::encode, wrapper);
            } else {
                tag.set("assets", value.getAssets(), WolfAssetSet.CODEC, wrapper);
                if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_1)) {
                    tag.set("baby_assets", value.getBabyAssets(), WolfAssetSet.CODEC, wrapper);
                }
            }
        }
    }.codec();

    @Deprecated
    static WolfVariant decode(NBT tag, ClientVersion version, @Nullable TypesBuilderData data) {
        return decode(tag, PacketWrapper.createDummyWrapper(version), data);
    }

    @Deprecated
    static WolfVariant decode(NBT tag, PacketWrapper<?> wrapper, @Nullable TypesBuilderData data) {
        return CODEC.decode(tag, wrapper).copy(data);
    }

    @Deprecated
    static NBT encode(WolfVariant variant, ClientVersion version) {
        return encode(PacketWrapper.createDummyWrapper(version), variant);
    }

    @Deprecated
    static NBT encode(PacketWrapper<?> wrapper, WolfVariant variant) {
        return CODEC.encode(wrapper, variant);
    }
}
