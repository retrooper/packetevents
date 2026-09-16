package com.github.retrooper.packetevents.protocol.world.blockentity.decopot;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
final class StaticDecoratedPotPattern extends AbstractMappedEntity implements DecoratedPotPattern {

    private final ResourceLocation assetId;

    public StaticDecoratedPotPattern(@Nullable TypesBuilderData data, ResourceLocation assetId) {
        super(data);
        this.assetId = assetId;
    }

    @Override
    public DecoratedPotPattern copy(@Nullable TypesBuilderData newData) {
        return new StaticDecoratedPotPattern(newData, this.assetId);
    }

    @Override
    public ResourceLocation getAssetId() {
        return this.assetId;
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (!(obj instanceof StaticDecoratedPotPattern)) return false;
        if (!super.equals(obj)) return false;
        StaticDecoratedPotPattern that = (StaticDecoratedPotPattern) obj;
        return this.assetId.equals(that.assetId);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.assetId);
    }
}
