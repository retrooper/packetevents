package com.github.retrooper.packetevents.protocol.item.blocktransformer;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@NullMarked
final class StaticBlockTransformer extends AbstractMappedEntity implements BlockTransformer {

    private final List<BlockTransformData> transforms;

    public StaticBlockTransformer(@Nullable TypesBuilderData data, List<BlockTransformData> transforms) {
        super(data);
        this.transforms = Collections.unmodifiableList(transforms);
    }

    @Override
    public BlockTransformer copy(@Nullable TypesBuilderData newData) {
        return new StaticBlockTransformer(newData, this.transforms);
    }

    @Override
    public List<BlockTransformData> getTransforms() {
        return this.transforms;
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (!(obj instanceof StaticBlockTransformer)) return false;
        StaticBlockTransformer that = (StaticBlockTransformer) obj;
        return this.transforms.equals(that.transforms);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.transforms);
    }
}
