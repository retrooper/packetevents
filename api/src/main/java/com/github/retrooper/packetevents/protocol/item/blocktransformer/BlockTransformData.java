package com.github.retrooper.packetevents.protocol.item.blocktransformer;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.github.retrooper.packetevents.protocol.util.CodecNameable;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.Direction;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public final class BlockTransformData {

    public static final NbtCodec<BlockTransformData> CODEC = new NbtMapCodec<BlockTransformData>() {
        @Override
        public BlockTransformData decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            BlockStateProvider blockStateProvider = tag.getOrThrow("block_state_provider", BlockStateProvider.CODEC, wrapper);
            Sound sound = tag.getOr("sound", Sound.CODEC, Sounds.INTENTIONALLY_EMPTY, wrapper);
            Particle particle = tag.getOr("particle", Particle.CODEC, Particle.NONE, wrapper);
            List<Direction> disallowedFaces = tag.getListOrEmpty("disallowed_faces", Direction.CODEC, wrapper);
            ResourceLocation loot = tag.getOrNull("loot", ResourceLocation.CODEC, wrapper);
            DropStrategy dropStrategy = tag.getOr("drop_strategy", DropStrategy.CODEC, DropStrategy.FROM_MIDDLE, wrapper);
            boolean updateFromNeighbors = tag.getOr("update_from_neighbors", NbtCodecs.BOOLEAN, true, wrapper);
            TransformType type = tag.getOr("transform_type", TransformType.CODEC, TransformType.SINGLE_BLOCK, wrapper);
            boolean consumeOnUse = tag.getOr("consume_on_use", NbtCodecs.BOOLEAN, true, wrapper);
            int itemDamagePerUse = tag.getOr("item_damage_on_use", NbtCodecs.INT, 0, wrapper);
            return new BlockTransformData(blockStateProvider, sound, particle, disallowedFaces, loot,
                    dropStrategy, updateFromNeighbors, type, consumeOnUse, itemDamagePerUse);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, BlockTransformData value) throws NbtCodecException {
            tag.set("block_state_provider", value.blockStateProvider, BlockStateProvider.CODEC, wrapper);
            if (!Sounds.INTENTIONALLY_EMPTY.equals(value.sound)) {
                tag.set("sound", value.sound, Sound.CODEC, wrapper);
            }
            if (value.particle != Particle.NONE) {
                tag.set("particle", value.particle, Particle.CODEC, wrapper);
            }
            if (!value.disallowedFaces.isEmpty()) {
                tag.setList("disallowed_faces", value.disallowedFaces, Direction.CODEC, wrapper);
            }
            if (value.loot != null) {
                tag.set("loot", value.loot, ResourceLocation.CODEC, wrapper);
            }
            if (value.dropStrategy != DropStrategy.FROM_MIDDLE) {
                tag.set("drop_strategy", value.dropStrategy, DropStrategy.CODEC, wrapper);
            }
            if (!value.updateFromNeighbors) {
                tag.set("update_from_neighbors", false, NbtCodecs.BOOLEAN, wrapper);
            }
            if (value.transformType != TransformType.SINGLE_BLOCK) {
                tag.set("transform_type", value.transformType, TransformType.CODEC, wrapper);
            }
            if (!value.consumeOnUse) {
                tag.set("consume_on_use", false, NbtCodecs.BOOLEAN, wrapper);
            }
            if (value.itemDamagePerUse != 0) {
                tag.set("item_damage_on_use", value.itemDamagePerUse, NbtCodecs.INT, wrapper);
            }
        }
    }.codec();

    // TODO what the fuck mojang, why did you add worldgen codecs into client-synced registries
    private final BlockStateProvider blockStateProvider;
    private final Sound sound;
    private final Particle particle;
    private final List<Direction> disallowedFaces;
    private final @Nullable ResourceLocation loot;
    private final DropStrategy dropStrategy;
    private final boolean updateFromNeighbors;
    private final TransformType transformType;
    private final boolean consumeOnUse;
    private final int itemDamagePerUse;

    // TODO use builder instead of this
    public BlockTransformData(
            BlockStateProvider blockStateProvider, Sound sound, Particle particle,
            List<Direction> disallowedFaces, @Nullable ResourceLocation loot, DropStrategy dropStrategy,
            boolean updateFromNeighbors, TransformType transformType, boolean consumeOnUse, int itemDamagePerUse
    ) {
        this.blockStateProvider = blockStateProvider;
        this.sound = sound;
        this.particle = particle;
        this.disallowedFaces = Collections.unmodifiableList(disallowedFaces);
        this.loot = loot;
        this.dropStrategy = dropStrategy;
        this.updateFromNeighbors = updateFromNeighbors;
        this.transformType = transformType;
        this.consumeOnUse = consumeOnUse;
        this.itemDamagePerUse = itemDamagePerUse;
    }

    public BlockStateProvider getBlockStateProvider() {
        return this.blockStateProvider;
    }

    public Sound getSound() {
        return this.sound;
    }

    public Particle getParticle() {
        return this.particle;
    }

    public List<Direction> getDisallowedFaces() {
        return this.disallowedFaces;
    }

    public @Nullable ResourceLocation getLoot() {
        return this.loot;
    }

    public DropStrategy getDropStrategy() {
        return this.dropStrategy;
    }

    public boolean isUpdateFromNeighbors() {
        return this.updateFromNeighbors;
    }

    public TransformType getTransformType() {
        return this.transformType;
    }

    public boolean isConsumeOnUse() {
        return this.consumeOnUse;
    }

    public int getItemDamagePerUse() {
        return this.itemDamagePerUse;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof BlockTransformData)) return false;
        BlockTransformData that = (BlockTransformData) obj;
        if (this.updateFromNeighbors != that.updateFromNeighbors) return false;
        if (this.consumeOnUse != that.consumeOnUse) return false;
        if (this.itemDamagePerUse != that.itemDamagePerUse) return false;
        if (!this.blockStateProvider.equals(that.blockStateProvider)) return false;
        if (!this.sound.equals(that.sound)) return false;
        if (this.particle != that.particle) return false;
        if (!this.disallowedFaces.equals(that.disallowedFaces)) return false;
        if (!Objects.equals(this.loot, that.loot)) return false;
        if (this.dropStrategy != that.dropStrategy) return false;
        return this.transformType == that.transformType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.blockStateProvider, this.sound, this.particle, this.disallowedFaces, this.loot, this.dropStrategy, this.updateFromNeighbors, this.transformType, this.consumeOnUse, this.itemDamagePerUse);
    }

    /**
     * A bit of hardcoding has never hurt anyone, right Mojang?
     */
    public enum Particle implements CodecNameable {

        NONE("none"),
        SCRAPE("scrape"),
        WAX_ON("wax_on"),
        WAX_OFF("wax_off"),
        ;

        public static final NbtCodec<Particle> CODEC = NbtCodecs.forEnum(values());

        private final String id;

        Particle(String id) {
            this.id = id;
        }

        @Override
        public String getCodecName() {
            return this.id;
        }
    }

    public enum DropStrategy implements CodecNameable {

        CLICKED_FACE("clicked_face"),
        FROM_MIDDLE("from_middle"),
        ;

        public static final NbtCodec<DropStrategy> CODEC = NbtCodecs.forEnum(values());

        private final String id;

        DropStrategy(String id) {
            this.id = id;
        }

        @Override
        public String getCodecName() {
            return this.id;
        }
    }

    /**
     * Mojang surely likes adding specific edge-cases here...
     */
    public enum TransformType implements CodecNameable {

        SINGLE_BLOCK("single_block"),
        COPPER_CHEST("copper_chest"),
        ;

        public static final NbtCodec<TransformType> CODEC = NbtCodecs.forEnum(values());

        private final String id;

        TransformType(String id) {
            this.id = id;
        }

        @Override
        public String getCodecName() {
            return this.id;
        }
    }
}
