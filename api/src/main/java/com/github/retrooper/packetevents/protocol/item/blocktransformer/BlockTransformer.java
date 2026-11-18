package com.github.retrooper.packetevents.protocol.item.blocktransformer;

import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * @versions 26.3+
 */
@NullMarked
public interface BlockTransformer extends MappedEntity, CopyableEntity<BlockTransformer>, DeepComparableEntity {

    NbtCodec<BlockTransformer> DIRECT_CODEC = BlockTransformData.CODEC
            .applyList()
            .apply(
                    l -> new StaticBlockTransformer(null, l),
                    BlockTransformer::getTransforms
            );

    List<BlockTransformData> getTransforms();

    static BlockTransformer read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntity(BlockTransformers.getRegistry());
    }

    static void write(PacketWrapper<?> wrapper, BlockTransformer transformer) {
        wrapper.writeMappedEntity(transformer);
    }
}
