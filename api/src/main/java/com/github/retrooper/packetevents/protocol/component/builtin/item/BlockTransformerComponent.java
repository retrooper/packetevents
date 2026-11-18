package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.protocol.item.blocktransformer.BlockTransformer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public class BlockTransformerComponent {

    private final BlockTransformer transformer;

    public BlockTransformerComponent(BlockTransformer transformer) {
        this.transformer = transformer;
    }

    public static BlockTransformerComponent read(PacketWrapper<?> wrapper) {
        return new BlockTransformerComponent(BlockTransformer.read(wrapper));
    }

    public static void write(PacketWrapper<?> wrapper, BlockTransformerComponent transformer) {
        BlockTransformer.write(wrapper, transformer.transformer);
    }

    public BlockTransformer getTransformer() {
        return this.transformer;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof BlockTransformerComponent)) return false;
        BlockTransformerComponent that = (BlockTransformerComponent) obj;
        return this.transformer.equals(that.transformer);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.transformer);
    }
}
