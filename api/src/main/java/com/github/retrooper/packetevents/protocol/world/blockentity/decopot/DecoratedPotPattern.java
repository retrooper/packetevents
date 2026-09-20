package com.github.retrooper.packetevents.protocol.world.blockentity.decopot;
// Created by booky10 in packetevents (5:16 AM 16.09.2026)

import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface DecoratedPotPattern extends MappedEntity, CopyableEntity<DecoratedPotPattern>, DeepComparableEntity {

    NbtCodec<DecoratedPotPattern> CODEC = new NbtMapCodec<DecoratedPotPattern>() {
        @Override
        public DecoratedPotPattern decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            ResourceLocation assetId = tag.getOrThrow("asset_id", ResourceLocation.CODEC, wrapper);
            return new StaticDecoratedPotPattern(null, assetId);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, DecoratedPotPattern value) throws NbtCodecException {
            tag.set("asset_id", value.getAssetId(), ResourceLocation.CODEC, wrapper);
        }
    }.codec();

    ResourceLocation getAssetId();

    static DecoratedPotPattern read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntity(DecoratedPotPatterns.getRegistry());
    }

    static void write(PacketWrapper<?> wrapper, DecoratedPotPattern pattern) {
        wrapper.writeMappedEntity(pattern);
    }
}
