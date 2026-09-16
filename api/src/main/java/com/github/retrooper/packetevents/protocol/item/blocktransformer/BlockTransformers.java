package com.github.retrooper.packetevents.protocol.item.blocktransformer;

import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jspecify.annotations.NullMarked;

import java.util.Collections;
import java.util.List;

/**
 * @versions 26.3+
 */
@NullMarked
public final class BlockTransformers {

    private static final VersionedRegistry<BlockTransformer> REGISTRY = new VersionedRegistry<>("block_transformer");

    private BlockTransformers() {
    }

    public static VersionedRegistry<BlockTransformer> getRegistry() {
        return REGISTRY;
    }

    private static BlockTransformer define(String name, BlockTransformData datum) {
        List<BlockTransformData> transforms = Collections.singletonList(datum);
        return REGISTRY.define(name, data -> new StaticBlockTransformer(data, transforms));
    }

    // TODO buitlin stuff
    public static final BlockTransformer AXE = define("axe");
    public static final BlockTransformer HOE = define("hoe");
    public static final BlockTransformer SHOVEL = define("shovel");

    static {
        REGISTRY.unloadMappings();
    }
}
