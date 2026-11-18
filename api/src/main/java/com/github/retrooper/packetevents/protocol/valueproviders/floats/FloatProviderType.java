package com.github.retrooper.packetevents.protocol.valueproviders.floats;

import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 1.17+
 */
@NullMarked
public interface FloatProviderType<T extends FloatProvider> extends MappedEntity {

    NbtCodec<FloatProviderType<?>> CODEC = NbtCodecs.forRegistry(FloatProviderTypes.getRegistry());

    NbtMapCodec<T> getCodec();
}
