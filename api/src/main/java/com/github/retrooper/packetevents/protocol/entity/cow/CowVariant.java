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

package com.github.retrooper.packetevents.protocol.entity.cow;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.util.CodecNameable;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.util.Index;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @versions 1.21.5+
 */
@NullMarked
public interface CowVariant extends MappedEntity, CopyableEntity<CowVariant>, DeepComparableEntity {

    NbtCodec<CowVariant> CODEC = new NbtMapCodec<CowVariant>() {
        @Override
        public CowVariant decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            ModelType modelType = tag.getOr("model", ModelType.CODEC, ModelType.NORMAL, wrapper);
            ResourceLocation assetId = tag.getOrThrow("asset_id", ResourceLocation.CODEC, wrapper);
            ResourceLocation babyAssetId = wrapper.getServerVersion().isOlderThan(ServerVersion.V_26_1) ? assetId
                    : tag.getOrThrow("baby_asset_id", ResourceLocation.CODEC, wrapper);
            return new StaticCowVariant(modelType, assetId, babyAssetId);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, CowVariant value) throws NbtCodecException {
            tag.set("model", value.getModelType(), ModelType.CODEC, wrapper);
            tag.set("asset_id", value.getAssetId(), ResourceLocation.CODEC, wrapper);
            if (wrapper.getServerVersion().isNewerThan(ServerVersion.V_26_1)) {
                tag.set("baby_asset_id", value.getBabyAssetId(), ResourceLocation.CODEC, wrapper);
            }
        }
    }.codec();

    ModelType getModelType();

    ResourceLocation getAssetId();

    /**
     * @versions 26.1+
     */
    ResourceLocation getBabyAssetId();

    static CowVariant read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntity(CowVariants.getRegistry());
    }

    static void write(PacketWrapper<?> wrapper, CowVariant variant) {
        wrapper.writeMappedEntity(variant);
    }

    @Deprecated
    static CowVariant decode(NBT tag, ClientVersion version, @Nullable TypesBuilderData data) {
        return CODEC.decode(tag, PacketWrapper.createDummyWrapper(version)).copy(data);
    }

    @Deprecated
    static NBT encode(CowVariant variant, ClientVersion version) {
        return CODEC.encode(PacketWrapper.createDummyWrapper(version), variant);
    }

    enum ModelType implements CodecNameable {

        NORMAL("normal"),
        COLD("cold"),
        WARM("warm"),
        ;

        private static final Index<String, ModelType> NAME_INDEX = Index.create(
                ModelType.class, ModelType::getName);
        public static final NbtCodec<ModelType> CODEC = NbtCodecs.forEnum(values());

        private final String name;

        ModelType(String name) {
            this.name = name;
        }

        public static ModelType getByName(String name) {
            return AdventureIndexUtil.indexValueOrThrow(NAME_INDEX, name);
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String getCodecName() {
            return this.name;
        }
    }
}
