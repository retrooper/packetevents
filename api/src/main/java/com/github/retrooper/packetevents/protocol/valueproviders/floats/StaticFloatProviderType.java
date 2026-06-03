package com.github.retrooper.packetevents.protocol.valueproviders.floats;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @versions 1.17+
 */
@NullMarked
@ApiStatus.Internal
final class StaticFloatProviderType<T extends FloatProvider> extends AbstractMappedEntity implements FloatProviderType<T> {

    private final NbtMapCodec<T> codec;

    public StaticFloatProviderType(@Nullable TypesBuilderData data, NbtMapCodec<T> codec) {
        super(data);
        this.codec = codec;
    }

    @Override
    public NbtMapCodec<T> getCodec() {
        return this.codec;
    }
}
